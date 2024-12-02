package com.example.movieismylife.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.movieismylife.model.Comment
import com.example.movieismylife.model.CommentView
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class ReplyViewModel : ViewModel() {
    // 댓글을 저장할 MutableLiveData
//    private val _comments = MutableLiveData<List<Comment>>()
//    val comments: LiveData<List<Comment>> get() = _comments

    private val _comments = MutableStateFlow<List<CommentView>>(emptyList())
    val comments = _comments.asStateFlow()

    fun createReview(userId: String, movieId: String, content: String, score: Float) {
        Log.d("Firestore", "!!!!!!!")
        // Firestore 인스턴스 가져오기
        val db = FirebaseFirestore.getInstance()

        // 댓글 객체 생성
        val comment = Comment(
            userId = userId,
            movieId = movieId,
            score = score,
            content = content,
            createdAt = System.currentTimeMillis()
        )

//        // 'movies' 컬렉션에서 'movie_id' 문서 내 'comments' 서브컬렉션에 댓글 추가
//        val movieId = movieId  // 특정 영화의 ID (예: Inception, Titanic 등)

        db.collection("movies")  // 'movies' 컬렉션
            .document(movieId)  // 특정 영화 문서 ID (예: 'movie_id_1')
            .collection("comments")  // 'comments' 서브컬렉션
            .add(comment)  // 댓글 추가
            .addOnSuccessListener { documentReference ->
                // 댓글이 성공적으로 추가되었을 때 처리
                Log.d("Firestore", "Comment added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                // 오류가 발생했을 때 처리
                Log.w("Firestore", "Error adding comment", e)
            }
    }

    // 댓글을 비동기적으로 불러오는 함수
    fun loadComments(movieId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val db = FirebaseFirestore.getInstance()

            try {
                // Firestore에서 댓글 가져오기
                val querySnapshot = db.collection("movies")
                    .document(movieId)
                    .collection("comments")
                    .get()
                    .await()

                val commentList = mutableListOf<CommentView>()

                for (document in querySnapshot) {
                    val comment = document.toObject(Comment::class.java)

                    // Firestore에서 사용자 정보 가져오기
                    val (name, profile) = fetchName(comment.userId)

                    // CommentView 생성
                    val commentView = CommentView(
                        score = comment.score,
                        content = comment.content,
                        name = name ?: "Unknown",
                        profile = profile ?: "",
                        title = "",
                        posterImage = "",
                        createdAt = comment.createdAt
                    )
                    commentList.add(commentView)
                }

                // UI 업데이트 (Main Thread)
                withContext(Dispatchers.Main) {
                    _comments.value = commentList
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    suspend fun fetchName(userId: String): Pair<String?, String?> {
        return try {
            val db = FirebaseFirestore.getInstance()
            val document = db.collection("users").document(userId).get().await()

            if (document.exists()) {
                val username = document.getString("name") ?: "Unknown"
                val profile = document.getString("profile") ?: ""
                Pair(username, profile)
            } else {
                Pair("Unknown", "")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Pair("Unknown", "")
        }
    }
}