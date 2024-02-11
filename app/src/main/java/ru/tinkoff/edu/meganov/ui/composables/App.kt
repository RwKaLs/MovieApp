package ru.tinkoff.edu.meganov.ui.composables

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import ru.tinkoff.edu.meganov.viewmodel.MovieListVM

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun App(context: Context, viewModel: MovieListVM, modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "movie_list") {
        composable("movie_list") { MovieList(context, viewModel, navController, modifier) }
        composable("movie_detail/{movieId}") { backStackEntry ->
            val movieId = backStackEntry.arguments?.getString("movieId")
            MovieDetails(viewModel, movieId, navController, modifier)
        }
    }

}

@Composable
fun NetworkImage(url: String, modifier: Modifier = Modifier) {
    val painter = rememberImagePainter(data = url)

    Image(
        painter = painter,
        contentDescription = "ImagePainter",
        modifier = modifier
    )
}
