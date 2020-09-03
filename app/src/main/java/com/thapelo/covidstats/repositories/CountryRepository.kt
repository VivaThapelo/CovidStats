package com.thapelo.covidstats.repositories

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import com.thapelo.covidstats.databases.CountryDao
import com.thapelo.covidstats.models.Country
import com.thapelo.covidstats.webservices.CountryWebservice
import javax.inject.Inject

class CountryRepository
@Inject constructor(
    private val countriesWebservice:
    CountryWebservice, private val countryDao: CountryDao,
    private val appExecutors: AppExecutors
) {
    fun getData(): LiveData<Resource<List<String>>> {
        return object : NetworkBoundResource<List<String>, Country>(appExecutors) {

            @SuppressLint("LogNotTimber")
            override fun saveCallResult(item: Country) {
                Log.d("Store results", item.toString())
                try {
                    countryDao.insertAll(item)
                } catch (e: Exception) {
                    Log.d("ContryRepo", "DB Storing error", e.fillInStackTrace())
                }
            }

            @SuppressLint("LogNotTimber")
            override fun loadFromDb(): LiveData<List<String>> {
                return countryDao.getAll()
            }

            @SuppressLint("LogNotTimber")
            override fun createCall() = countriesWebservice.getCountries()

            override fun shouldFetch(data: List<String>?) = data == null

            @SuppressLint("LogNotTimber")
            override fun onFetchFailed() {
                super.onFetchFailed()
                Log.d("NBR", "Fetch failed")
            }
        }.asLiveData()
    }
}
