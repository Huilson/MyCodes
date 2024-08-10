package com.example.minhabomba2.data.network

import com.example.minhabomba2.data.abastecimento.network.ApiBombaService
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory


private val json = Json { ignoreUnknownKeys = true }
private val jsonConverterFactory = json.asConverterFactory("application/json".toMediaType())
private const val API_ABASTECIMENTO_BASE_URL = "http://10.0.2.2:8080"

private val apiAbastecimentoClient = Retrofit.Builder()
    .addConverterFactory(jsonConverterFactory)
    .baseUrl(API_ABASTECIMENTO_BASE_URL)
    .build()

object ApiService{
    val conection: ApiBombaService by lazy {
        apiAbastecimentoClient.create(ApiBombaService::class.java)
    }
}
