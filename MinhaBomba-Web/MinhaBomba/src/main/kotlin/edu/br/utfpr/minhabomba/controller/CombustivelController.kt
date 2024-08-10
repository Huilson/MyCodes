package edu.br.utfpr.minhabomba.controller

import edu.br.utfpr.minhabomba.entity.Abastecimento
import edu.br.utfpr.minhabomba.entity.Combustivel
import edu.br.utfpr.minhabomba.repository.CombustivelRepository
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import kotlin.jvm.optionals.getOrNull

@RestController
@RequestMapping("/combustivel")
class CombustivelController (
        private val repository: CombustivelRepository
){
    /*@GetMapping
    fun listarCombustivel(@RequestParam tipo: String?):List<Combustivel>{
        val tipos = tipo?.let {
            repository.findByTipoContainsIgnoreCase(it)
        } ?: repository.findAll()
        return tipos
    }*/

    @GetMapping("/tudo")
    fun listaTudo(): List<Combustivel> {
        return repository.findAll()
    }

    @GetMapping("/tipo/{tipo}")
    fun listaTipo(@PathVariable tipo: String): ResponseEntity<Combustivel> {
        val combustivel = repository.findByTipoContainsIgnoreCase(tipo)
        return ResponseEntity.ok(combustivel)
    }

    @GetMapping("/{id}")
    fun buscarId(@PathVariable(name = "id") codigo: Int): ResponseEntity<Combustivel>{
        val combustivel = repository.findById(codigo).getOrNull()
                ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(combustivel)
    }

    @PostMapping
    fun salvar(@Valid @RequestBody combustivel: Combustivel): Combustivel{
        return repository.save(combustivel)
    }

    @DeleteMapping("/{id}")
    fun deletar(@PathVariable(name = "id") codigo: Int): ResponseEntity<Unit>{
        repository.deleteById(codigo)
        return ResponseEntity.noContent().build()
    }
}