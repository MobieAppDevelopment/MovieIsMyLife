package com.example.movieismylife.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import com.example.movieismylife.model.User

class UserViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    private val usersCollection = firestore.collection("users")

    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> = _users

    init {
        loadUsers()
    }

    private fun loadUsers() {
        viewModelScope.launch {
            try {
                val snapshot = usersCollection.get().await()
                val userList = snapshot.documents.mapNotNull { document ->
                    val id = document.id
                    val name = document.getString("name") ?: ""
                    val password = document.getString("password") ?: ""
                    val profile = document.getString("profile") ?: ""
                    val userName = document.getString("userName") ?: ""
                    User(id, name, password, profile, userName)
                }
                _users.value = userList
            } catch (e: Exception) {
                // 에러 처리
                println("Error loading users: ${e.message}")
            }
        }
    }

    fun getUserById(id: String): User? {
        return users.value?.find { it.id == id }
    }

    // 필요한 경우 사용자 추가, 업데이트, 삭제 메서드를 여기에 추가할 수 있습니다.
}