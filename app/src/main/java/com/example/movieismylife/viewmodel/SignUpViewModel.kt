package com.example.movieismylife.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieismylife.model.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class SignUpViewModel : ViewModel() {
    private val _state = MutableStateFlow<SignUpState>(SignUpState.Nothing)
    val state = _state.asStateFlow()

    private val db = FirebaseFirestore.getInstance()

    fun signUp(name: String, userName: String, password: String, profile: String) {
        viewModelScope.launch {
            try {
                _state.value = SignUpState.Loading

                // 새 사용자 ID 생성
                val newUserId = generateNewUserId()

                val user = User(
                    name = name,
                    userName = userName,
                    password = password,
                    profile = profile
                )

                // Firestore에 사용자 추가
                db.collection("users")
                    .document(newUserId.toString())
                    .set(user)
                    .await()

                _state.value = SignUpState.Success
            } catch (e: Exception) {
                _state.value = SignUpState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    private suspend fun generateNewUserId(): Int {
        val querySnapshot = db.collection("users")
            .get()
            .await()

        return if (querySnapshot.isEmpty) {
            1 // 첫 번째 사용자인 경우
        } else {
            val maxId = querySnapshot.documents
                .mapNotNull { it.id.toIntOrNull() }
                .maxOrNull() ?: 0
            maxId + 1 // 최대 ID에 1을 더함
        }
    }
}

sealed class SignUpState {
    object Nothing : SignUpState()
    object Loading : SignUpState()
    object Success : SignUpState()
    data class Error(val message: String) : SignUpState()
}