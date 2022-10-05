package com.example.weatherapp.repository

import com.example.weatherapp.data.WeatherDao
import com.example.weatherapp.model.Favorites
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class WeatherDbRepository @Inject constructor(private val weatherDao: WeatherDao){

    //In here, we mirror the DAO
    fun getFavorites(): Flow<List<Favorites>> = weatherDao.getFavorites()
    suspend fun insertFavorites(favorite: Favorites) = weatherDao.insertFavorites(favorite)
    suspend fun updateFavorites(favorite: Favorites) = weatherDao.updateFavorites(favorite)
    suspend fun deleteAllFavorites() = weatherDao.deleteAllFavorites()
    suspend fun deleteFavorites(favorite: Favorites) = weatherDao.deleteFavorites(favorite)
    suspend fun getFavById(city : String): Favorites = weatherDao.getFavById(city)

}