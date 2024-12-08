package com.example.movieismylife.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieismylife.model.Comment
import com.example.movieismylife.model.CommentView
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class ReviewViewModel : ViewModel() {
    // 댓글을 저장할 MutableLiveData
//    private val _comments = MutableLiveData<List<Comment>>()
//    val comments: LiveData<List<Comment>> get() = _comments

    var _userLike: Boolean = true
    var _likeCount: Int = 0

    // 상태 관리
    private val _sharedData = MutableStateFlow<CommentView?>(null)
    val sharedData: StateFlow<CommentView?> get() = _sharedData

    private val _comments = MutableStateFlow<List<CommentView>>(emptyList())
    val comments = _comments.asStateFlow()

    private val _myComments = MutableStateFlow<List<CommentView>>(emptyList())
    val myComments = _myComments.asStateFlow()

    private val _isLoading = MutableStateFlow(true) // 로드 상태 관리
    val isLoading: StateFlow<Boolean> = _isLoading

    // 상태 업데이트
    fun updateData(commentData: CommentView, userLike: Boolean, likeCount: Int) {
        _sharedData.value = commentData
        _userLike = userLike
        _likeCount = likeCount
    }

    fun createReview(userId:String, movieId:String, content:String, score:Long) {
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
    fun loadComments(movieId: String, userId: String) { // userId: 로그인돼있는 유저
        viewModelScope.launch {
            _isLoading.value = true
            _comments.value = emptyList()

            val db = FirebaseFirestore.getInstance()

            try {
                // Firestore에서 댓글 가져오기
                val querySnapshot = db.collection("comments")
                    .whereEqualTo("movieId", movieId)
                    .get()
                    .await()

                val commentList = mutableListOf<CommentView>()

                for (document in querySnapshot) {
                    val documentId = document.id
                    val comment = document.toObject(Comment::class.java)

                    // Firestore에서 사용자 정보 가져오기
                    val (name, profile) = fetchName(comment.userId)

                    // Firestore에서 좋아요 정보 가져오기
                    val userLike = fetchUserLike(userId, documentId) // userId: 로그인돼있는 유저
                    Log.d("wtf", "${userLike}")

                    // 좋아요 개수 카운트
                    var likeCount = 0
                    // Firestore에서 댓글 가져오기
                    val querySnapshot2 = db.collection("comments")
                        .document(documentId)
                        .collection("likeUsers")
                        .get()
                        .await()
                    for (document in querySnapshot2) {
                        likeCount += 1
                    }

                    // CommentView 생성
                    val commentView = CommentView(
                        score = comment.score,
                        content = comment.content,
                        name = name ?: "Unknown",
                        profile = profile ?: "",
                        title = "",
                        posterImage = "",
                        createdAt = comment.createdAt,
                        commentId = documentId,
                        movieId = movieId,
                        userLike = userLike,
                        likeCount = likeCount
                    )
                    commentList.add(commentView)
                }

                // UI 업데이트 (Main Thread)
                withContext(Dispatchers.Main) {
                    _comments.value = commentList
                }
                _isLoading.value = false
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun loadMyComments(userId: String) { // userId: 로그인돼있는 유저
        viewModelScope.launch {
            _isLoading.value = true
            _myComments.value = emptyList()

            val db = FirebaseFirestore.getInstance()

            try {
                // Firestore에서 댓글 가져오기
                val querySnapshot = db.collection("comments")
                    .get()
                    .await()

                val commentList = mutableListOf<CommentView>()

                for (document in querySnapshot) {
                    val documentId = document.id
                    val comment = document.toObject(Comment::class.java)

                    // Firestore에서 사용자 정보 가져오기
                    val (name, profile) = fetchName(comment.userId)

                    // Firestore에서 좋아요 정보 가져오기
                    val userLike = fetchUserLike(userId, documentId) // userId: 로그인돼있는 유저
                    Log.d("wtf", "${userLike}")

                    // 좋아요 개수 카운트
                    var likeCount = 0
                    // Firestore에서 댓글 가져오기
                    val querySnapshot2 = db.collection("comments")
                        .document(documentId)
                        .collection("likeUsers")
                        .get()
                        .await()
                    for (document in querySnapshot2) {
                        likeCount += 1
                    }

                    // CommentView 생성
                    val commentView = CommentView(
                        score = comment.score,
                        content = comment.content,
                        name = name ?: "Unknown",
                        profile = profile ?: "",
                        title = "",
                        posterImage = "",
                        createdAt = comment.createdAt,
                        commentId = documentId,
                        movieId = "",
                        userLike = userLike,
                        likeCount = likeCount
                    )
                    commentList.add(commentView)
                }

                // UI 업데이트 (Main Thread)
                withContext(Dispatchers.Main) {
                    _myComments.value = commentList
                }
                _isLoading.value = false
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

    suspend fun fetchUserLike(userId: String, documentId: String): Boolean {
        return try {
            val db = FirebaseFirestore.getInstance()
            val querySnapshot = db.collection("comments")
                    .document(documentId)
                    .collection("likeUsers")
                    .whereEqualTo("userId", userId)
                    .get()
                    .await()
            // 쿼리 결과 검사
            if (!querySnapshot.isEmpty) {
                for (document in querySnapshot.documents) {
                    Log.d("check", "Document found with userId: ${document.getString("userId")}")
                    return true // 문서가 존재하면 true 반환
                }
            }
            Log.d("check", "No document found for userId: $userId in commentId: $documentId")
            false // 쿼리 결과가 비어있으면 false 반환
        } catch (e: Exception) {
            false
        }
    }

    fun createLike(userId: String, commentId: String) {
        // Firestore 인스턴스 가져오기
        val db = FirebaseFirestore.getInstance()

        // 댓글 객체 생성
        val likeUser = hashMapOf(
            "userId" to userId,
        )

//        // 'movies' 컬렉션에서 'movie_id' 문서 내 'comments' 서브컬렉션에 댓글 추가
//        val movieId = movieId  // 특정 영화의 ID (예: Inception, Titanic 등)

        db.collection("comments")
            .document(commentId)
            .collection("likeUsers")
            .add(likeUser)  // 댓글 추가
            .addOnSuccessListener { documentReference ->
                // 댓글이 성공적으로 추가되었을 때 처리
                Log.d("Firestore", "Comment added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                // 오류가 발생했을 때 처리
                Log.w("Firestore", "Error adding comment", e)
            }
    }

    fun createUserLike(userId: String, commentId: String, movieTitle: String, moviePoster: String) {
        // Firestore 인스턴스 가져오기
        val db = FirebaseFirestore.getInstance()

        // 댓글 객체 생성
        val userLike = hashMapOf(
            "userId" to userId,
            "commentId" to commentId,
            "movieTitle" to movieTitle,
            "moviePoster" to moviePoster,
        )

//        // 'movies' 컬렉션에서 'movie_id' 문서 내 'comments' 서브컬렉션에 댓글 추가
//        val movieId = movieId  // 특정 영화의 ID (예: Inception, Titanic 등)

        db.collection("userLikes")
            .add(userLike)  // 댓글 추가
            .addOnSuccessListener { documentReference ->
                // 댓글이 성공적으로 추가되었을 때 처리
                Log.d("Firestore", "Comment added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                // 오류가 발생했을 때 처리
                Log.w("Firestore", "Error adding comment", e)
            }
    }

    fun deleteLikeUser(commentId: String, userId: String) { // userId: 로그인돼있는 유저
        val db = FirebaseFirestore.getInstance()

        db.collection("comments")
            .document(commentId)
            .collection("likeUsers")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener {
                    documents ->
                for (document in documents) {
                    db.collection("comments")
                        .document(commentId)
                        .collection("likeUsers")
                        .document(document.id)
                        .delete()
                        .addOnSuccessListener {
                            Log.d("DeleteDocument", "Document successfully deleted!")
                        }
                        .addOnFailureListener { e ->
                            Log.w("DeleteDocument", "Error deleting document", e)
                        }
                }
            }
    }

    fun deleteUserLike(commentId: String, userId: String) { // userId: 로그인돼있는 유저
        val db = FirebaseFirestore.getInstance()

        db.collection("userLikes")
            .whereEqualTo("userId", userId)
            .whereEqualTo("commentId", commentId)
            .get()
            .addOnSuccessListener {
                    documents ->
                for (document in documents) {
                    // 문서 삭제
                    document.reference.delete()
                        .addOnSuccessListener {
                            Log.d("DeleteDoc", "Document successfully deleted!")
                        }
                        .addOnFailureListener { e ->
                            Log.w("DeleteDoc", "Error deleting document", e)
                        }
                }
            }
    }
}