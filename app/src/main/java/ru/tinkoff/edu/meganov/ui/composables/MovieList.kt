package ru.tinkoff.edu.meganov.ui.composables

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import ru.tinkoff.edu.meganov.viewmodel.MovieListVM

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoviesList(viewModel: MovieListVM, modifier: Modifier = Modifier) {
    val movies by viewModel.movies.observeAsState(emptyList())
    var searchQuery by rememberSaveable {
        mutableStateOf("")
    }
    Box(modifier = modifier.fillMaxSize()) {
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
                            label = { Text("Search") },
                            modifier = Modifier.weight(1f)
                        )
                        ElevatedButton(
                            onClick = { /*TODO*/ },
                            modifier = Modifier.padding(start = 8.dp)
                        ) {
                            Image(imageVector = Icons.Filled.Search, contentDescription = "SearchBtn")
                        }
                    }
                }
            )
            LazyColumn(modifier = modifier) {
                items(movies) { movie ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(modifier = Modifier.padding(16.dp)) {
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
                                    .padding(bottom = 50.dp)
                            ) {
                                Text(text = movie.nameRu,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp,
                                    modifier = modifier)
                                Text(text = movie.year.toString(),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontSize = 18.sp,
                                    modifier = modifier)
                            }
                        }
                    }
                    if (movie == movies.last()) {
                        LaunchedEffect(Unit) {
                            viewModel.loadMoreMovies()
                        }
                    }
                }
            }
        }
        BottomNavigation(
            modifier = modifier.align(Alignment.BottomCenter)
        ) {
            BottomNavigationItem(
                icon = { Icon(Icons.Filled.Favorite, contentDescription = null) },
                label = { Text("Избранное") },
                selected = false, // You can use a mutable state to hold the selected tab
                onClick = { /* Handle favorite click */ }
            )
            BottomNavigationItem(
                icon = { Icon(Icons.Filled.Star, contentDescription = null) },
                label = { Text("Популярные") },
                selected = true, // You can use a mutable state to hold the selected tab
                onClick = { /* Handle popular click */ }
            )
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
