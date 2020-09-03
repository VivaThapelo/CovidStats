package com.thapelo.covidstats.models

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
class Statistics(
    private val errors: List<String>,
    val parameters: List<String>,
    val results: Int,
    @PrimaryKey @NonNull
    @SerializedName("response")
    val statistics: List<Any>
)