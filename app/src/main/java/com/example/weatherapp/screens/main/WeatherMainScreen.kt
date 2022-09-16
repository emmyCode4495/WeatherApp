package com.example.weatherapp.screens.main

import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.navigation.NavController
import com.example.weatherapp.data.DataOrException
import com.example.weatherapp.model.Weather


@Composable
fun WeatherMainScreen(navController: NavController,
                      mainViewModel: MainViewModel) {
    


    val weatherData = produceState<DataOrException<Weather, Boolean,Exception>>(
        initialValue = DataOrException(loading = true)){
        value = mainViewModel.getWeatherData(city = "Moscow")
    }.value

    if(weatherData.loading == true){
        CircularProgressIndicator()
    }else if(weatherData.data !=null){
        MainScaffold(weather = weatherData.data!!)
    }
}

@Composable
fun MainScaffold(weather: Weather) {

}