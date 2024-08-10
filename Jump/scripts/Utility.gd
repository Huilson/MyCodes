extends Node
#Essa classe é SINGLETON, pode ser acessada em todo projeto
func add_log_msg(msg : String): 
	var console = get_tree().get_first_node_in_group("debug_console")#acha um grupo chamado DEBUG_CONSOLE
	if console:
		var log_label = console.find_child("LogLabel")#Acha um nó LogLabel
		if  log_label:
			log_label.text += msg 
