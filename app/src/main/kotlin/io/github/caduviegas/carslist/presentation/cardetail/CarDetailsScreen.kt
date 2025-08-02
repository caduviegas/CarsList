package io.github.caduviegas.carslist.presentation.cardetail

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import io.github.caduviegas.carslist.R
import io.github.caduviegas.carslist.domain.model.Car
import io.github.caduviegas.carslist.domain.util.CarColor
import io.github.caduviegas.carslist.presentation.CarsDestinations
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarDetailsScreen(
    navController: NavController,
    viewModel: CarDetailsViewModel = koinViewModel()
) {
    val car by viewModel.car.collectAsState()
    val uiState by viewModel.leadSaveUiState.collectAsState()
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val errorDuration = 3000L
    var errorProgress by remember { mutableFloatStateOf(1f) }

    LaunchedEffect(Unit) {
        val carNav = navController.previousBackStackEntry
            ?.savedStateHandle
            ?.get<Car>("car")
        carNav?.let { viewModel.setCar(it) }
    }

    LaunchedEffect(uiState) {
        showError = uiState in listOf(LeadSaveUiState.Error, LeadSaveUiState.NoUser)
        errorMessage = when (uiState) {
            LeadSaveUiState.Error -> "Ocorreu um erro ao salvar o lead. Tente novamente."
            LeadSaveUiState.NoUser -> "Primeiro crie uma conta antes de comprar carro"
            else -> ""
        }
    }

    LaunchedEffect(showError) {
        if (showError) {
            errorProgress = 1f
            val steps = 30
            val stepDuration = errorDuration / steps
            repeat(steps) {
                errorProgress = 1f - (it + 1) / steps.toFloat()
                delay(stepDuration)
            }
            showError = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_car),
                            contentDescription = null,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(id = R.string.app_name),
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
                actions = {
                    if (uiState == LeadSaveUiState.NoUser) {
                        TextButton(onClick = { navController.navigate(CarsDestinations.LOGIN) }) {
                            Text(
                                text = "ENTRAR",
                                color = CarColor.OnPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }
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
                ) {
                    Column {
                        Text(errorMessage)
                        LinearProgressIndicator(
                            progress = { errorProgress },
                            color = CarColor.OnError,
                            trackColor = CarColor.Error,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(10.dp)
                                .padding(top = 8.dp)
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        car?.let { car ->
            val formatter = remember { DateTimeFormatter.ofPattern("dd/MM/yyyy") }
            val cadastroFormatado = car.cadastro.format(formatter)

            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .background(CarColor.Background)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.bg_car),
                    contentDescription = "Foto do carro",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .background(CarColor.SecondaryLight)
                        .clip(RectangleShape)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = car.nomeModelo,
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp,
                    color = CarColor.OnSurface,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )

                Text(
                    text = "R$ ${"%,.2f".format(car.valor)}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = CarColor.OnSurface,
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                )

                CarInfoRow(label = "Ano", value = car.ano.toString())
                HorizontalDivider(modifier = Modifier.padding(horizontal = 24.dp))
                CarInfoRow(label = "Cor", value = car.cor)
                HorizontalDivider(modifier = Modifier.padding(horizontal = 24.dp))
                CarInfoRow(label = "Combustível", value = car.fuelType.toString())
                HorizontalDivider(modifier = Modifier.padding(horizontal = 24.dp))
                CarInfoRow(label = "Nº de portas", value = car.numPortas.toString())
                HorizontalDivider(modifier = Modifier.padding(horizontal = 24.dp))
                CarInfoRow(label = "Data do anúncio", value = cadastroFormatado)

                Spacer(modifier = Modifier.height(16.dp))

                data class ButtonState(
                    val text: String,
                    val color: Color,
                    val enabled: Boolean,
                    val showLoading: Boolean
                )

                val buttonState = when (uiState) {
                    LeadSaveUiState.NoLead,
                    LeadSaveUiState.Error,
                    LeadSaveUiState.NoUser ->
                        ButtonState("Comprar carro", CarColor.Primary, true, false)
                    LeadSaveUiState.AlreadyHasLead,
                    LeadSaveUiState.Success ->
                        ButtonState("Compra solicitada", CarColor.Secondary, false, false)
                    LeadSaveUiState.Loading ->
                        ButtonState("Comprar carro", CarColor.Primary, false, true)
                }
                Button(
                    onClick = { viewModel.saveLeadForCurrentCar() },
                    enabled = buttonState.enabled,
                    colors = ButtonDefaults.buttonColors(containerColor = buttonState.color),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .height(56.dp)
                ) {
                    if (buttonState.showLoading) {
                        CircularProgressIndicator(
                            color = CarColor.OnPrimary,
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = buttonState.text,
                            fontWeight = FontWeight.Bold,
                            color = CarColor.OnPrimary,
                            fontSize = 18.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }

    BackHandler {
        navController.popBackStack()
    }
}

@Composable
fun CarInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            color = CarColor.OnSurface,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            textAlign = TextAlign.End,
            color = CarColor.OnSurface,
            modifier = Modifier.weight(1f),
            maxLines = 1
        )
    }
}