package ru.tinkoff.edu.meganov.ui.composables

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.tinkoff.edu.meganov.viewmodel.MovieListVM

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetails(viewModel: MovieListVM, movieId: String?, navController: NavController, modifier: Modifier = Modifier) {
    LaunchedEffect(movieId) {
        movieId?.let { viewModel.selectMovie(it) }
    }
    LaunchedEffect(movieId) {
        movieId?.let { viewModel.selectMovie(it) }
    }
    val movieDetails by viewModel.selectedMovie.observeAsState()
    val isLoading by viewModel.isLoading.observeAsState(true)
    val loadError by viewModel.loadError.observeAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = if (isLoading) "Loading..." else movieDetails?.nameRu?: "Loading...") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) {
        if (isLoading) {

        } else if (loadError != null) {
            ErrorScreen(message = loadError!!, onRetry = { movieId?.let { it1 ->
                viewModel.selectMovie(
                    it1
                )
            } }, modifier)
        } else if (movieDetails != null) {
            LazyColumn(modifier = Modifier.padding(16.dp)) {
                item {
                    NetworkImage(
                        url = movieDetails!!.posterUrl,
                        modifier = modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = movieDetails!!.description)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "Жанры: ${movieDetails!!.genres.joinToString { it.genre }}")
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "Страны: ${movieDetails!!.countries.joinToString { it.country }}")
                }
            }
        }
    }
}

@Composable
fun ErrorScreen(message: String, onRetry: () -> Unit, modifier: Modifier = Modifier) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier.padding(10.dp)) {
            Icon(Icons.Filled.Warning, contentDescription = "ErrorIcon", modifier = modifier.size(50.dp))
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = message, style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(16.dp))
            IconButton(onClick = onRetry) {
                Icon(Icons.Filled.Refresh, contentDescription = "RetryIcon", modifier = modifier.size(25.dp))
            }
        }
    }
}

