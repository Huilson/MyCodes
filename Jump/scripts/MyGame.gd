extends Node2D

signal player_died(score, highscore)
signal pause_game

@onready var generator = $LevelGenerator
@onready var ground_sprite = $GroundSprite
@onready var p_far = $ParallaxBackground/ParallaxFar
@onready var p_middle = $ParallaxBackground/ParallaxMiddle
@onready var p_near = $ParallaxBackground/ParallaxNear
@onready var hud = $UILayer/HUD

var player:Player = null
var camera:Camera2D = null
var player_spawn_pos: Vector2
var viewport_size : Vector2

var score: int = 0 #auto casting
var highscore: int = 0
var save_file_path = "user://highscore.save"

#variaveis de recurso
var camera_scene = preload("res://scenes/camera.tscn") #pega cena da camera
var player_scene = preload("res://scenes/player.tscn") #pega cena do player

func _ready():
	viewport_size = get_viewport_rect().size
	var player_spawn_pos_y_offset = 135 #margem
	
	#var para posicionar jogador no centro da tela (eixo X)
	player_spawn_pos.x = viewport_size.x/2 
	#var para posicionar jogador no fundo da tela (eixo Y)
	player_spawn_pos.y = viewport_size.y - player_spawn_pos_y_offset
	
	#seta localização da base/chão (SPRITE)
	ground_sprite.global_position.x = viewport_size.x / 2.0
	ground_sprite.global_position.y = viewport_size.y
	#funções que determinam movimento do parallax e tamanho das imagens(sprites)
	
	setup_parrallax_layer(p_far)
	setup_parrallax_layer(p_middle)
	setup_parrallax_layer(p_near)
	
	load_score()
	hud.visible = false
	hud.set_score(0)
	hud.pause_game.connect(_on_pause_game) #Espera aperta o botao pause
	ground_sprite.visible = false
	#new_game()

func get_parallax_sprite_scale(parallax_sprite : Sprite2D):
	var parallax_texture = parallax_sprite.get_texture()#pega info sobre a img
	var parallax_texture_width = parallax_texture.get_width()#pega largura da img
	
	var scale = viewport_size.x / parallax_texture_width#determina escala da img
	var result =  Vector2(scale, scale)#seta altura e largura da img
	return result#retorna img
	

func setup_parrallax_layer(parallax_layer : ParallaxLayer):
	var parallax_sprite = parallax_layer.find_child("Sprite2D")#tenta achar um nó chamado Sprite2D
	if parallax_sprite != null:
		#retorna a escala correta de um sprite (VECTOR2)
		parallax_sprite.scale = get_parallax_sprite_scale(parallax_sprite)
		
		#determina velocidade de movimento dos parralax (dos 3)
		parallax_layer.motion_mirroring.y = parallax_sprite.scale.y * parallax_sprite.get_texture().get_height()

func _process(_delta):
	if Input.is_action_just_pressed("quit"):
		get_tree().quit()#chamada para sair do jogo
	if Input.is_action_just_pressed("reset"):
		get_tree().reload_current_scene()#chamada para recomeçar o jogo
	if player:
		if score < (viewport_size.y - player.global_position.y): #Atualiza score apenas se progredir
			score = (viewport_size.y - player.global_position.y) #calculo do score
			hud.set_score(score)
			#MyUtility.add_log_msg("Score: "+str(score)+"\n")
	

func new_game():
	reset_game()#antes de começar o jogo, reseta o jogo
	
	player = player_scene.instantiate() #cria um novo player
	player.global_position = player_spawn_pos #defina posição do avatar
	
	player.died.connect(_on_player_died)#se receber sinal, chama função
	
	add_child(player) #Adiciona
	
	camera = camera_scene.instantiate() #cria uma nova camera
	camera.setup_camera(player) #Build
	add_child(camera) #Adiciona

	#se houver jogador, seta no GERADOR
	if player:
		generator.setup(player)
		generator.start_generator()
		
	hud.visible = true
	ground_sprite.visible = true
	score = 0

func _on_player_died():
	hud.visible = false
	
	if score > highscore:
		highscore = score
		save_score()
		MyUtility.add_log_msg("Best Score: "+str(highscore)+"\n")
		
	player_died.emit(score,highscore)

func reset_game():
	ground_sprite.visible = false
	hud.set_score(0)
	hud.visible = false
	generator.reset_level()
	if player != null:
		player.queue_free() #remove player
		player = null #não deixa instanciar player
		generator.player = null #evita que o jogo crash
	
	if camera != null:
		camera.queue_free() #remove a camera
		camera = null

func save_score():
	MyUtility.add_log_msg("Salvando arquivo")
	var file = FileAccess.open(save_file_path, FileAccess.WRITE)	
	file.store_var(highscore)
	file.close()

func load_score():
	MyUtility.add_log_msg("Carregando arquivo")
	if FileAccess.file_exists(save_file_path):
		var file = FileAccess.open(save_file_path, FileAccess.READ)
		highscore = file.get_var()
		file.close()
	else:
		highscore = 0

func _on_pause_game():
	pause_game.emit()
