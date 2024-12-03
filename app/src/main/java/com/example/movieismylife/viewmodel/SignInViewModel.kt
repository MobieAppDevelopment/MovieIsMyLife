package com.example.movieismylife.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class SignInViewModel : ViewModel() {
    private val _state = MutableStateFlow<SignInState>(SignInState.Nothing)
    val state = _state.asStateFlow()

    private val db = FirebaseFirestore.getInstance()

    fun signIn(userName: String, password: String) {
        viewModelScope.launch {
            try {
                _state.value = SignInState.Loading

                // Query Firestore to find a user with the given userName and password
                val querySnapshot = db.collection("users")
                    .whereEqualTo("userName", userName)
                    .whereEqualTo("password", password)
                    .get()
                    .await()

                if (!querySnapshot.isEmpty) {
                    // User found, login successful
                    val user = querySnapshot.documents[0].toObject(User::class.java)
                    _state.value = SignInState.Success(user)
                } else {
                    // User not found or incorrect credentials
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
    data class Success(val user: User?) : SignInState()
    data class Error(val message: String) : SignInState()
}

// User data class (you might want to move this to a separate file)
data class User(
    val id: Int = 0,
    val name: String = "",
    val userName: String = "",
    val password: String = "",
    val profile: String = ""
)