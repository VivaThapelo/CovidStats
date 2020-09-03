package com.thapelo.covidstats.repositories

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import com.thapelo.covidstats.databases.StatisticsDao
import com.thapelo.covidstats.models.Statistics
import com.thapelo.covidstats.webservices.StatisticsWebservice
import javax.inject.Inject

class StatisticsRepository @Inject constructor(
    private val statisticsWebservice: StatisticsWebservice,
    private val statisticsDao: StatisticsDao,
    private val appExecutors: AppExecutors
) {
    fun getStatistics(statistics: String): LiveData<Resource<List<String>>> {
        return object : NetworkBoundResource<List<String>, Statistics>(appExecutors) {

            @SuppressLint("LogNotTimber")
            override fun saveCallResult(item: Statistics) {
                Log.d("Store results", item.toString())
                try {
                    statisticsDao.insertAll(item)
                } catch (e: Exception) {
                    Log.d("StatisticsRepo", "DB Storing error", e.fillInStackTrace())
                }
            }

            @SuppressLint("LogNotTimber")
            override fun loadFromDb(): LiveData<List<String>> {
                return statisticsDao.getAll()
            }

            @SuppressLint("LogNotTimber")
            override fun createCall() = statisticsWebservice.getStatistics()

            override fun shouldFetch(data: List<String>?) = data == null

            @SuppressLint("LogNotTimber")
            override fun onFetchFailed() {
                super.onFetchFailed()
                Log.d("NBR", "Fetch failed")
            }
        }.asLiveData()
    }
}