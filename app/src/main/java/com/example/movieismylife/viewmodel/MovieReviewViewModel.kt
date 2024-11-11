package com.example.movieismylife.viewmodel

import androidx.lifecycle.ViewModel
import com.example.movieismylife.enum.SortOption
import com.example.movieismylife.model.MovieReview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


class MovieReviewViewModel : ViewModel() {
    private val _reviews = MutableStateFlow<List<MovieReview>>(emptyList())
    val reviews = _reviews.asStateFlow()

    private val _sortOption = MutableStateFlow(SortOption.LATEST)
    val sortOption = _sortOption.asStateFlow()

    // 내가 쓴 리뷰 페이지 인가? 좋아요를 누른 걸 확인하는 리뷰 페이지 인가?
    private val _reviewType = MutableStateFlow("MY REVIEW")
    val reviewType = _reviewType.asStateFlow()

    init {
        // 임시 샘플 데이터 초기화 (실제 로는 API 로 DB 에서 내가 작성한 리뷰 리스트 가져와야 함.)
        _reviews.value = listOf(
            MovieReview(1, "유령 신부", "팀버튼 감독의 독특한 연출력과 아름다운 스톱모션 애니메이션. 로맨스와 공포가 절묘하게 어우러진 걸작", 4, "https://media.themoviedb.org/t/p/w220_and_h330_face/ryYEpxWiZFmJJZUyvQCBvM5UOCU.jpg","test1@cau.ac.kr","testUser1"),
            MovieReview(2, "몽키 킹 3D", "화려한 그래픽과 액션 씬이 돋보이는 작품", 5, "https://media.themoviedb.org/t/p/w220_and_h330_face/pi2pkIUeVk0z3iy67YKTxDjeAGr.jpg","test2@cau.ac.kr","testUser2"),
            MovieReview(3, "포레스트 검프", "톰 행크스의 연기가 돋보이는 감동적인 인생 드라마", 2, "https://media.themoviedb.org/t/p/w220_and_h330_face/pi2pkIUeVk0z3iy67YKTxDjeAGr.jpg","test3@cau.ac.kr","testUser3"),
            MovieReview(4, "블루문", "아름다운 영상미와 감동적인 스토리", 1, "https://media.themoviedb.org/t/p/w220_and_h330_face/pi2pkIUeVk0z3iy67YKTxDjeAGr.jpg","test4@cau.ac.kr","testUser4"),
            MovieReview(5, "포레스트 검프2", "톰 행크스의 연기가 돋보이는 감동적인 인생 드라마. good", 3, "https://media.themoviedb.org/t/p/w220_and_h330_face/pi2pkIUeVk0z3iy67YKTxDjeAGr.jpg","test5@cau.ac.kr","testUser5"),
        )
        sortReviews()
    }

    //정렬 기준 설정
    fun setSortOption(option: SortOption) {
        _sortOption.value = option
        sortReviews()
    }

    // 정렬 수행
    private fun sortReviews() {
        _reviews.update { reviews ->
            when (_sortOption.value) {
                SortOption.LATEST -> reviews.sortedByDescending { it.id } // 최신순 정렬
                SortOption.RATING -> reviews.sortedByDescending { it.starRating } // 별점순 정렬
            }
        }
    }
}