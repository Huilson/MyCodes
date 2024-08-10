package com.example.minhabomba2.ui.bomba.list

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.minhabomba2.R
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.minhabomba2.data.abastecimento.Abastecimento
import com.example.minhabomba2.ui.utils.composables.ErrorState
import com.example.minhabomba2.ui.utils.composables.LoadingState

@Composable
fun BombaListScreen(
    modifier: Modifier = Modifier,
    viewModel: BombaListViewModel = viewModel(),
    onItemPressed:(Abastecimento) -> Unit,
    onAddPressed:() -> Unit
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),

        topBar = {
            topBarBomba(onRefresh = viewModel::load, showRefresh = viewModel.uiState.success)
        },
        floatingActionButton = {
            if (viewModel.uiState.success) {
                FloatingActionButton(onClick = onAddPressed) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(R.string.add)
                    )
                }
            }
        }
    ) { padValues->
        if (viewModel.uiState.loading) {
            LoadingState(modifier = Modifier.padding(padValues))
        } else if (viewModel.uiState.hasError) {
            ErrorState(
                modifier = Modifier.padding(padValues),
                onTryAgain = viewModel::load
            )
        } else {
            ListingState(
                modifier = Modifier.padding(padValues),
                abastecimento = viewModel.uiState.abastecer,
                onItemPressed = onItemPressed
            )
        }
    }
}

@Composable
private fun FilledList(
    modifier: Modifier = Modifier,
    abastecer: List<Abastecimento>,
    onItemPressed:(Abastecimento) -> Unit) {
    LazyColumn(
        modifier = modifier.padding(vertical = 2.dp)
    ) {
        items(abastecer) {
            ListItem(
                modifier = Modifier.padding(2.dp)
                    .clickable { onItemPressed(it) },
                headlineContent = {
                    Text(
                        text = "ID: ${it.id} - TOTAL: ${it.total}",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                },
                supportingContent = {
                    Text(
                        text = " Litros: ${it.litros}" +
                                "\n Valor: ${it.combustivel?.valor} " +
                                "\n Tipo: ${it.combustivel?.tipo}",
                        color = MaterialTheme.colorScheme.secondary
                    )
                },
                trailingContent = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = stringResource(R.string.select),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            )
            HorizontalDivider(
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

@Composable
private fun EmptyList(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Nenhum abastecimento encotrado",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            modifier = Modifier.padding(top = 2.dp),
            text = "Faça seu primeiro abastecimento precionando \"+\"",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun ListingState(
    modifier: Modifier = Modifier,
    abastecimento: List<Abastecimento> = listOf(),
    onItemPressed: (Abastecimento) -> Unit
) {
    if (abastecimento.isEmpty()) {
        EmptyList(modifier = modifier)
    } else {
        FilledList(modifier = modifier, abastecer = abastecimento, onItemPressed = onItemPressed)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun topBarBomba(
    modifier: Modifier = Modifier,
    onRefresh: () -> Unit,
    showRefresh: Boolean
) {
    TopAppBar(
        modifier = modifier,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary
        ),
        title = { Text(text = stringResource(R.string.titleApp)) },
        actions = {
            if (showRefresh) {
                IconButton(onClick = onRefresh) {
                    Icon(
                        imageVector = Icons.Filled.Refresh,
                        contentDescription = stringResource(R.string.refresh)
                    )
                }
            }
        })
}