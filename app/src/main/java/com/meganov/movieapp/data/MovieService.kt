package com.meganov.movieapp.data

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieService {
    @Headers("X-API-KEY: 39acb823-e916-427e-ace3-18f1e5380818")
    @GET("api/v2.2/films/collections")
    suspend fun getTopMovies(
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int,
        @Query("type") type: String = "TOP_POPULAR_MOVIES"
    ): MovieResponse

    @Headers("X-API-KEY: 39acb823-e916-427e-ace3-18f1e5380818")
    @GET("api/v2.2/films/{id}")
    suspend fun getMovieDetails(@Path("id") id: String): MovieDetailsResponse
}
