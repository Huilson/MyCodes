package edu.br.utfpr.minhabomba.repository

import edu.br.utfpr.minhabomba.entity.Combustivel
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CombustivelRepository : JpaRepository <Combustivel, Int>{
    fun findByTipoContainsIgnoreCase(tipo: String): Combustivel
}