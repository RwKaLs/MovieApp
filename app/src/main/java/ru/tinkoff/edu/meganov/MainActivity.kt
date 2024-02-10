package ru.tinkoff.edu.meganov

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.tinkoff.edu.meganov.data.MovieRepo
import ru.tinkoff.edu.meganov.ui.composables.MoviesList
import ru.tinkoff.edu.meganov.ui.theme.MovieAppTheme
import ru.tinkoff.edu.meganov.viewmodel.MovieListVM

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MovieAppTheme {
                val viewModel = viewModel<MovieListVM>(
                    factory = object : ViewModelProvider.Factory {
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            return MovieListVM(MovieRepo()) as T
                        }
                    }
                )
                MoviesList(viewModel = viewModel)
            }
        }
    }
}
