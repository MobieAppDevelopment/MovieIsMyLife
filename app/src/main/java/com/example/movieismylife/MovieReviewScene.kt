package com.example.movieismylife

import android.graphics.Paint.Style
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontSynthesis.Companion.Style
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.movieismylife.enum.SortOption
import com.example.movieismylife.ui.ReviewItem
import com.example.movieismylife.viewmodel.MovieReviewViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieReviewScreen(viewModel: MovieReviewViewModel = viewModel()) {
    val reviews by viewModel.reviews.collectAsState()
    val sortOption by viewModel.sortOption.collectAsState()
    val reviewType by viewModel.reviewType.collectAsState()

    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        TopAppBar(title = {
            Text(
                "MOVIE IS MY LIFE",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }, navigationIcon = {
            IconButton(onClick = { /* TODO */ }) {
                Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.White)
            }
        }, actions = {
            IconButton(onClick = { /* TODO */ }) {
                Icon(
                    Icons.Default.Search, contentDescription = "Search", tint = Color.White
                )
            }
        }, colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Black
        )
        )
    }) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding), color = Color.Black
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = reviewType,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    var expanded by remember { mutableStateOf(false) }
                    ExposedDropdownMenuBox(expanded = expanded,
                        onExpandedChange = { expanded = it }) {
                        TextField(
                            value = if (sortOption == SortOption.LATEST) "최신순" else "별점순",
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            colors = TextFieldDefaults.colors(
                                unfocusedContainerColor = Color.White,
                                focusedContainerColor = Color.White,
                                unfocusedTextColor = Color.Black,
                                focusedTextColor = Color.Black,
                                unfocusedTrailingIconColor = Color.Black,
                                focusedTrailingIconColor = Color.Black
                            ),        textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
                            maxLines = 1,
                            modifier = Modifier
                                .menuAnchor()
                                .width(120.dp)
                                .height(50.dp)
                        )
                        ExposedDropdownMenu(expanded = expanded,
                            onDismissRequest = { expanded = false }) {
                            DropdownMenuItem(text = { Text("최신순") }, onClick = {
                                viewModel.setSortOption(SortOption.LATEST)
                                expanded = false
                            })
                            DropdownMenuItem(text = { Text("별점순") }, onClick = {
                                viewModel.setSortOption(SortOption.RATING)
                                expanded = false
                            })
                        }
                    }
                }
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(reviews) { review ->
                        ReviewItem(review = review)
                    }
                }
            }
        }
    }
}

