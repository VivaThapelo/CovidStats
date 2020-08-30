package com.thapelo.covidstats.ui.main.webservices

import com.thapelo.covidstats.ui.main.models.Country
import retrofit2.Call
import retrofit2.http.GET

interface CountriesWebservice {
    /**
     * @GET declares an HTTP GET request
     */
    @GET("/countries")
    fun getCountries(): Call<List<Country>>
}