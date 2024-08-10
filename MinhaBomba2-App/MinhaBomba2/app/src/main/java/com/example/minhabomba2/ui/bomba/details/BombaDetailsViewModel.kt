package com.example.minhabomba2.ui.bomba.details

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.minhabomba2.data.abastecimento.Abastecimento
import com.example.minhabomba2.data.network.ApiService
import kotlinx.coroutines.launch

data class AbastecerDetailsUiState(
    val isLoading: Boolean = false,
    val hasErrorLoading: Boolean = false,
    val abastecer : Abastecimento = Abastecimento(),
    val isDeleting : Boolean = false,
    val hasErrorDeleting: Boolean = false,
    val abastecerDeleted: Boolean = false,
    val showConfirmDialog: Boolean = false
){
    val isSuccessLoading get() : Boolean = !isLoading && !hasErrorLoading
}

class BombaDetailsViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
    private val abastecerId: Int = savedStateHandle.get<Int>("id")!!

    var uiState: AbastecerDetailsUiState by mutableStateOf(AbastecerDetailsUiState())

    init {
       loadBomba()
    }

    fun loadBomba(){
        uiState = uiState.copy(
            isLoading = true,
            hasErrorLoading = false,
        )
        viewModelScope.launch {
            uiState = try {
                uiState.copy(
                    isLoading = false,
                    abastecer = ApiService.conection.buscarId(abastecerId)
                )
            }catch (ex: Exception){
                Log.i("APIError", "Error on API Details: $ex")
                uiState.copy(
                    isLoading = false,
                    hasErrorLoading = true
                )
            }
        }
    }

    fun dismissConfirmationDialog(){
        uiState =uiState.copy(
            showConfirmDialog = false

        )
    }

    fun showConfirmationDialog(){
        uiState =uiState.copy(
            showConfirmDialog = true

        )
    }

    fun delete(){
        uiState = uiState.copy(
            isDeleting = true,
            hasErrorDeleting = false,
            showConfirmDialog = false
        )
        viewModelScope.launch {
            uiState = try {
                ApiService.conection.deletar(abastecerId)
                uiState.copy(
                    isDeleting = false,
                    abastecerDeleted = true
                )
            }catch (ex: Exception){
                Log.i("APIError", "Error on API Details: $ex")
                uiState.copy(
                    isDeleting = false,
                    hasErrorDeleting = true
                )
            }
        }
    }
}