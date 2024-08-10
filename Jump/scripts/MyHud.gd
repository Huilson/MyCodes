extends Control

signal pause_game

@onready var topbar = $TopBar
@onready var bg = $TopBarBg
@onready var label = $TopBar/Label

func _ready():
	var os_name = OS.get_name()
	if os_name == "Android" || os_name ==  "iOS":
		var safe_area = DisplayServer.get_display_safe_area() #pega o tamanho da tela (sem poluição)
		var safe_area_top = safe_area.y #pega o tamnho do eixo y
		
		#Configurações para iOS
		if os_name == "iOS":
			var screen_scale = DisplayServer.screen_get_scale()
			safe_area_top = (safe_area_top / screen_scale)
		#FIM DAS CONFIGURAÇÕES
		
		#reposiciona HUD
		topbar.position.y = safe_area_top
		var margin = 10
		bg += safe_area_top + margin

func _on_pause_button_pressed():
	SoundFX.play("Click")
	pause_game.emit()
	
	#Faz o todo jogo pausar (vice-versa), ver PROCESS-MODE
	#get_tree().paused = !get_tree().paused 

func set_score(new_score):
	label.text = str(new_score)
