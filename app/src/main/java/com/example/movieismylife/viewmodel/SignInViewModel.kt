package com.example.movieismylife.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class SignInViewModel : ViewModel() {
    private val _state = MutableStateFlow<SignInState>(SignInState.Nothing)
    val state: StateFlow<SignInState> = _state.asStateFlow()

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
                    _state.value = SignInState.Success(user)
                } else {
                    _state.value = SignInState.Error("Invalid username or password")
                }
            } catch (e: Exception) {
                _state.value = SignInState.Error(e.message ?: "Unknown error occurred")
            }
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

