package io.github.caduviegas.carslist.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import io.github.caduviegas.carslist.presentation.cardetail.CarDetailsScreen
import io.github.caduviegas.carslist.presentation.carlist.CarListScreen
import io.github.caduviegas.carslist.presentation.home.HomeScreen
import io.github.caduviegas.carslist.presentation.leads.LeadsScreen
import io.github.caduviegas.carslist.presentation.login.LoginScreen
import io.github.caduviegas.carslist.presentation.logout.LogoutScreen

object CarsDestinations {
    const val HOME = "home"
    const val LOGIN = "login"
    const val CAR_LIST = "cars_list"
    const val LEADS = "leads"
    const val CAR_DETAILS = "car_details"
    const val LOGOUT = "logout"
}

@Composable
fun CarsNavHost(navController: NavHostController) {
    NavHost(navController, startDestination = CarsDestinations.HOME) {
        composable(CarsDestinations.HOME) {
            HomeScreen(navController)
        }
        composable(CarsDestinations.LOGIN) {
            LoginScreen(navController)
        }
        composable(CarsDestinations.CAR_LIST) {
            CarListScreen(navController)
        }
        composable(CarsDestinations.LEADS) {
            LeadsScreen(navController)
        }
        composable(CarsDestinations.CAR_DETAILS) {
            CarDetailsScreen(navController)
        }
        composable(CarsDestinations.LOGOUT) {
            LogoutScreen(navController)
        }
    }
}