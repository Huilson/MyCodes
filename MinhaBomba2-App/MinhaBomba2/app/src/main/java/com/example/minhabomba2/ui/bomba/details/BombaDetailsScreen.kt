package com.example.minhabomba2.ui.bomba.details

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.minhabomba2.R
import com.example.minhabomba2.data.abastecimento.Abastecimento
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.minhabomba2.ui.utils.composables.ErrorState
import com.example.minhabomba2.ui.utils.composables.LoadingState

@Composable
fun BombaDetailsScreen(
    modifier: Modifier = Modifier,
    viewModel: BombaDetailsViewModel = viewModel(),
    onBackPressed: () -> Unit,
    onItemDeleted:() -> Unit,
    onEditPressed: () -> Unit,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    LaunchedEffect(viewModel.uiState.abastecerDeleted) {
        if (viewModel.uiState.abastecerDeleted){
            onItemDeleted()
        }
    }

    val context = LocalContext.current
    LaunchedEffect(snackbarHostState, viewModel.uiState.hasErrorDeleting) {
        if (viewModel.uiState.hasErrorDeleting){
            snackbarHostState.showSnackbar(context.getString(R.string.error_deleting))
        }
    }

    if (viewModel.uiState.showConfirmDialog){
        ConfirmationDialog(
            title = "ATENÇÃO!",
            text = "Deseja realmente excluir esse registro?",
            onDismiss = viewModel::dismissConfirmationDialog,
            onConfirm = viewModel::delete,
            dismissButtonText = "Não",
            confirmButtonText = "Sim"
        )
    }
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState)},
        modifier = modifier.fillMaxSize(),
        topBar = {
            DetailsTopBar(
                onBackPressed = onBackPressed,
                onEditPressed = onEditPressed,
                onDeletePressed = viewModel::showConfirmationDialog,
                showAction = viewModel.uiState.isSuccessLoading,
                isProcessing = viewModel.uiState.isDeleting
            )
        })
    { padValues ->
        if (viewModel.uiState.isLoading) {
            LoadingState(modifier = Modifier.padding(padValues))
        } else if (viewModel.uiState.hasErrorLoading) {
            ErrorState(modifier = Modifier.padding(padValues), onTryAgain = viewModel::loadBomba)
        } else {
            AbastecerDetails(
                abastecimento = viewModel.uiState.abastecer,
                modifier = modifier.padding(padValues)
            )
        }
    }
}

@Composable
private fun AbastecerDetails(
    modifier: Modifier = Modifier,
    abastecimento: Abastecimento
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        BombaTitle(text = "Id: ${abastecimento.id}")
        BombaAttribute(name = "Tipo: ", value = abastecimento.combustivel?.tipo ?: "N/A")
        BombaAttribute(name = "Preço por Litro: ", value = "${abastecimento.combustivel?.valor}")
        BombaAttribute(name = "Litros abastecidos: ", value = "${abastecimento.litros}")
        HorizontalDivider(modifier = Modifier.padding(top = 8.dp))
        BombaAttribute(name = "Total pago: ", value = "${abastecimento.total}")
    }
}

@Composable
private fun BombaTitle(modifier: Modifier = Modifier, text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.tertiary,
        modifier = modifier.padding(all = 16.dp)
    )
}

@Composable
private fun BombaAttribute(modifier: Modifier = Modifier, name: String, value: String) {
    Column(modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Text(
            text = name,
            style = MaterialTheme.typography.labelLarge.copy(
                //fontStyle = FontStyle.Italic
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.tertiary
        )
        val textStyle: TextStyle
        val text: String
        if (value.isNotEmpty()) {
            text = value
            textStyle = MaterialTheme.typography.bodySmall
        } else {
            text = "N/A"
            textStyle = MaterialTheme.typography.bodySmall
        }
        Text(
            text = text,
            style = textStyle,
            color = MaterialTheme.colorScheme.tertiary
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DetailsTopBar(
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit,
    onEditPressed: () -> Unit,
    onDeletePressed: () -> Unit,
    showAction: Boolean,
    isProcessing: Boolean
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            titleContentColor = MaterialTheme.colorScheme.tertiary
        ),
        title = { Text(text = stringResource(R.string.details_text)) },
        navigationIcon = {
            IconButton(onClick = onBackPressed) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back)
                )
            }
        }, actions = {
            if (showAction) {
                if (isProcessing) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(60.dp)
                            .padding(16.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    IconButton(onClick = onEditPressed) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = stringResource(R.string.edit)
                        )
                    }
                    IconButton(onClick = onDeletePressed) {
                        Log.i("Dialog","Entro passo 1")
                        Icon(
                            imageVector = Icons.Filled.DeleteForever,
                            contentDescription = stringResource(R.string.delete)
                        )
                    }
                }//fim ELSE(Processing)
            }//fim IF(Show Action)
        }, modifier = modifier.fillMaxWidth()
    )
}

@Composable
private fun ConfirmationDialog(
    modifier: Modifier = Modifier,
    title: String,
    text: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    dismissButtonText: String,
    confirmButtonText: String
) {
    AlertDialog(
        modifier = modifier,
        title = { Text(title) },
        text = { Text(text) },
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(confirmButtonText)
            }//fim TextButtom
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(dismissButtonText)
            }//fim TextButtom
        })
    Log.i("Dialog","Entro passo 2")
}