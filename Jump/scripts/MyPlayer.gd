extends CharacterBody2D
class_name Player #variavel que se refere a esse script/cena que pode ser chamada em outro script

signal died

#Renomear para facilitar
@onready var animator = $AnimationPlayer
@onready var cshape = $CollisionShape2D

var speed = 300.0
var speedMobile = 130.0
var gravity = 15.0 #velocidade de queda do avatar
var max_gravity_fall = 1000.0 #MAX que o avatar no eixo Y pode cair
var viewport_size #variavel que usada para armazenar tamanho da tela (VECTOR2)
var jump_velocity = -800 #precisa ser negativo para "subir" no eixo Y 

var use_accelerometer = false
var dead = false #verificação dos status do player

func _ready():
	viewport_size = get_viewport_rect().size#função que retorna vetor com tamanho da tela
	
	#detecta SISTEMA OPERACIONAL
	var os_name = OS.get_name()
	if os_name == "Android" || os_name ==  "iOS":
		use_accelerometer = true

func _process(_delta):
	if velocity.y > 0:#se a velocidade do avatar for crescente (eixo Y) esta caindo
		if animator.current_animation != "fall":#Se animação atual for diferente de FALL
			animator.play("fall")#Reproduz o sprite FALL
	elif velocity.y < 0:# se a velocidade do avatar for decrescente (eixo Y) esta pulando
		if animator.current_animation != "jump":#Se animação atual for diferente de JUMP
			animator.play("jump")#Reproduz o sprite JUMP

func _physics_process(_delta):
	velocity.y += gravity #determina gravida (EIXO Y)
	if (velocity.y > max_gravity_fall):
		velocity.y = max_gravity_fall #limita queda do avatar, nesse caso não passará de 1000.0
	
	if !dead:
		if  use_accelerometer:#se for MOBILE
			var mobile_input = Input.get_accelerometer()
			print("Accelerometer: "+str(mobile_input))
			velocity.x = mobile_input.x * speedMobile
		#se for DESKTOP
		else:
			var direction = Input.get_axis("move_left","move_right")#-1 para move_left, +1 para move_right
			if direction:
			#Caso seja diferente de 0, move de acordo com o eixo 1(direita) ou -1(esquerda)
				velocity.x = direction * speed
			else:
				#Esse método faz o avatar parar, a speed determina o quanto irá demorar para parar
				velocity.x = move_toward(velocity.x, 0, speed)
	
	#Faz cm que o avatar se mova para o lado de acordo com os eixos
	move_and_slide()
	
	var margin = 20 #variavel para determinar margem da tela
	#se a posição do avatar for além do tamanho da tela, reseta a posição do avatar (eixo X)
	if global_position.x > viewport_size.x + margin:
		global_position.x = -margin
	#se a posição do avatar for menor que a margem, joga ele para o outro lado da tela
	if global_position.x < -margin:
		global_position.x = viewport_size.x + margin

func jump():
	velocity.y = jump_velocity #determina altura do pulo
	MyUtility.add_log_msg("Jump modafoka! \n")
	SoundFX.play("Jump")

func _on_visible_on_screen_notifier_2d_screen_exited():
	MyUtility.add_log_msg("Player die! \n")
	die()
	
func die():
	if !dead:
		dead = true
		cshape.set_deferred("disabled", true)#faz o avatar parar de pular
		died.emit()
		SoundFX.play("Fall")
