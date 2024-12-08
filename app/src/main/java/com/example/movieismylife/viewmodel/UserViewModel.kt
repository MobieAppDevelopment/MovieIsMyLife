//package com.example.movieismylife.viewmodel
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.movieismylife.model.Comment
//import com.example.movieismylife.model.User
//import com.google.firebase.firestore.FirebaseFirestore
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.tasks.await
//
//class UserViewModel : ViewModel() {
//    private val _comments = MutableStateFlow<List<Comment>>(emptyList())
//    val comments = _comments.asStateFlow()
//
//    private val db = FirebaseFirestore.getInstance()
//
//    fun listenForComments(userId: String) {
//        val querySnapshot = db.collection("movies")
//            .get()
//            .addOnSuccessListener { movies ->
//                for (movie in movies) {
//                    val movieId = movie.id
//
//                    db.collection("movies")
//                        .document(movieId)
//                        .collection("comments")
//                        .get()
//                        .addOnSuccessListener { comments ->
//                            val list = mutableListOf<Comment>()
//                            for (comment in comments) {
//                                val id = comment.getString("userId")
//                                if (id == userId) {
//                                    val commentObject = comment.toObject(Comment::class.java)
//                                    list.add(commentObject) // 리스트에 추가
//                                }
//                            }
//                            _comments.value = list
//                        }
//                        .addOnFailureListener { exception ->
//                            println("Error accessing comments in movie '$movieId': $exception")
//                        }
//                }
//            }
//            .addOnFailureListener { exception ->
//                println("Error accessing movies: $exception")
//            }
//    }
//
//    private suspend fun generateNewUserId(): Int {
//        val querySnapshot = db.collection("users")
//            .get()
//            .await()
//
//        return if (querySnapshot.isEmpty) {
//            1 // 첫 번째 사용자인 경우
//        } else {
//            val maxId = querySnapshot.documents
//                .mapNotNull { it.id.toIntOrNull() }
//                .maxOrNull() ?: 0
//            maxId + 1 // 최대 ID에 1을 더함
//        }
//    }
//}

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class User(
    val id: String,
    val name: String,
    val password: String,
    val profile: String,
    val userName: String
)

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