package com.thapelo.covidstats.ui.main.webservices

import com.thapelo.covidstats.ui.main.models.Country
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers

interface CountriesWebservice {
    /**
     * @GET declares an HTTP GET request
     */

    @Headers(
        (
                "x-rapidapi-key: 2eb6b5cc5emsh3096b7a40f24568p15fbeajsnf3a768f7020d"
                )
    )

    @GET("/countries")
    fun getCountries(): Call<List<Country>>
}