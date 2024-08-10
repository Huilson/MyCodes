package edu.br.utfpr.minhabomba.controller

import edu.br.utfpr.minhabomba.entity.Abastecimento
import edu.br.utfpr.minhabomba.entity.Combustivel
import edu.br.utfpr.minhabomba.repository.AbastecerRepository
import edu.br.utfpr.minhabomba.repository.CombustivelRepository
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*
import kotlin.jvm.optionals.getOrNull

@RestController
@RequestMapping("/abastecer")
class AbastecerController(
        private val repository: AbastecerRepository,
        private val repositoryCombustivel: CombustivelRepository
) {

    @GetMapping
    fun listar(): List<Abastecimento> {
        return repository.findAll()
    }

    @GetMapping("/{id}")
    fun buscarId(@PathVariable(name = "id") codigo: Int): ResponseEntity<Abastecimento> {
        val abastecimento = repository.findById(codigo).getOrNull()
                ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(abastecimento)
    }

    @PostMapping
    fun salvar(@Valid @RequestBody abastecimento: Abastecimento) : Abastecimento {

        val combustivel : Combustivel? = repositoryCombustivel
            .findByTipoContainsIgnoreCase(abastecimento.combustivel?.tipo ?: "")
        abastecimento.combustivel = combustivel

        if (abastecimento.combustivel != null) {
            abastecimento.total = abastecimento.combustivel!!.valor * abastecimento.litros
        }
        return repository.save(abastecimento)

    @DeleteMapping("/{id}")
    fun deletar(@PathVariable id: Int){
        repository.deleteById(id)
    }
}