package com.example.minhabomba2.data.abastecimento

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.math.BigDecimal

@Serializable
data class Abastecimento(
    var id: Int=0,

    //@Serializable(with = BigDecimalSerializer::class)
    //var litros: BigDecimal = BigDecimal.ZERO,
    var litros: Double = 0.0,

    var combustivel: Combustivel? = null,

    //@Serializable(with = BigDecimalSerializer::class)
    //var total: BigDecimal = BigDecimal.ZERO
    var total: Double = 0.0
){
//    object BigDecimalSerializer: KSerializer<BigDecimal> {
//        override fun deserialize(decoder: Decoder): BigDecimal {
//            return decoder.decodeString().toBigDecimal()
//        }
//
//        override fun serialize(encoder: Encoder, value: BigDecimal) {
//            encoder.encodeString(value.toPlainString())
//        }
//
//        override val descriptor: SerialDescriptor
//            get() = PrimitiveSerialDescriptor("BigDecimal", PrimitiveKind.STRING)
//    }
}

