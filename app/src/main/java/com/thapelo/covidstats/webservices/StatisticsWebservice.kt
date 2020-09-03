package com.thapelo.covidstats.webservices

import androidx.lifecycle.LiveData
import com.thapelo.covidstats.models.Statistics
import com.thapelo.covidstats.repositories.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Headers

interface StatisticsWebservice {
    /**
     * @GET declares an HTTP GET request
     */

    @Headers(
        ("x-rapidapi-key: 2eb6b5cc5emsh3096b7a40f24568p15fbeajsnf3a768f7020d"),
        ("x-rapidapi-host: covid-193.p.rapidapi.com")
    )


    @GET("statistics")
    fun getStatistics(): LiveData<ApiResponse<Statistics>>
}