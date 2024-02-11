package ru.tinkoff.edu.meganov.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

data class MovieResponse(
    val total: Int,
    val totalPages: Int,
    val items: List<Movie>
)

data class MovieDetailsResponse(
    @PrimaryKey val kinopoiskId: Int,
    val imdbId: String,
    val nameRu: String,
    val nameOriginal: String,
    val posterUrl: String,
    val posterUrlPreview: String,
    val reviewsCount: Int,
    val ratingGoodReview: Double,
    val ratingGoodReviewVoteCount: Int,
    val ratingKinopoisk: Double,
    val ratingKinopoiskVoteCount: Int,
    val ratingImdb: Double,
    val ratingImdbVoteCount: Int,
    val ratingFilmCritics: Double,
    val ratingFilmCriticsVoteCount: Int,
    val ratingAwait: Int,
    val ratingAwaitCount: Int,
    val webUrl: String,
    val year: Int,
    val filmLength: Int,
    val description: String,
    val type: String,
    val ratingMpaa: String,
    val countries: List<Country>,
    val genres: List<Genre>,
    val serial: Boolean,
    val shortFilm: Boolean,
    val completed: Boolean,
    val hasImax: Boolean,
    val has3D: Boolean
)

@Parcelize
@Entity(tableName = "favouritemovies")
data class Movie(
    @PrimaryKey val kinopoiskId: Int,
    val imdbId: String,
    val nameRu: String,
    val nameEn: String?,
    val nameOriginal: String?,
    val countries: List<Country>,
    val genres: List<Genre>,
    val ratingKinopoisk: Double?,
    val ratingImdb: Double?,
    val year: Int,
    val type: String,
    val posterUrl: String,
    val posterUrlPreview: String
) : Parcelable

@Parcelize
data class Country(
    val country: String
) : Parcelable

@Parcelize
data class Genre(
    val genre: String
) : Parcelable
