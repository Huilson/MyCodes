package edu.br.utfpr.minhabomba.repository

import edu.br.utfpr.minhabomba.entity.Abastecimento
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AbastecerRepository: JpaRepository<Abastecimento, Int>{
}