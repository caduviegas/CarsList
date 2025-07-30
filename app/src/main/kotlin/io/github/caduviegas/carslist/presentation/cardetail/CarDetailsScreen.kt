package io.github.caduviegas.carslist.presentation.cardetail

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarDetailsScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Detalhe do Carro") })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text("Tela de Detalhes de Carro")
        }
    }

    BackHandler {
        navController.popBackStack()
    }
}