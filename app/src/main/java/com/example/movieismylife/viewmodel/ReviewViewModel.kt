package com.example.movieismylife.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movieismylife.model.Comment
import com.google.firebase.firestore.FirebaseFirestore

class ReviewViewModel : ViewModel() {
    // 댓글을 저장할 MutableLiveData
    private val _comments = MutableLiveData<List<Comment>>()
    val comments: LiveData<List<Comment>> get() = _comments

    fun onCreate(userId:String, movieId:String, content:String) {
        Log.d("Firestore", "!!!!!!!")
        // Firestore 인스턴스 가져오기
        val db = FirebaseFirestore.getInstance()

        // 댓글 객체 생성
        val comment = Comment(
            userId = userId,
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
        val db = FirebaseFirestore.getInstance()

        // 'movies' 컬렉션에서 'movieId' 문서의 'comments' 서브컬렉션을 가져오기
        db.collection("movies")
            .document(movieId) // 특정 영화 문서 ID
            .collection("comments") // 'comments' 서브컬렉션
            .get() // 댓글 데이터를 비동기적으로 가져옴
            .addOnSuccessListener { querySnapshot ->
                val commentList = mutableListOf<Comment>()
                for (document in querySnapshot) {
                    // 댓글 데이터를 Comment 객체로 변환하여 리스트에 추가
                    val comment = document.toObject(Comment::class.java)
                    commentList.add(comment)
                }
                // 댓글 리스트를 LiveData에 설정
                _comments.value = commentList
            }
            .addOnFailureListener { e ->
                // 오류가 발생하면 로그로 출력
                e.printStackTrace()
            }
    }
}