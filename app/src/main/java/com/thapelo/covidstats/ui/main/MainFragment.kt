package com.thapelo.covidstats.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.thapelo.covidstats.R
import com.thapelo.covidstats.databases.AppDatabase
import com.thapelo.covidstats.repositories.AppExecutors
import com.thapelo.covidstats.repositories.CountryRepository
import com.thapelo.covidstats.webservices.CountryWebservice
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject


class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var countryAdapter: ArrayAdapter<String>

    @Inject
    lateinit var appExecutors: AppExecutors

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        val rootView: View = inflater.inflate(R.layout.main_fragment, container, false)

        val COUNTRIES = listOf<String>(
            "All Countries",
            "Belgium",
            "France",
            "Italy",
            "Germany",
            "Spain",
            "South Africa",
            "Canada",
            "Austria",
            "Denmark",
            "Chile",
            "Brazil",
            "Ghana",
            "Gambia"
        )

        countryAdapter = ArrayAdapter<String>(
            this.requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            COUNTRIES.toList()
        )
        val autocompleteView =
            rootView.findViewById(R.id.autocomplete_textview) as CustomAutoCompleteTextView
        autocompleteView.setAdapter(countryAdapter)

        autocompleteView.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                getStatsData(autocompleteView.text.toString())
                true
            } else {
                false
            }
        }



        return rootView
    }

    fun getStatsData(country: String) {

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val retrofit = Retrofit.Builder()
            .baseUrl("https://covid-193.p.rapidapi.com/")
            .addCallAdapterFactory(com.thapelo.covidstats.repositories.LiveDataCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val countryWebservice: CountryWebservice = retrofit.create(CountryWebservice::class.java)
        var db = AppDatabase.getInstance(requireActivity().applicationContext)
        appExecutors = AppExecutors()
        val countriesRepository = CountryRepository(
            countriesWebservice = countryWebservice,
            countryDao = db!!.countryDao(),
            appExecutors = this.appExecutors
        )
        val mainViewModelFactory = MainViewModelFactory(countriesRepository = countriesRepository)
        viewModel = ViewModelProvider(this, mainViewModelFactory).get(MainViewModel::class.java)
        viewModel.countries.observe(this.viewLifecycleOwner) { countries ->
            if (!countries?.data.isNullOrEmpty()) {
                countryAdapter.clear()
                var list = listOf<String>()
                // Log.d("String Op",countries?.data!!.toString())
                list = (countries?.data!![0].substring(1, countries.data[0].length - 1)).replace(
                    "\"",
                    "",
                    true
                ).split(",")
                /* for (country in countries?.data!!) {
                      list += country
                  }*/
                countryAdapter.addAll(list)
                //Log.d("Data", countries?.data!!.toString())
                countryAdapter.notifyDataSetChanged()
            } else {
                Log.d("Countries", "isNull")
            }
        }
    }

}