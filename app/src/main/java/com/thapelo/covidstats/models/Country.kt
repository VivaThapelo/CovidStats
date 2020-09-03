package com.thapelo.covidstats.models

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Country(
    var errors: List<Any>,
    var results: Int,
    var parameters: List<Any>,
    @PrimaryKey
    @NonNull
    @SerializedName("response")
    var countries: List<Any>
)