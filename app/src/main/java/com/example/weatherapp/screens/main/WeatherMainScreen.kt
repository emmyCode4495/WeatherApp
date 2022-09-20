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
import com.example.weatherapp.utils.formatDate
import com.example.weatherapp.utils.formatDateTime
import com.example.weatherapp.utils.formatDecimals
import com.example.weatherapp.widgets.WeatherAppBar
import okhttp3.internal.format


@Composable
fun WeatherMainScreen(navController: NavController,
                      mainViewModel: MainViewModel) {
    


    val weatherData = produceState<DataOrException<Weather, Boolean,Exception>>(
        initialValue = DataOrException(loading = true)){
        value = mainViewModel.getWeatherData(city = "lagos")
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

@Composable
fun FirstDayWeatherReport(imageUrl:String,data: Weather) {
    Surface(modifier = Modifier
        .padding(top = 5.dp, bottom = 7.dp)
        .fillMaxWidth()
        .fillMaxHeight(),
    shape = RoundedCornerShape(size = 14.dp),
    color = Color(0xFFEEF1EF)){
        LazyColumn(modifier = Modifier.padding(2.dp),
        contentPadding = PaddingValues(1.dp)){
            items(items = data.list){ item: WeatherItem ->
                WeatherDetailRow(weather = item, data)

            }
        }

    }
}

@Composable
fun WeatherDetailRow(weather: WeatherItem,data: Weather) {
    val weatherItem = data.list[0]
    val imageUrl  = "https://openweathermap.org/img/wn/${weatherItem.weather[0].icon}.png"
    Surface(modifier = Modifier
        .padding(3.dp)
        .fillMaxWidth(),
    shape = CircleShape.copy(topEnd = CornerSize(6.dp)),
    color = Color.White) {
        Row(modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween) {
            Text(formatDate(weather.dt)
                .split(",")[0],
            modifier = Modifier.padding(start = 5.dp))

            WeatherStateImage(imageUrl = imageUrl)
            Surface(modifier = Modifier.padding(0.dp),
            shape = CircleShape,
            color = Color(0xFFFFC400)) {
                Text(weather.weather[0].description,
                modifier = Modifier.padding(4.dp),
                style = MaterialTheme.typography.caption)
            }
            Text(text = buildAnnotatedString {
                withStyle(style = SpanStyle(
                    color = Color.Blue.copy(alpha = 0.7f),
                    fontWeight = FontWeight.SemiBold
                )){
                    append(formatDecimals(weather.temp.max) + "º")
                }
                withStyle(style = SpanStyle(
                    color = Color.LightGray,
                    fontWeight = FontWeight.SemiBold
                )){
                    append(formatDecimals(weather.temp.min) + "º")
                }
            })
        }



    }
}

@Composable
fun WeeklyText() {
    Row(verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.Center){
        Text(text = "This Week",
            style = MaterialTheme.typography.h5,
        fontWeight = FontWeight.ExtraBold)
    }

}

@Composable
fun TimeDisplayForCitiesSunsetSunrise(weather: WeatherItem) {
    Row(modifier = Modifier
        .padding(top = 15.dp, bottom = 6.dp)
        .fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween) {
                   Row() {
                Image(painter = painterResource(id = R.drawable.sunrise),
                    contentDescription = "Sunrise Image",
                modifier = Modifier.size(30.dp))
                Text(text = formatDateTime(weather.sunrise),
                style = MaterialTheme.typography.caption)
            }
            Row() {

                Text(text = formatDateTime(weather.sunset),
                style = MaterialTheme.typography.caption)

                Image(painter = painterResource(id = R.drawable.sunset),
                    contentDescription = "Sunset Image",
                    modifier = Modifier.size(30.dp))

            }
            
        }
        
    }

@Composable
fun HumidityWindPressureRow(weather: WeatherItem) {
        Row(modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween){
            Row(modifier = Modifier.padding(4.dp)) {
                Icon(painter = painterResource(id = R.drawable.humidity ),
                    contentDescription = "Humid image representation",
                modifier = Modifier.size(20.dp))
                Text(text = "${weather.humidity}%")
            }
            Row(modifier = Modifier.padding(4.dp)) {
                Icon(painter = painterResource(id = R.drawable.pressure ),
                    contentDescription = "Pressure image representation",
                    modifier = Modifier.size(20.dp))
                Text(text = "${weather.pressure} psi")
            }
            Row(modifier = Modifier.padding(4.dp)) {
                Icon(painter = painterResource(id = R.drawable.wind ),
                    contentDescription = "Wind image representation",
                    modifier = Modifier.size(20.dp))
                Text(text = "${weather.pressure} mph",
                style = MaterialTheme.typography.caption)
            }

            }
        }



@Composable
fun WeatherStateImage(imageUrl: String) {
    Image(painter = rememberImagePainter(imageUrl),
        contentDescription = "Icon Image",
    modifier = Modifier.size(80.dp))
}
