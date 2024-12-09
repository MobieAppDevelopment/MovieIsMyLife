package com.example.movieismylife

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.material3.Switch
import com.example.movieismylife.viewmodel.ReviewViewModel
import com.example.movieismylife.viewmodel.SignInState
import com.example.movieismylife.viewmodel.SignInViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewWritePage(
    navController: NavController,
    reviewViewModel: ReviewViewModel,
    signInViewModel: SignInViewModel,
    movieId: String
    ) {
    var reviewText by remember { mutableStateOf("") }
    var isPrivate by remember { mutableStateOf(false) }
    val uiState by signInViewModel.state.collectAsState()
    val getUserId = (uiState as? SignInState.Success)?.user?.id ?: -1
    val userId = getUserId.toString()
    val score = reviewViewModel._score

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF1A1B1E)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Top Bar
            TopAppBar(
                title = {
                    Text(
                        "리뷰 작성하기",
                        color = Color.White,
                        fontSize = 18.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    TextButton(
                        onClick = {
                            navController.popBackStack()
                            reviewViewModel.createReview(userId = userId, movieId = movieId, content = reviewText, score = score)
                            reviewViewModel.loadComments(movieId = movieId, userId = userId)
                            signInViewModel.getMyComments(id = userId)
                                  },
                        enabled = reviewText.isNotEmpty()
                    ) {
                        Text(
                            "저장",
                            color = if (reviewText.isNotEmpty())
                                Color(0xFF4B6BFB) else Color.Gray
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1A1B1E)
                )
            )

            // Review Text Input
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    value = reviewText,
                    onValueChange = {
                        if (it.length <= 100) {
                            reviewText = it
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    placeholder = {
                        Text(
                            "내 생각을 짧게 기록해보세요.",
                            color = Color.Gray,
                            fontSize = 16.sp
                        )
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color(0xFF242632),
                        focusedBorderColor = Color(0xFF4B6BFB),
                        unfocusedBorderColor = Color(0xFF242632),
                        cursorColor = Color.White
                    ),
                    textStyle = TextStyle(
                        fontSize = 16.sp,
                        color = Color.White
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text
                    )
                )

                // Character Counter
                Text(
                    text = "${reviewText.length}/100",
                    color = Color.Gray,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                        .wrapContentWidth()
                )

//                // Private Toggle
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(top = 16.dp),
//                    horizontalArrangement = Arrangement.SpaceBetween
//                ) {
//                    Text(
//                        text = "비공개",
//                        color = Color.White,
//                        fontSize = 16.sp
//                    )
//                    Switch(
//                        checked = isPrivate,
//                        onCheckedChange = { isPrivate = it },
//                        colors = SwitchDefaults.colors(
//                            checkedThumbColor = Color(0xFF4B6BFB),
//                            checkedTrackColor = Color(0xFF4B6BFB).copy(alpha = 0.5f),
//                            uncheckedThumbColor = Color.Gray,
//                            uncheckedTrackColor = Color.Gray.copy(alpha = 0.5f)
//                        )
//                    )
//                }
            }
        }
    }
}


