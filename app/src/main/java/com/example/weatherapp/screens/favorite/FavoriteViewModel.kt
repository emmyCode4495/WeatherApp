package com.example.weatherapp.screens.favorite

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.model.Favorites
import com.example.weatherapp.repository.WeatherDbRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FavoriteViewModel @Inject constructor(private val repository: WeatherDbRepository)
    : ViewModel(){
        private val _favList = MutableStateFlow<List<Favorites>>(emptyList())
            val favList = _favList.asStateFlow()

    init{
        viewModelScope.launch(Dispatchers.IO){
            repository.getFavorites().distinctUntilChanged()
                .collect { listOfFavs ->
                    if(listOfFavs.isNullOrEmpty()){
                        Log.d("TAG",":Empty favs")
                    }else{
                        _favList.value = listOfFavs
                        Log.d("FAVS",":${favList.value}")
                    }

                }
        }
    }

    fun insertFavorite(favorite: Favorites) =
        viewModelScope.launch { repository.insertFavorites(favorite)}
    fun updateFavorite(favorite: Favorites) =
        viewModelScope.launch { repository.updateFavorites(favorite) }
    fun deleteFavorite(favorite: Favorites) =
        viewModelScope.launch { repository.deleteFavorites(favorite) }
}