package com.example.movieismylife.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieismylife.model.Comment
import com.example.movieismylife.model.CommentView
import com.example.movieismylife.model.Reply
import com.example.movieismylife.model.ReplyView
import com.google.firebase.Timestamp
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

    private val _replies = MutableStateFlow<List<ReplyView>>(emptyList())
    val replies = _replies.asStateFlow()

    fun createReply(userId: String, commentId: String?, content: String) {
        // Firestore 인스턴스 가져오기
        val db = FirebaseFirestore.getInstance()

        // 댓글 객체 생성
        val reply = Reply(
            userId = userId,
            commentId = commentId ?: "",
            content = content,
            createdAt = Timestamp.now()
        )

//        // 'movies' 컬렉션에서 'movie_id' 문서 내 'comments' 서브컬렉션에 댓글 추가
//        val movieId = movieId  // 특정 영화의 ID (예: Inception, Titanic 등)

        // movies 컬렉션에서 해당 movieId 문서의 comments 서브컬렉션 안의 commentId 문서의 replies 서브컬렉션에 추가
        db.collection("comments")
            .document(commentId ?: "")  // 특정 commentId
            .collection("replies")
            .add(reply)
            .addOnSuccessListener { documentReference ->
                // 댓글이 성공적으로 추가되었을 때 처리
                Log.d("Firestore", "Reply added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                // 오류가 발생했을 때 처리
                Log.w("Firestore", "Error adding reply", e)
            }
    }

    // 댓글을 비동기적으로 불러오는 함수
    fun loadReplies(commentId: String) {
        viewModelScope.launch {
            val db = FirebaseFirestore.getInstance()

            try {
                // Firestore에서 댓글 가져오기
                val querySnapshot = db.collection("comments")
                    .document(commentId)
                    .collection("replies")
                    .get()
                    .await()

                val replyList = mutableListOf<ReplyView>()

                for (document in querySnapshot) {
                    val reply = document.toObject(Reply::class.java)

                    // Firestore에서 사용자 정보 가져오기
                    val (name, profile) = fetchName(reply.userId)

                    // ReplyView 생성
                    val replyView = ReplyView(
                        name = name ?: "Unknown",
                        profile = profile ?: "",
                        content = reply.content
                    )
                    replyList.add(replyView)
                }

                // UI 업데이트 (Main Thread)
                withContext(Dispatchers.Main) {
                    _replies.value = replyList
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