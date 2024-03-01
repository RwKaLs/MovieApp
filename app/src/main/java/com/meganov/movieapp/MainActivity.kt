package com.meganov.movieapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.meganov.movieapp.data.MovieRepo
import com.meganov.movieapp.ui.composables.App
import com.meganov.movieapp.ui.theme.MovieAppTheme
import com.meganov.movieapp.viewmodel.MovieListVM

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MovieAppTheme {
//                val systemUiController = rememberSystemUiController()
//                systemUiController.setSystemBarsColor(color = Color.Transparent)
                val viewModel = viewModel<MovieListVM>(
                    factory = object : ViewModelProvider.Factory {
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            return MovieListVM(applicationContext, MovieRepo()) as T
                        }
                    }
                )
                App(context = this, viewModel = viewModel)
            }
        }
    }
}
