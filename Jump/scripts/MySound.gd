extends Node

#DICINÁRIO
var sounds = {
	"Click" : load("res://assets/sound/Click.wav"),
	"Fall" : load("res://assets/sound/Fall.wav"),
	"Jump" : load("res://assets/sound/Jump.wav")
}

@onready var sound_players = get_children()

func play(sound_name):
	var sound_play = sounds[sound_name] #pega o som que é preciso tocar
	
	for sound_player in sound_players:
		if !sound_player.playing:
			sound_player.stream = sound_play #carrega um StreamPlayer de audio
			sound_player.play() #toca o audio
			return #evita que seja reproduzido por vários StreamPlayer
