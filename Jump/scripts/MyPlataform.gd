extends Area2D

class_name Plataform# variavel que se refere a esse script/cena que pode ser chamada em outro script

func _on_body_entered(body):#Sinal que detecta colisão
	if  body is Player:#Detecta se o BODY PLAYER toca no BODY PLATAFORM
		#se entrar nesse if, significa que agora BODY é o PLAYER
		if body.velocity.y > 0:
			body.jump()
