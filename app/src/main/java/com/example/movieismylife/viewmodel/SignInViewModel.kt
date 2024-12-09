package com.example.movieismylife.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieismylife.model.Comment
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class SignInViewModel : ViewModel() {
    private val _state = MutableStateFlow<SignInState>(SignInState.Nothing)
    val state: StateFlow<SignInState> = _state.asStateFlow()

    private val _likeComments = MutableLiveData<List<Comment>>()
    val likeComments: LiveData<List<Comment>> = _likeComments

    private val _myComments = MutableLiveData<List<Comment>>()
    val myComments: LiveData<List<Comment>> = _myComments


    private val firestore = FirebaseFirestore.getInstance()
    private val usersCollection = firestore.collection("users")

    fun signIn(userName: String, password: String) {
        viewModelScope.launch {
            try {
                _state.value = SignInState.Loading

                val querySnapshot = usersCollection
                    .whereEqualTo("userName", userName)
                    .whereEqualTo("password", password)
                    .get()
                    .await()

                if (!querySnapshot.isEmpty) {
                    val document = querySnapshot.documents[0]
                    val user = User(
                        id = document.id,
                        name = document.getString("name") ?: "",
                        userName = document.getString("userName") ?: "",
                        password = document.getString("password") ?: "",
                        profile = document.getString("profile") ?: ""
                    )
                    getLikeComments(document.id)
                    getMyComments(document.id)

                    _state.value = SignInState.Success(user)
                } else {
                    _state.value = SignInState.Error("Invalid username or password")
                }
            } catch (e: Exception) {
                _state.value = SignInState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    public fun getLikeComments(id: String) {
        viewModelScope.launch {
            try {
                val querySnapshot = firestore.collection("comments").get().await()

                if (!querySnapshot.isEmpty) {
                    val commentList = querySnapshot.documents.mapNotNull { document ->
                        val likeUsersSnapshot = firestore.collection("comments")
                            .document(document.id)  // 해당 다큐먼트 지정
                            .collection("likeUsers")  // likeusers 서브컬렉션
                            .whereEqualTo("userId", id)
                            .get().await()

                        if(likeUsersSnapshot.isEmpty) {
                            null
                        }
                        else {
                            // 좋아요를 누른 사용자에 대한 댓글을 가져옴
                            val comment = Comment(
                                content = document.getString("content") ?: "",
                                createdAt = document.getTimestamp("createdAt")!!,
                                movieId = document.getString("movieId") ?: "",
                                score = document.getLong("score") ?: 0,
                                userId = document.getString("userId") ?: ""
                            )
                            comment
                        }
                    }.filterNotNull()
                    _likeComments.value = commentList
                } else {
                    //
                }

            } catch (e: Exception) {
                _state.value = SignInState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    public fun getMyComments(id: String) {
        viewModelScope.launch {
            try {
                val querySnapshot = firestore.collection("comments")
                    .whereEqualTo("userId", id)
                    .get()
                    .await()

                if (!querySnapshot.isEmpty) {
                    val commentList = querySnapshot.documents.mapNotNull { document ->
                        if(querySnapshot.isEmpty) {
                            null
                        } else {
                            Comment(
                                content = document.getString("content") ?: "",
                                createdAt = document.getTimestamp("createdAt") ?: Timestamp.now(),
                                movieId = document.getString("movieId") ?: "",
                                score = document.getLong("score") ?: 0,
                                userId = document.getString("userId") ?: ""
                            )
                        }
                    }.filterNotNull()
                    _myComments.value = commentList
                }

            } catch (e: Exception) {
                _state.value = SignInState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            // Reset the sign-in state
            _state.value = SignInState.Nothing

            // Clear liked comments
            _likeComments.value = emptyList()

            // Clear user's comments
            _myComments.value = emptyList()

            // You might want to perform additional cleanup here if needed
            // For example, clearing any cached data or resetting other variables

            Log.d("SignInViewModel", "User logged out and all states reset")
        }
    }
}

sealed class SignInState {
    object Nothing : SignInState()
    object Loading : SignInState()
    data class Success(val user: User) : SignInState()
    data class Error(val message: String) : SignInState()
}

data class User(
    val id: String,
    val name: String,
    val userName: String,
    val password: String,
    val profile: String
)

