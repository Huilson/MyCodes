package com.example.minhabomba2.ui.utils.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.minhabomba2.R

@Composable
fun ErrorState(modifier: Modifier = Modifier, onTryAgain: () -> Unit) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Filled.CloudOff,
            contentDescription = stringResource(R.string.error_icon),
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier.size(70.dp)
        )
        Text(
            modifier = Modifier.padding(top = 2.dp, start = 2.dp, end = 2.dp),
            text = stringResource(R.string.error_loading),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )
        ElevatedButton(
            onClick = onTryAgain,
            modifier = Modifier.padding(top = 4.dp)
        ) {
            Text(text = stringResource(R.string.try_again))
        }
    }
}
