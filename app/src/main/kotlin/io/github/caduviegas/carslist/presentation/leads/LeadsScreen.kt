package io.github.caduviegas.carslist.presentation.leads

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.github.caduviegas.carslist.R
import io.github.caduviegas.carslist.domain.util.CarColor
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeadsScreen(navController: NavController,
                viewModel: LeadsViewModel = koinViewModel()
) {
    //val uiState by viewModel.leadSaveUiState.collectAsState()
    var showError by remember { mutableStateOf(false) }
    var errorMessage  = "Falha ao salvar intenção de compra"

//    LaunchedEffect(uiState) {
//        showError = uiState == LeadSaveUiState.Error
//    }

    LaunchedEffect(Unit) {
        //resgatar Car que veio da tela anterior por aqui e passar para ViewModel
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = io.github.caduviegas.carslist.R.drawable.ic_car),
                            contentDescription = null,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(id = io.github.caduviegas.carslist.R.string.app_name),
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = "Voltar"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CarColor.Primary
                )
            )
        },
        snackbarHost = {
            if (showError) {
                Snackbar(
                    modifier = Modifier.padding(16.dp),
                    containerColor = CarColor.Error,
                    contentColor = CarColor.OnError
                ) { Text(errorMessage) }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {

        }
    }

    BackHandler {
        navController.popBackStack()
    }
}