extends TextureButton

class_name ScreenButton

signal clicked(button)

func _on_pressed():
	clicked.emit(self)#SELF é um keyword usada para referência ao objeto que o script é anexado
	#com isso podemos passar todo esse objeto pelo SIGNAL
