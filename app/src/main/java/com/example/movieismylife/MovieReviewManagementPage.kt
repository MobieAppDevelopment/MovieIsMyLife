package com.example.movieismylife

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.movieismylife.enum.SortOption
import com.example.movieismylife.ui.ReviewItem
import com.example.movieismylife.viewmodel.MovieReviewViewModel
import com.example.movieismylife.viewmodel.ReviewViewModel
import com.google.android.libraries.places.api.model.Review

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieReviewManagementPage(
    navController: NavController,
    userId: String,
    reviewViewModel: ReviewViewModel,
    onClickBackArrow: () -> Unit,
    ) {
    val myComments by reviewViewModel.myComments.collectAsState()
    val myLikeComments by reviewViewModel.myLikeComments.collectAsState()
    val sortOption by reviewViewModel.sortOption.collectAsState(initial = SortOption.LATEST)
    val reviewType by reviewViewModel.reviewType.collectAsState()

//    val reviews by movieReviewViewModel.reviews.collectAsState()
//    val sortOption by movieReviewViewModel.sortOption.collectAsState()
//    val reviewType by movieReviewViewModel.reviewType.collectAsState()

    Scaffold(modifier = Modifier.fillMaxSize(),
        bottomBar = { MovieBottomBar(navController=navController) }
    ) { innerPadding ->
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
                    IconButton(onClick = onClickBackArrow) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                    Text(
                        text = "MY REVIEW",
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
                                reviewViewModel.setSortOption(SortOption.LATEST, reviewType)
                                expanded = false
                            })
                            DropdownMenuItem(text = { Text("별점순") }, onClick = {
                                reviewViewModel.setSortOption(SortOption.RATING, reviewType)
                                expanded = false
                            })
                        }
                    }
                }
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    if(reviewType == "MY REVIEW") {
                        items(myComments) { myComment ->
                            ReviewItem(myComment = myComment)
                        }
                    } else {
                        items(myLikeComments) { myLikeComment ->
                            ReviewItem(myComment = myLikeComment)
                        }
                    }
                }
            }
        }
    }
}

