package com.example.movieismylife.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore

class MyPageViewModel : ViewModel() {
    private var writedReviewNum = 0
    // 해당 review들의 리스트도 변수로 가져야 함
    private var writedReplyNum = 0
    // 해당 reply들의 리스트도 변수로 가져야 함
    private val db = FirebaseFirestore.getInstance()

    init {
        // 여기서 db를 활용하여 내 userid에 맞는 위 변수들 다 가져와야 함.
    }

    fun getWritedReviews() {} // 내가 쓴 리뷰 데이터클래스 리스트 반환하는 함수
    fun getWritedReplys() {} // 내가 쓴 댓글 데이터클래스 리스트 반환하는 함수
}