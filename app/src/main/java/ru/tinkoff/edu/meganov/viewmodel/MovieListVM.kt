package ru.tinkoff.edu.meganov.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.tinkoff.edu.meganov.data.Movie
import ru.tinkoff.edu.meganov.data.MovieDetailsResponse
import ru.tinkoff.edu.meganov.data.MovieRepo

class MovieListVM(private val movieRepo: MovieRepo) : ViewModel() {

    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> get() = _movies
    private var currentPage = 1
    private val pageSize = 20

    init {
        loadMoreMovies()
    }
    private val _selectedMovie = MutableLiveData<MovieDetailsResponse>()
    val selectedMovie: LiveData<MovieDetailsResponse> get() = _selectedMovie

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading
    private val _loadError = MutableLiveData<String?>()
    val loadError: LiveData<String?> get() = _loadError

    fun selectMovie(id: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val movieDetails = movieRepo.getMovieDetails(id)
                _selectedMovie.value = movieDetails
                _loadError.value = null
            } catch (e: Exception) {
                _loadError.value = e.localizedMessage ?: "ERROR"
            }
            _isLoading.value = false
        }
    }

    fun loadMoreMovies() {
        viewModelScope.launch {
            try {
                val newMovies = movieRepo.getTopMovies(currentPage, pageSize)
                _movies.value = _movies.value.orEmpty() + newMovies
                currentPage++
                _loadError.value = null
            } catch (e: Exception) {
                _loadError.value = e.localizedMessage ?: "ERROR"
            }
        }
    }
}
