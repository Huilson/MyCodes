package com.example.minhabomba2.ui.bomba.form

import android.util.Log
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.minhabomba2.R
import com.example.minhabomba2.data.abastecimento.Abastecimento
import com.example.minhabomba2.data.abastecimento.Combustivel
import com.example.minhabomba2.data.network.ApiService
import kotlinx.coroutines.launch

data class FormField(
    val value: String = "",
    @StringRes
    val errorMessageCode: Int? = null
)

data class FormState(
    val tipo: FormField = FormField(),
    val valor: FormField = FormField(),
    val litros: FormField = FormField(),
    val total: FormField = FormField()
){
    val isValid get(): Boolean = tipo.errorMessageCode == null &&
            valor.errorMessageCode == null &&
            litros.errorMessageCode == null
}

data class BombaFormUiState(
    val fuels: List<Combustivel> = listOf(),
    val bombaId: Int = 0,
    val isLoading: Boolean = false,
    val hasErrorLoading: Boolean = false,
    val formState: FormState = FormState(),
    val isSaving: Boolean = false,
    val hasErrorSaving: Boolean = false,
    val itemSaved: Boolean = false
){
    val isNewItem get(): Boolean = bombaId <= 0
    val isSucessLoading get() :Boolean = !isLoading && !hasErrorLoading
}

class BombaFormViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
    private val bombaId: Int = savedStateHandle.get<String>("id")?.toIntOrNull() ?: 0
    private val fuels: List<Combustivel> = listOf()

    var uiState : BombaFormUiState by mutableStateOf(BombaFormUiState())

    init {
        if(bombaId > 0){
            loadBomba()
        }else{
            loadFuel()
        }
    }

    fun loadFuel(){
        Log.i("fuels","loaded fuels only...")
        uiState = uiState.copy(
            isLoading = true,
            hasErrorLoading = false,
        )
        viewModelScope.launch {
            uiState = try {
                Log.d("fuels","loaded fuels try...")
                val fuels : List<Combustivel> =  ApiService.conection.listaTudo()
                Log.d("fuels","loaded fuels: $fuels")
                uiState.copy(
                    isLoading = false,
                    fuels = fuels)
            } catch (ex: Exception){
                Log.i("ApiError", "Error on API form fuel: $ex")
                uiState.copy(
                    isLoading = false,
                    hasErrorLoading = true
                )
            }
        }
    }

    fun loadBomba(){
        uiState = uiState.copy(
            isLoading = true,
            hasErrorLoading = false,
            bombaId = bombaId
        )
        viewModelScope.launch {
            uiState = try {
                val bomba = ApiService.conection.buscarId(bombaId)
                val fuels : List<Combustivel> =  ApiService.conection.listaTudo()
                uiState.copy(
                    isLoading = false,
                    fuels = fuels,
                    formState = FormState(
                        tipo = FormField((bomba.combustivel?.tipo ?: 0.0).toString()),
                        valor = FormField((bomba.combustivel?.valor ?: 0.0).toString()),
                        litros = FormField((bomba.litros).toString()),
                        total = FormField((bomba.total).toString())
                    )
                )
            } catch (ex: Exception){
                Log.i("ApiError", "Error on API form: $ex")
                uiState.copy(
                    isLoading = false,
                    hasErrorLoading = true
                )
            }
        }
    }

    fun onLitrosChanged(value: String) {
        if(uiState.formState.litros.value != value){
            uiState = uiState.copy(
                formState = uiState.formState.copy(
                    litros = uiState.formState.litros.copy(
                        value = value,
                        errorMessageCode = validateLitros(value)
                    )
                )
            )
        }
    }

    fun onValorChanged(value: String) {
        if(uiState.formState.valor.value != value){
            uiState = uiState.copy(
                formState = uiState.formState.copy(
                    valor = uiState.formState.valor.copy(
                        value = value
                    )
                )
            )
        }
    }
    fun onTipoChanged(value: String) {
        if(uiState.formState.tipo.value != value){
            uiState = uiState.copy(
                formState = uiState.formState.copy(
                    tipo = uiState.formState.tipo.copy(
                        value = value
                    )
                )
            )
        }
    }
    fun onTotalChanged(value: String) {
        if(uiState.formState.total.value != value){
            uiState = uiState.copy(
                formState = uiState.formState.copy(
                    total = uiState.formState.total.copy(
                        value = value
                    )
                )
            )
        }
    }

    @StringRes
    private fun validateLitros(campo: String): Int? = if (campo.isBlank()){
        R.string.valid_field_message
    } else {
        null
    }

    @StringRes
    private fun validateTipo(campo: String): Int? = if (campo.isBlank()){
        R.string.valid_field_message
    } else {
        null
    }

    fun save(){
        if(!isValidForm()){
            return
        }
        uiState = uiState.copy(
            isSaving = true,
            hasErrorSaving = false
        )
        viewModelScope.launch {
            val abastecer = Abastecimento(
                id = bombaId,
                combustivel = Combustivel(
                  tipo = uiState.formState.tipo.value,
                  valor = uiState.formState.valor.value.toDouble()
                ),
                litros = uiState.formState.litros.value.toDouble(),
                total = uiState.formState.valor.value.toDouble() * uiState.formState.litros.value.toDouble()
            )
            uiState = try {
                Log.i("fuel","Tipo: ${abastecer.combustivel?.tipo}")
                Log.i("fuel","Litros: ${abastecer.litros}")
                if (!abastecer.combustivel?.tipo.equals("Escolha um Combustivel")) {
                    Log.i("fuel","caiu no if")
                    ApiService.conection.salvar(abastecer)
                } else {
                    Log.i("fuel","caiu no else")
                    uiState.copy(
                        isSaving = false,
                        hasErrorSaving = true
                    )
                }
                uiState.copy(
                    isSaving = false,
                    itemSaved = true
                )
            } catch (ex: Exception){
                Log.i("ApiError", "Error on Api Save: $ex")
                uiState.copy(
                    isSaving = false,
                    hasErrorSaving = true
                )
            }
        }
    }

    fun isValidForm():Boolean{
        uiState = uiState.copy(
            formState = uiState.formState.copy(
                litros = uiState.formState.litros.copy(
                    errorMessageCode = validateLitros(uiState.formState.litros.value)
                ),
                tipo = uiState.formState.tipo.copy(
                    errorMessageCode = validateTipo(uiState.formState.tipo.value)
                )
            )
        )
        return uiState.formState.isValid
    }
}