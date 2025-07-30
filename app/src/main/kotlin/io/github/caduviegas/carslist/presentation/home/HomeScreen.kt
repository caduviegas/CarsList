package io.github.caduviegas.carslist.presentation.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.github.caduviegas.carslist.presentation.CarsDestinations
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = koinViewModel()
) {
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.validateUser()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Button(
                onClick = { navController.navigate(CarsDestinations.LOGIN) },
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) {
                Text("Ir para Cadastro")
            }
            Button(
                onClick = { navController.navigate(CarsDestinations.CAR_LIST) },
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) {
                Text("Ir para Lista de Carros")
            }
            Spacer(modifier = Modifier.height(32.dp))
            Text("Usuário logado: ${isLoggedIn ?: "Verificando..."}")
        }
    }
}