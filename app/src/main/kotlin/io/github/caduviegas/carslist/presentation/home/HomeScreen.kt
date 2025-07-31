package io.github.caduviegas.carslist.presentation.home

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import io.github.caduviegas.carslist.presentation.CarsDestinations
import org.koin.androidx.compose.koinViewModel
import io.github.caduviegas.carslist.domain.util.then
import io.github.caduviegas.carslist.R
import io.github.caduviegas.carslist.domain.util.CarColor
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = koinViewModel()
) {
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()
    val colorOnPrimary = CarColor.Surface
    val colorPrimary = CarColor.Primary
    val colorSecondary = CarColor.Secondary

    var showButtons by remember { mutableStateOf(false) }
    var animateCar by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.validateUser()
    }

    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn == true) {
            navController.navigate(CarsDestinations.CAR_LIST) {
                popUpTo(CarsDestinations.HOME) { inclusive = true }
            }
        } else if (isLoggedIn == false) {
            animateCar = true
            delay(500)
            showButtons = true
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorOnPrimary),
        contentAlignment = Alignment.Center
    ) {
        val transition = updateTransition(targetState = animateCar, label = "carAnim")
        val carOffsetY by transition.animateDp(
            transitionSpec = { tween(durationMillis = 500) },
            label = "carOffsetY"
        ) { anim ->
            if (anim) (-120).dp else 0.dp
        }
        val carScale by transition.animateFloat(
            transitionSpec = { tween(durationMillis = 500) },
            label = "carScale"
        ) { anim ->
            if (anim) 0.6f else 1f
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.height(80.dp))
            Box(
                modifier = Modifier
                    .offset(y = carOffsetY)
                    .size(160.dp)
                    .graphicsLayer {
                        scaleX = carScale
                        scaleY = carScale
                    },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_car),
                    contentDescription = "Carro",
                    modifier = Modifier.fillMaxSize()
                )
            }
            Spacer(modifier = Modifier.height(animateCar.then(32.dp, 80.dp)))
            AnimatedVisibility(
                visible = showButtons,
                enter = fadeIn(animationSpec = tween(500)),
                exit = fadeOut()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp)
                ) {
                    Button(
                        onClick = { navController.navigate(CarsDestinations.LOGIN) },
                        colors = ButtonDefaults.buttonColors(containerColor = colorPrimary),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Text("Fazer login")
                    }
                    Button(
                        onClick = { navController.navigate(CarsDestinations.CAR_LIST) },
                        colors = ButtonDefaults.buttonColors(containerColor = colorSecondary),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Text("Entrar sem login")
                    }
                }
            }
        }
    }
}