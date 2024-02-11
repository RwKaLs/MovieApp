package ru.tinkoff.edu.meganov.data

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieService {
    @Headers("X-API-KEY: e30ffed0-76ab-4dd6-b41f-4c9da2b2735b")
    @GET("api/v2.2/films/collections")
    suspend fun getTopMovies(@Query("page") page: Int,
                             @Query("pageSize") pageSize: Int,
                             @Query("type") type: String = "TOP_POPULAR_MOVIES"): MovieResponse

    @Headers("X-API-KEY: e30ffed0-76ab-4dd6-b41f-4c9da2b2735b")
    @GET("api/v2.2/films/{id}")
    suspend fun getMovieDetails(@Path("id") id:String): MovieDetailsResponse
}
