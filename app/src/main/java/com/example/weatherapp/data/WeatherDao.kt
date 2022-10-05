package com.example.weatherapp.data

import androidx.room.*
import com.example.weatherapp.model.Favorites
import kotlinx.coroutines.flow.Flow


@Dao
interface WeatherDao {
    @Query("SELECT * from fav_tbl")
    fun getFavorites(): Flow<List<Favorites>>

    @Query("SELECT * from fav_tbl where city =:city")
    suspend fun getFavById(city: String): Favorites

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorites(favorite: Favorites)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateFavorites(favorite: Favorites)

    @Query("DELETE from fav_tbl")
    suspend fun deleteAllFavorites()

    @Delete
    suspend fun deleteFavorites(favorite: Favorites)


}