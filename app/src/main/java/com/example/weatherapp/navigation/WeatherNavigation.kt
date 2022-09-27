package com.example.weatherapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.weatherapp.screens.about.AboutScreen
import com.example.weatherapp.screens.favorite.FavoriteScreen
import com.example.weatherapp.screens.main.MainViewModel
import com.example.weatherapp.screens.main.WeatherMainScreen
import com.example.weatherapp.screens.settings.SettingsScreen
import com.example.weatherapp.screens.splash.WeatherSplashScreen
import com.example.weatherapp.search.SearchScreen

@ExperimentalComposeUiApi
@Composable
fun WeatherNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = WeatherScreens.SplashScreen.name){
     composable(WeatherScreens.SplashScreen.name){
         WeatherSplashScreen(navController = navController)
     }
        val route = WeatherScreens.MainScreen.name
        composable("$route/{city}",
        arguments = listOf(
            navArgument(name = "city"){
                type = NavType.StringType
            }
        )){navBack ->
            navBack.arguments?.getString("city").let{ city ->

                val mainViewModel = hiltViewModel<MainViewModel>()
                if (city != null) {
                    WeatherMainScreen(navController = navController, mainViewModel,
                        city = city)
                }
            }

        }
        composable(WeatherScreens.SearchScreen.name){
            SearchScreen(navController = navController)
        }
        composable(WeatherScreens.AboutScreen.name){
            AboutScreen(navController = navController)
        }
        composable(WeatherScreens.FavouriteScreen.name){
            FavoriteScreen(navController = navController)
        }
        composable(WeatherScreens.SettingsScreen.name){
            SettingsScreen(navController = navController)
        }
    }

}