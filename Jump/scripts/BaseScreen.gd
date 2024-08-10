extends Control

func _ready():
	visible = false
	modulate.a = 0.0 #altera o VISIBILITY-MODULATE-ALFA para 0%
	#Isso só possível porque esse SCRIPT herda de CONTROL (extends Control)
	
	get_tree().call_group("buttons", "set_disabled", true) #desabilita botões

func appear():
	visible = true
	var tween = get_tree().create_tween() #Tween é um objeto usado para gerar animações via scripts	
	tween.set_pause_mode(Tween.TWEEN_PAUSE_PROCESS)#muda o tipo de comportamento do tween quando passado
	
	#Esse metodo pega a própia cena, TitleScreen (SELF), no módulo CONTROL, pega o VISIBILITY-MODULATE-ALFA,
	#e preenche 100% dele, em 1,5 segundos
	tween.tween_property(self, "modulate:a", 1.0, 1.5)
	return tween

func disappear():
	get_tree().call_group("buttons", "set_disabled", true) #desabilita botões
	
	var tween = get_tree().create_tween()
	tween.set_pause_mode(Tween.TWEEN_PAUSE_PROCESS)
	tween.tween_property(self, "modulate:a", 0.0, 1.5)
	return tween
