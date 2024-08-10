package edu.br.utfpr.minhabomba.entity

import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import java.math.BigDecimal

@Entity
class Abastecimento(
        @Id
        @GeneratedValue
        var id: Int,

        @Positive
        @field:NotNull(message = "{valor.notnull}")
        @Column(nullable = false, precision = 15, scale = 2)
        var litros: BigDecimal = BigDecimal.ZERO,

        @ManyToOne(optional = false)
        @JoinColumn
        var combustivel: Combustivel? = null,

        @Column(nullable = false, precision = 15, scale = 2)
        var total: BigDecimal = BigDecimal.ZERO
)