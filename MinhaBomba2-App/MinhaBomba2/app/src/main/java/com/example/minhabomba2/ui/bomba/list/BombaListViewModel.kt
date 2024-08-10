package com.example.minhabomba2.ui.bomba.list

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.minhabomba2.data.abastecimento.Abastecimento
import com.example.minhabomba2.data.network.ApiService
import kotlinx.coroutines.launch

data class BombaListUiState(
    val loading : Boolean = false,
    val hasError : Boolean = false,
    val abastecer : List<Abastecimento> = listOf()
){
    val success get(): Boolean = !loading && !hasError
}

class BombaListViewModel : ViewModel() {
    var uiState: BombaListUiState by mutableStateOf(BombaListUiState())

    init {
        load()
    }

    fun load(){
        uiState = uiState.copy(
            loading = true,
            hasError = false
        )
        viewModelScope.launch {
            uiState = try {
                val abastecer = ApiService.conection.listar()
                uiState.copy(
                    abastecer = abastecer,
                    loading = false
                )
            } catch (ex: Exception){
                Log.i("APIError", "Error on API List: $ex")
                uiState.copy(
                    hasError = true,
                    loading = false
                )
            }
        }
    }
}