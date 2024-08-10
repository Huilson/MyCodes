extends Node2D
@onready var plataform_parent = $PlataformParent

var plataform_scene = preload("res://scenes/plataform.tscn")
var player : Player = null

#variaveis de geração de troncos
var start_plataform_y
var viewport_size
var generated_plataform_count = 0
var level_size = 50
var level_d = 1.5
var y_distance_between_plataform = 100
var level_difficult = 0.5
var plataform_width = 136 #tamanho da plataforma (collisionShape2D/shape/rectangle)
#o tamanho do colisor é 134, mas com 2px extra fica mais bonito


func _ready():
	viewport_size = get_viewport_rect().size
	#para gerar plataformas, passa o tamanho da tela (eixo Y) e a distância entre as plataformas x 2
	start_plataform_y = viewport_size.y - (y_distance_between_plataform*2)
	print("Tamnho do eixo Y:"+str(viewport_size.y))
	generated_plataform_count = 0
	
func start_generator():
	#ao gerar o mapa, passa se TRUE para cria o chão
	generate_level(start_plataform_y, true)

func _process(_delta):
	if player:#se o jogador não for nulo
		var py = player.global_position.y
		#para calcular o fim do mapa, 
		#é preciso ter o valor da 1º platafirma - o nº de plataformas x o espaço entre elas
		var end_of_level_p = start_plataform_y - (generated_plataform_count * y_distance_between_plataform)
		#linha limite para gerar novas plataformas
		var threshold = end_of_level_p + (y_distance_between_plataform*6)
		if py <= threshold:
			#gera novas plataformas, quando passa da linha limite, pasa FALSE para não gerar chão
			generate_level(end_of_level_p, false)

func generate_level(start_y:float, generate_ground:bool):
	if generate_ground == true:
		# --------------- GERAR CHÃO ---------------
		#adiciona uma plataforma extra para evitar "gaps"
		var ground_layer_plataform_count = (viewport_size.x / plataform_width)+1 
		#calculo para preencher a tela com plataformas
		var ground_layer_y_offset = 62#tamanho da plataform em px no eixo Y
		
		for i in range(ground_layer_plataform_count):#enquanto a tela não for preenchida, faça
			#eixo x = i x tamanho da plataforma(colisor)
			#eixo y = tamanho da tela - tamanho da plataforma(eixo y)
			var ground_location = Vector2(i * plataform_width, viewport_size.y - ground_layer_y_offset)
			create_plataform(ground_location)#cria plataformas do tamanho da tela, ver calculo acima
		# --------------- FIM GERAR CHÃO --------------- 
		#FIM IF
	# --------------- GERAR TRONCOS --------------- 
	#conforme o avatar sobe o eixo Y vai ficando negativo, subir faz com que o eixo Y fique
	#proximo de -infinito, por isso é subtraído da distância entre as plataformas
	for i in range(level_size):
		var max_x_position = viewport_size.x - plataform_width #limitar tamanho max do eixo x
		var random_x = randf_range(0.0, max_x_position)#gera um numero aleatorio
		var location : Vector2 = Vector2.ZERO#só pra tirar o warning
		
		#gera plataformas aleatoriamente, de acordo com o numero gerado na função RANF_RANGE
		location.x = random_x
		#é preciso gerar novas plataformas conferme o eixo Y sobe
		
		location.y = start_y - (y_distance_between_plataform * i)
		
		create_plataform(location)
		generated_plataform_count+= 1
		y_distance_between_plataform += level_d
		level_d += 0.1
		print(y_distance_between_plataform)
	# --------------- FIM GERAR TRONCOS --------------- 

func create_plataform(location: Vector2):
	var plataform= plataform_scene.instantiate()#cria uma nova plataforma
	plataform.global_position = location#pega uma localização
	plataform_parent.add_child(plataform)#Adiciona plataforma no local especificado por POSITION 
	return plataform
	#se for TRUE, gera o chão, ver _READY()

#função ue seta o jogador (SCRIPT/SCENE)
func setup(_player : Player):
	if _player:
		player = _player

func reset_level():
	generated_plataform_count = 0 #reseta contador
	for plataform in plataform_parent.get_children(): #dentro do parent percorre todas as plataformas
		plataform.queue_free() #Remove plataforma
