extends CanvasLayer

signal start_game
signal delete_level

@onready var console = $Debug/ConsoleLog
@onready var title = $TitleScreen
@onready var pause = $PauseScreen
@onready var over = $GameOverScreen
@onready var over_score = $GameOverScreen/Box/ScoreLabel
@onready var over_high = $GameOverScreen/Box/HighScoreLabel

var current_screen = null

func _ready():
	console.visible = false #desabilita botão ao começar game
	
	register_buttons()
	change_screen(title)

func _process(delta):
	pass

func _on_toggle_console_pressed():
	console.visible = !console.visible #função para ligar e desligar botão
	
func register_buttons():
	var buttons = get_tree().get_nodes_in_group("buttons")#Pega todos os nós que pertecem ao grupo BUTTONS
	if buttons.size() > 0:
		for button in buttons:
			if button is ScreenButton:
				button.clicked.connect(_on_button_pressed)#Chama uma função

#Essa função verifica o nome do botão e então faz alguma coisa
func _on_button_pressed(button):
	SoundFX.play("Click")
	match button.name:
		"Play":
			change_screen(null)#faz a tela desaparecer
			await(get_tree().create_timer(1.5).timeout)#Timer para esperar animação (ver tween no script BASE)
			start_game.emit()#transmite o SIGNAL
			
		"Retry":
			MyUtility.add_log_msg("Retry button pressed! \n")
			change_screen(null)
			await(get_tree().create_timer(1.5).timeout)
			get_tree().paused = false
			start_game.emit()
			
		"Close":
			MyUtility.add_log_msg("Close button pressed! \n")
			change_screen(null)
			await(get_tree().create_timer(1.5).timeout)
			get_tree().paused = false			
			
		"Back":
			MyUtility.add_log_msg("Back button pressed! \n")
			change_screen(title)
			get_tree().paused = false
			delete_level.emit()
			
		"OverRetry":
			MyUtility.add_log_msg("OverRetry button pressed! \n")
			change_screen(null)
			await(get_tree().create_timer(1.5).timeout)
			start_game.emit()
			
		"OverBack":
			MyUtility.add_log_msg("OverBack button pressed! \n")
			change_screen(title)
			delete_level.emit()

func change_screen(new_screen):
	if current_screen != null:
		var tween = current_screen.disappear()
		await(tween.finished) #Espera a animação terminar antes de continuar o código
		current_screen.visible = false #Destroí tela atual
	current_screen = new_screen #nova tela passada por parametro se torna a tela atual
	if current_screen != null:
		var tween = current_screen.appear()
		await(tween.finished)
		get_tree().call_group("buttons", "set_disabled", false) #desabilita botões

func game_over(score, highscore):
	over_score.text = "Score: "+ str(score)
	over_high.text = "Best: "+ str(highscore)
	change_screen(over)

func pause_game():
	change_screen(pause)
