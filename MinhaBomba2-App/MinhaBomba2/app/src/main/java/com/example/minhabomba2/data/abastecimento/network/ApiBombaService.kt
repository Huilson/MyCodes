package com.example.minhabomba2.data.abastecimento.network

import com.example.minhabomba2.data.abastecimento.Abastecimento
import com.example.minhabomba2.data.abastecimento.Combustivel
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiBombaService{
    @GET("abastecer")
    suspend fun listar(): List<Abastecimento>

    @GET("abastecer/{id}")
    suspend fun buscarId(@Path("id") id:Int): Abastecimento

    @DELETE("abastecer/{id}")
    suspend fun deletar(@Path("id") id:Int)

    @POST("abastecer")
    suspend fun salvar(@Body abastecer: Abastecimento): Abastecimento

    @GET("combustivel/tudo")
    suspend fun listaTudo(): List<Combustivel>
}