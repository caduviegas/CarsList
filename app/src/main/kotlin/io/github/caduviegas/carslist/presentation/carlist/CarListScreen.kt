package io.github.caduviegas.carslist.presentation.carlist

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import io.github.caduviegas.carslist.R
import io.github.caduviegas.carslist.domain.model.Car
import io.github.caduviegas.carslist.domain.util.CarColor
import io.github.caduviegas.carslist.presentation.CarsDestinations
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarListScreen(
    navController: NavController,
    viewModel: CarListViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val isUserLoggedIn by viewModel.isUserLoggedIn.collectAsState()
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(uiState) {
        if (uiState is CarListUiState.Error) {
            showError = true
            errorMessage = (uiState as CarListUiState.Error).message
        } else {
            showError = false
        }
    }

    LaunchedEffect(Unit) {
        viewModel.fetchCars()
        viewModel.checkUserLoggedIn()
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
                    if (isUserLoggedIn) {
                        IconButton(onClick = { navController.navigate(CarsDestinations.LEADS) }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_cart),
                                contentDescription = "Leads"
                            )
                        }
                        IconButton(onClick = { navController.navigate(CarsDestinations.LOGOUT) }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_person),
                                contentDescription = "Logout"
                            )
                        }
                    } else {
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
                ) { Text(errorMessage) }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            when (uiState) {
                is CarListUiState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Transparent),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = CarColor.Primary)
                    }
                }
                is CarListUiState.Success -> {
                    val cars = (uiState as CarListUiState.Success).cars
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(CarColor.Background),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(cars) { car ->
                            CarCard(
                                car = car,
                                onClick = {
                                    navController.currentBackStackEntry?.savedStateHandle?.set("car", car)
                                    navController.navigate(CarsDestinations.CAR_DETAILS)
                                }
                            )
                        }
                    }
                }
                is CarListUiState.Error -> { }
            }
        }
    }

    BackHandler {
        navController.popBackStack()
    }
}

@Composable
fun CarCard(
    car: Car,
    onClick: () -> Unit
) {
    val cardHeight = 220.dp
    val imageHeight = cardHeight * 0.6f

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(cardHeight)
            .clickable { onClick() },
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(imageHeight)
                    .background(CarColor.SecondaryLight),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.bg_car),
                    contentDescription = "Foto do carro",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.align(Alignment.TopStart)
                ) {
                    Text(
                        text = car.nomeModelo,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = CarColor.OnSurface
                    )
                    Text(
                        text = car.ano.toString(),
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp,
                        color = CarColor.OnSurface
                    )
                }
                Text(
                    text = "R$ ${"%,.2f".format(car.valor)}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = CarColor.OnSurface,
                    modifier = Modifier.align(Alignment.BottomEnd)
                )
            }
        }
    }
}
