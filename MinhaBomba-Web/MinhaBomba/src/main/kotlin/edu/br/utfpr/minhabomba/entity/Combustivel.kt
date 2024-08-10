package edu.br.utfpr.minhabomba.entity

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size
import java.math.BigDecimal

@Entity
class Combustivel (
        @Id
        @GeneratedValue
        var id: Int = 0,

        //@field:NotBlank(message = "{nome.notblank}")
        //@field:Size(max = 8, min=6)
        @Column(nullable = false)
        var tipo: String = "", //Tipos: DIESEL, ETANOL, GASOLINA

        //@field:NotNull(message = "{valor.notnull}")
        //@field:Positive(message = "{valor.positivo}")
        @Column(nullable = false, precision = 15, scale = 2)
        var valor: BigDecimal = BigDecimal.ZERO
)