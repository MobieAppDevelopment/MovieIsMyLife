package com.example.movieismylife.response

import android.os.Parcelable
import com.example.movieismylife.model.Credits
import com.google.gson.annotations.SerializedName

data class CreditsResponse (
    @SerializedName("id")
    val id: Int,
    @SerializedName("cast")
    val cast: List<Credits>
)