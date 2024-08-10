package com.example.minhabomba2.ui.bomba.form

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.minhabomba2.R
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.minhabomba2.data.abastecimento.Combustivel

@Composable
fun BombaFormScreen(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState()},
    onBackPressed: () -> Unit,
    onItemSaved:() -> Unit,
    viewModel: BombaFormViewModel = viewModel()) {
    
    LaunchedEffect(viewModel.uiState.itemSaved) {
        if (viewModel.uiState.itemSaved) {
            onItemSaved()
        }
    }

    val context = LocalContext.current
    LaunchedEffect(snackbarHostState, viewModel.uiState.hasErrorSaving) {
        if (viewModel.uiState.hasErrorSaving) {
            snackbarHostState.showSnackbar(context.getString(R.string.error_fueling))
        }
    }
    
    Scaffold(
        modifier = modifier.fillMaxWidth(),
        topBar = {
            FormTopAppBar(
                isNewItem = viewModel.uiState.isNewItem,
                onBackPressed = onBackPressed,
                onSavePressed = viewModel::save,
                showAction = viewModel.uiState.isSucessLoading,
                isSaving = viewModel.uiState.isSaving)
        }
    ) { padVallues ->
        FormContent(
            modifier = Modifier.padding(padVallues),
            bombaId = viewModel.uiState.bombaId,
            fuels = viewModel.uiState.fuels,
            formState = viewModel.uiState.formState,
            onLitrosChanged = viewModel::onLitrosChanged,
            onTipoChanged = viewModel::onTipoChanged,
            onValorChanged = viewModel::onValorChanged,
            onTotalChanged = viewModel::onTotalChanged)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FormTopAppBar(
    modifier: Modifier = Modifier,
    isNewItem: Boolean,
    showAction: Boolean,
    isSaving: Boolean,
    onBackPressed:()->Unit,
    onSavePressed:()->Unit) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            titleContentColor = MaterialTheme.colorScheme.secondary
        ),
        title = {
            Text(
                if (isNewItem) {
                    stringResource(R.string.new_item)
                } else {
                    stringResource(R.string.edit_item)
                }
            )//fim TEXT
        },
        navigationIcon = {
            IconButton(onClick = onBackPressed) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back))
            }
        }, actions = {
            if (showAction){
                if (isSaving){
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(60.dp)
                            .padding(all = 16.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    IconButton(onClick = onSavePressed) {
                        Icon(imageVector = Icons.Filled.Check,
                            contentDescription = stringResource(R.string.save)
                        )
                    }
                }
            }            
        }
    )//fim TopBar
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FormContent(
    modifier: Modifier = Modifier, 
    bombaId: Int,
    fuels: List<Combustivel>,
    formState: FormState,
    onLitrosChanged:(String)-> Unit,
    onTipoChanged:(String)-> Unit,
    onValorChanged:(String)-> Unit,
    onTotalChanged:(String)-> Unit,
    ) {
    Log.d("fuels","fuels list na screen: $fuels")

    var fuelName = mutableListOf<String>()
    fuels.forEach{
         fuelName.add(it.tipo)
    }
    var fuelPrice = mutableListOf<String>()
    fuels.forEach{
        fuelPrice.add(it.valor.toString())
    }

    var isExpanded by remember {
        mutableStateOf(false)
    }

    var selectedItem by remember {
        mutableStateOf("Escolha um Combustivel")
    }

    var selectedPrice by remember {
        mutableStateOf("0.0")
    }

    Log.i("fuels","Fuels: $fuelName")

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(vertical = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        BombaId(id = bombaId)

        FormTextField(label = "Tipo ",
            value = selectedItem,
            onValueChange = onTipoChanged,
            errorMessageCode = formState.tipo.errorMessageCode,
            keyboardCapitalization = KeyboardCapitalization.Words,
            enabled = false)
        onTipoChanged(selectedItem)

        ExposedDropdownMenuBox(
            modifier = Modifier.padding(start = 16.dp),
            expanded = isExpanded,
            onExpandedChange = {isExpanded = !isExpanded})
        {
            TextField(
                modifier = Modifier.menuAnchor(),
                value = selectedItem,
                onValueChange = {},
                readOnly = true,
                trailingIcon = {ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)}
            )
            ExposedDropdownMenu(expanded = isExpanded, onDismissRequest = { isExpanded =false }) {
                fuelName.forEachIndexed { index, text ->
                    DropdownMenuItem(
                        text = { Text(text = text) },
                        onClick = {
                            selectedItem = fuelName[index]
                            selectedPrice = fuelPrice[index]
                            isExpanded = false},
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding)
                }
            }
        }//fim DropDown*/

        FormTextField(label = "Quantidade de Litros ",
            value = formState.litros.value,
            onValueChange = onLitrosChanged,
            errorMessageCode = formState.litros.errorMessageCode,
            keyboardType = KeyboardType.Decimal,
            keyboardActions = ImeAction.Done)

        FormTextField(label = "Preço do Litro ",
            value = selectedPrice,
            onValueChange = onValorChanged,
            errorMessageCode = formState.valor.errorMessageCode,
            keyboardType = KeyboardType.Decimal,
            enabled = false)
        onValorChanged(selectedPrice)

        FormTextField(label = "Total Pago ",
            value = formState.total.value,
            onValueChange = onTotalChanged,
            errorMessageCode = null,
            keyboardType = KeyboardType.Decimal,
            enabled = false)

        Log.i("log", "Valor: ${formState.tipo.value}")
    }
}

@Composable
private fun BombaId(modifier: Modifier = Modifier, id: Int) {
    Log.i("id", "Valor do id: $id")
    if(id>0){
        FormTitle(text = "Id: $id")
    } else {
        Row (
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically
        ){
            FormTitle(text = "Id: ")
            Text(text = "...",
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.titleSmall)
        }
    }
}

@Composable
private fun FormTitle(modifier: Modifier = Modifier, text: String) {
    Text(
        modifier = modifier.padding(start = 16.dp),
        text = text,
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.secondary)
}

@Composable
private fun FormTextField(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    onValueChange:(String) -> Unit,
    enabled: Boolean = true,
    @StringRes
    errorMessageCode: Int? = null,
    keyboardCapitalization: KeyboardCapitalization = KeyboardCapitalization.Sentences,
    keyboardActions: ImeAction = ImeAction.Next,
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (()-> Unit)?=null)
{
    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label)},
        maxLines = 1,
        enabled = enabled,
        isError = errorMessageCode != null,
        trailingIcon = trailingIcon,
        keyboardOptions = KeyboardOptions(
            capitalization = keyboardCapitalization,
            imeAction = keyboardActions,
            keyboardType = keyboardType),
        visualTransformation = visualTransformation
        )
    errorMessageCode?.let { Text(text = stringResource(errorMessageCode),
        color = MaterialTheme.colorScheme.error,
        style = MaterialTheme.typography.labelSmall,
        modifier = Modifier.padding(horizontal = 16.dp))}
}