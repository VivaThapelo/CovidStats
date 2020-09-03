package com.thapelo.covidstats.ui.main

import androidx.lifecycle.ViewModel
import com.thapelo.covidstats.repositories.CountryRepository
import javax.inject.Inject

class MainViewModel @Inject constructor(private val countryRepository: CountryRepository) :
    ViewModel() {
    val countries = countryRepository.getData()

}