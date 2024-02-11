package ru.tinkoff.edu.meganov.ui.composables

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import ru.tinkoff.edu.meganov.data.AppDatabase
import ru.tinkoff.edu.meganov.viewmodel.MovieListVM

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
fun MovieList(
    context: Context,
    viewModel: MovieListVM,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val movies by viewModel.movies.observeAsState(emptyList())
    var searchQuery by rememberSaveable {
        mutableStateOf("")
    }
    var favouriteSelected by rememberSaveable {
        mutableStateOf(false)
    }
    var searching by rememberSaveable {
        mutableStateOf(false)
    }
    val isLoading by viewModel.isLoading.observeAsState(true)
    val loadError by viewModel.loadError.observeAsState()
    val favoriteMovies by viewModel.favoriteMovies.observeAsState()
    val showingmovies by viewModel.showingMovies.observeAsState(emptyList())
    val movieDetailsDao = AppDatabase.getDatabase(context).movieDetailsDao()
    val portrait = LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT
    if (portrait || !portrait) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.primary)
        ) {
            Column {
                TopAppBar(
                    title = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            TextField(
                                value = searchQuery,
                                onValueChange = { searchQuery = it },
                                label = { Text("Поиск") },
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(
                                onClick = {
                                    searching = searchQuery != ""
                                    viewModel.updateShowingMovies(showingmovies.filter { movie ->
                                        movie.nameRu.contains(searchQuery, ignoreCase = true)
                                    })
                                },
                                modifier = modifier
                                    .padding(start = 8.dp, end = 8.dp)
                                    .align(Alignment.CenterVertically)
                            ) {
                                Image(
                                    imageVector = Icons.Filled.Search,
                                    contentDescription = "SearchBtn"
                                )
                            }
                        }
                    }
                )
                if (isLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else if (loadError != null && !favouriteSelected) {
                    ErrorScreen(
                        message = loadError!!,
                        onRetry = { viewModel.loadMoreMovies() },
                        modifier
                    )
                } else {
                    LazyColumn(modifier = modifier) {
                        items(showingmovies) { movie ->
                            val isFavourite = rememberSaveable {
                                mutableStateOf(false)
                            }
                            viewModel.viewModelScope.launch {
                                isFavourite.value =
                                    MutableLiveData(movieDetailsDao.getById("${movie.kinopoiskId}")).map { it != null }.value!!
                            }
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                                    .combinedClickable(
                                        onClick = {
                                            navController.navigate("movie_detail/${movie.kinopoiskId}")
                                        },
                                        onLongClick = {
                                            viewModel.viewModelScope.launch {
                                                if (isFavourite.value) {
                                                    movieDetailsDao.delete(movie)
                                                } else {
                                                    movieDetailsDao.insert(movie)
                                                }
                                                isFavourite.value = !isFavourite.value
                                            }
                                        }
                                    ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Row(modifier = Modifier.padding(10.dp)) {
                                    NetworkImage(
                                        url = movie.posterUrlPreview,
                                        modifier = Modifier
                                            .height(120.dp)
                                            .width(120.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                    )
                                    Spacer(modifier = Modifier.width(16.dp))
                                    Column(
                                        modifier = modifier
                                            .align(Alignment.CenterVertically)
                                            .weight(1f)
                                            .padding(bottom = 50.dp)
                                    ) {
                                        Text(
                                            text = movie.nameRu,
                                            style = MaterialTheme.typography.titleLarge,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 20.sp,
                                            modifier = modifier
                                        )
                                        Text(
                                            text = movie.year.toString(),
                                            style = MaterialTheme.typography.titleMedium,
                                            fontSize = 18.sp,
                                            modifier = modifier
                                        )
                                    }
                                    if (isFavourite.value) {
                                        Icon(
                                            imageVector = Icons.Filled.Star,
                                            contentDescription = "Favourite"
                                        )
                                    }
                                }
                            }
                            if (movie == showingmovies.last() && !favouriteSelected && !searching) {
                                LaunchedEffect(Unit) {
                                    viewModel.loadMoreMovies()
                                }
                            }
                        }
                    }
                }
            }
            BottomNavigation(
                backgroundColor = MaterialTheme.colorScheme.inversePrimary,
                modifier = modifier.align(Alignment.BottomCenter)
            ) {
                BottomNavigationItem(
                    icon = { Icon(Icons.Filled.Star, contentDescription = null) },
                    label = { Text("Популярные") },
                    selected = !favouriteSelected,
                    onClick = {
                        favouriteSelected = false
                        viewModel.movies.value?.let { viewModel.updateShowingMovies(it) }
                    }
                )
                BottomNavigationItem(
                    icon = { Icon(Icons.Filled.Favorite, contentDescription = null) },
                    label = { Text("Избранное") },
                    selected = favouriteSelected,
                    onClick = {
                        favouriteSelected = true
                        viewModel.favoriteMovies.value?.let { viewModel.updateShowingMovies(it) }
                    }
                )
            }
        }
    } else {
//        var selectedMovie: Movie? by remember {
//            mutableStateOf(null)
//        }
//        Row(Modifier.fillMaxSize()) {
//            Box(modifier = Modifier.weight(1f)) {
//                Column {
//                    TopAppBar(
//                        title = {
//                            Row(
//                                modifier = Modifier.fillMaxWidth(),
//                                horizontalArrangement = Arrangement.SpaceBetween
//                            ) {
//                                TextField(
//                                    value = searchQuery,
//                                    onValueChange = { searchQuery = it },
//                                    label = { Text("Поиск") },
//                                    modifier = Modifier.weight(1f)
//                                )
//                                IconButton (
//                                    onClick = {
//                                        showingmovies = movies.filter { movie ->
//                                            movie.nameRu.contains(searchQuery, ignoreCase = true)
//                                        }
//                                    },
//                                    modifier = modifier
//                                        .padding(start = 8.dp, end = 8.dp)
//                                        .align(Alignment.CenterVertically)
//                                ) {
//                                    Image(imageVector = Icons.Filled.Search, contentDescription = "SearchBtn")
//                                }
//                            }
//                        }
//                    )
//                    if (isLoading) {
//                        // loading
//                    } else if (loadError != null && !favouriteSelected) {
//                        ErrorScreen(message = loadError!!, onRetry = { viewModel.loadMoreMovies() }, modifier)
//                    } else {
//                        LazyColumn(modifier = modifier) {
//                            items(showingmovies) { movie ->
//                                val isFavourite = rememberSaveable {
//                                    mutableStateOf(false)
//                                }
//                                viewModel.viewModelScope.launch {
//                                    isFavourite.value = MutableLiveData(movieDetailsDao.getById("${movie.kinopoiskId}")).map { it != null }.value!!
//                                }
//                                Card(
//                                    modifier = Modifier
//                                        .fillMaxWidth()
//                                        .padding(8.dp)
//                                        .combinedClickable(
//                                            onClick = {
//                                                selectedMovie = movie
//                                            },
//                                            onLongClick = {
//                                                viewModel.viewModelScope.launch {
//                                                    if (isFavourite.value) {
//                                                        movieDetailsDao.delete(movie)
//                                                    } else {
//                                                        movieDetailsDao.insert(movie)
//                                                    }
//                                                    isFavourite.value = !isFavourite.value
//                                                }
//                                            }
//                                        ),
//                                    shape = RoundedCornerShape(8.dp)
//                                ) {
//                                    Row(modifier = Modifier.padding(10.dp)) {
//                                        NetworkImage(
//                                            url = movie.posterUrlPreview,
//                                            modifier = Modifier
//                                                .height(120.dp)
//                                                .width(120.dp)
//                                                .clip(RoundedCornerShape(8.dp))
//                                        )
//                                        Spacer(modifier = Modifier.width(16.dp))
//                                        Column(
//                                            modifier = modifier
//                                                .align(Alignment.CenterVertically)
//                                                .weight(1f)
//                                                .padding(bottom = 50.dp)
//                                        ) {
//                                            Text(
//                                                text = movie.nameRu,
//                                                style = MaterialTheme.typography.titleLarge,
//                                                fontWeight = FontWeight.Bold,
//                                                fontSize = 20.sp,
//                                                modifier = modifier
//                                            )
//                                            Text(
//                                                text = movie.year.toString(),
//                                                style = MaterialTheme.typography.titleMedium,
//                                                fontSize = 18.sp,
//                                                modifier = modifier
//                                            )
//                                        }
//                                        if (isFavourite.value) {
//                                            Icon(imageVector = Icons.Filled.Star,
//                                                contentDescription = "Favourite")
//                                        }
//                                    }
//                                }
//                                if (movie == showingmovies.last() && !favouriteSelected) {
//                                    LaunchedEffect(Unit) {
//                                        viewModel.loadMoreMovies()
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//
//            Box(modifier = Modifier.weight(1f)) {
//                MovieDetails(viewModel, "${selectedMovie?.kinopoiskId}", navController, modifier)
//            }
//        }
    }
}
