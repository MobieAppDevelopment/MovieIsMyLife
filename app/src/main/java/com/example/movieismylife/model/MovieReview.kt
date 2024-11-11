package com.example.movieismylife.model

data class MovieReview(
    val id: Int, // PK
    val movieTitle: String, // 영화 제목
    val reviewText: String, // 리뷰
    val starRating: Int, // 별점
    val movieImageUrl: String, //영화 이미지
    val writerEmail: String, // 작성자 이메일
    val writerNickName: String, // 작성자 닉네임
)