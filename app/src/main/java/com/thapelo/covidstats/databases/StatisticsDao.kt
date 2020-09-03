package com.thapelo.covidstats.databases

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.thapelo.covidstats.models.Statistics

@Dao
interface StatisticsDao {

    @Query("SELECT statistics FROM Statistics")
    fun getAll(): LiveData<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(stats: Statistics?)
}