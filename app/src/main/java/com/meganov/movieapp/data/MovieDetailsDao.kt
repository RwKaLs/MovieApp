package com.meganov.movieapp.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Dao
interface MovieDetailsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movie: Movie)

    @Query("SELECT * FROM favouritemovies")
    fun getAll(): LiveData<List<Movie>>

    @Query("SELECT * FROM favouritemovies WHERE kinopoiskId = :id")
    suspend fun getById(id: String): Movie?

    @Delete
    suspend fun delete(movie: Movie)
}


@Database(entities = [Movie::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDetailsDao(): MovieDetailsDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromCountryList(countries: List<Country>): String {
        return gson.toJson(countries)
    }

    @TypeConverter
    fun toCountryList(countriesString: String): List<Country> {
        val type = object : TypeToken<List<Country>>() {}.type
        return gson.fromJson(countriesString, type)
    }

    @TypeConverter
    fun fromGenreList(genres: List<Genre>): String {
        return gson.toJson(genres)
    }

    @TypeConverter
    fun toGenreList(genresString: String): List<Genre> {
        val type = object : TypeToken<List<Genre>>() {}.type
        return gson.fromJson(genresString, type)
    }
}
