extends Node

@onready var game = $Game
@onready var screen = $Screens

var game_in_progess = false

func _on_window_event(event):
	match event:
		DisplayServer.WINDOW_EVENT_FOCUS_IN:
			pass
		DisplayServer.WINDOW_EVENT_FOCUS_OUT:
			if  game_in_progess == true && !get_tree().paused:#se o jogo perder foco ou não estiver pausado
				MyUtility.add_log_msg("The game loses focus!")
				_on_game_pause()
		DisplayServer.WINDOW_EVENT_CLOSE_REQUEST:
			get_tree().quit()

func _ready():
	DisplayServer.window_set_window_event_callback(_on_window_event)
	
	#se receber sinal, conecta com próxima função
	screen.start_game.connect(_on_screens_start_game)
	screen.delete_level.connect(_on_screen_delete_level)
	game.pause_game.connect(_on_game_pause)
	game.player_died.connect(_on_game_player_died)

func _on_screens_start_game():
	MyUtility.add_log_msg("The game has begun!")
	game_in_progess = true
	game.new_game()

func _on_game_player_died(score, highscore):
	game_in_progess = false
	await(get_tree().create_timer(1.5).timeout)
	screen.game_over(score, highscore)

func _on_screen_delete_level():
	game_in_progess = false
	game.reset_game()

func _on_game_pause():
	get_tree().paused = true
	screen.pause_game()
