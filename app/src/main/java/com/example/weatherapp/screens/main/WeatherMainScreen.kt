package com.example.weatherapp.screens.main

import android.inputmethodservice.Keyboard
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.weatherapp.R
import com.example.weatherapp.data.DataOrException
import com.example.weatherapp.model.Weather
import com.example.weatherapp.model.WeatherItem
import com.example.weatherapp.navigation.WeatherScreens
import com.example.weatherapp.utils.formatDate
import com.example.weatherapp.utils.formatDateTime
import com.example.weatherapp.utils.formatDecimals
import com.example.weatherapp.widgets.*
import okhttp3.internal.format


@Composable
fun WeatherMainScreen(
    navController: NavController,
    mainViewModel: MainViewModel,
    city: Any
) {

    val weatherData = produceState<DataOrException<Weather, Boolean,Exception>>(
        initialValue = DataOrException(loading = true)){
        value = mainViewModel.getWeatherData(city = city.toString())
    }.value

    if(weatherData.loading == true){
        CircularProgressIndicator()
    }else if(weatherData.data !=null){
        MainScaffold(weather = weatherData.data!!, navController)
    }
}

@Composable
fun MainScaffold(weather: Weather,
                 navController: NavController) {
    
    Scaffold(topBar = {
        WeatherAppBar(title = weather.city.name + ", ${weather.city.country}",
            navController = navController,
            onAddActionClicked = {
                navController.navigate(WeatherScreens.SearchScreen.name)
            },
        elevation = 5.dp){
            Log.d("TAG","MainScaffold: Button Clicked")
        }
    }) {
        MainContent(data = weather)
    }
}

@Composable
fun MainContent(data: Weather) {

    val weatherItem = data.list[0]
    val imageUrl  = "https://openweathermap.org/img/wn/${weatherItem.weather[0].icon}.png"
    Column(modifier = Modifier
        .padding(4.dp)
        .fillMaxWidth(),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally){

        //for the data Preview
        Text(text = formatDate(weatherItem.dt),
        style = MaterialTheme.typography.caption,
        color = MaterialTheme.colors.onSecondary,

        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(6.dp))

    Surface(modifier = Modifier
        .padding(4.dp)
        .size(200.dp),
    shape = CircleShape,
    color = Color(0xFFFFc400)) {
        Column(verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
            WeatherStateImage(imageUrl = imageUrl)

            //getting the temperature for the day
            Text(text = formatDecimals(weatherItem.temp.day) + "º",
            style = MaterialTheme.typography.h4,
            fontWeight = FontWeight.ExtraBold)
            Text(text = weatherItem.weather[0].main, fontStyle = FontStyle.Italic)

        }

    }
        HumidityWindPressureRow(weather = weatherItem)
        Divider()
        TimeDisplayForCitiesSunsetSunrise(weather = weatherItem)
        WeeklyText()
        Divider()
        FirstDayWeatherReport(imageUrl = imageUrl, data)
}

}

