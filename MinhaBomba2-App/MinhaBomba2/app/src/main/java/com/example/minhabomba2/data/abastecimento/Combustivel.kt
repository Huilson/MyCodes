package com.example.minhabomba2.data.abastecimento

import kotlinx.serialization.Serializable
import java.math.BigDecimal

@Serializable
data class Combustivel (
    var id: Int = 0,
    var tipo: String = "", //Tipos: DIESEL, ETANOL, GASOLINA
    var valor: Double = 0.0
    //@Serializable(with = BigDecimalSerializer::class)
    //var valor: BigDecimal = BigDecimal.ZERO
)