package ru.tinkoff.edu.meganov.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.tinkoff.edu.meganov.data.Movie
import ru.tinkoff.edu.meganov.data.MovieRepo

class MovieListVM(private val movieRepo: MovieRepo) : ViewModel() {
    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> get() = _movies

    private var currentPage = 1
    private val pageSize = 20

    init {
        loadMoreMovies()
    }

    fun loadMoreMovies() {
        viewModelScope.launch {
            val newMovies = movieRepo.getTopMovies(currentPage, pageSize)
            _movies.value = _movies.value.orEmpty() + newMovies
            currentPage++
        }
    }
}
