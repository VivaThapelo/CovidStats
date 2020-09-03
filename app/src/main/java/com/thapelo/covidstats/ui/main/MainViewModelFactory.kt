package com.thapelo.covidstats.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.thapelo.covidstats.repositories.CountryRepository

class MainViewModelFactory(private val countriesRepository: CountryRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(countryRepository = this.countriesRepository) as T
        }
        throw IllegalStateException("View Class not Found")
    }

}