extends Camera2D

@onready var destroyer = $Destroyer
@onready var destroyer_shape = $Destroyer/CollisionShape2D

var player:Player = null #Definimos a variavel com tipagem Player (ver MyPlayer)
var viewport_size

func _ready():
	if player: #recall, apenas consertar um bug
		global_position.y = player.global_position.y #recall, apenas consertar um bug
		
	viewport_size = get_viewport_rect().size
	#seta a camera no centro do eixo X
	global_position.x = viewport_size.x/2
	#para não ver as plataformas serem destruídas retire o "/2"
	
	#limites da camera
	limit_bottom = viewport_size.y 
	limit_left = 0
	limit_right = viewport_size.x
	#position.y é relativa e não global
	#esta conta faz com que o Destroyer fica na parte de baixo da tela
	destroyer.position.y = viewport_size.y / 2.0
	
	#cria um novo colisor
	var rect_shape = RectangleShape2D.new()
	#var que armazena o tamanho do colisor de acordo com o tamanho da tela e distância do eixo X (200)
	var rect_shape_size = Vector2(viewport_size.x, 200)
	#seta o tamanho dentro do colisor
	rect_shape.set_size(rect_shape_size)
	#BUILD
	destroyer_shape.shape = rect_shape
	#FIM BUILD

func _process(_delta):
	if player:
		var margin = 420 #margem para evitar que o avatar desapareça
		if limit_bottom > player.global_position.y + margin:
			#quando o avatar subir evita que a camera volta a descer
			limit_bottom = int(player.global_position.y + margin)
	
	#var que armazena um Array de AREA2D
	var overlapping_areas = destroyer.get_overlapping_areas()
	if overlapping_areas.size() > 0:
		for area in overlapping_areas:
			if area is Plataform:
				#destroi plataformas tocadas pelo COLLISION do DESTROYER
				area.queue_free()

func setup_camera(_player: Player):
	if _player: #verifica se o _player não é nulo
		player = _player #passa o _player para a variavel global

func _physics_process(_delta):	
	if player: #verifica se a variavel global não é nula
		global_position.y = player.global_position.y#seta a posição da camera igual a posição do jogador
