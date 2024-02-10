package ru.tinkoff.edu.meganov.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.tinkoff.edu.meganov.data.network.MovieService

class MovieRepo {
    suspend fun getTopMovies(page: Int, pageSize: Int): List<Movie> {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://kinopoiskapiunofficial.tech")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(MovieService::class.java)
        val response = service.getTopMovies(page, pageSize)
        return response.items
    }
}