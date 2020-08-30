package com.thapelo.covidstats.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.thapelo.covidstats.R


class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

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

        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            this.requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            COUNTRIES.toList()
        )
        val autocompleteView =
            rootView.findViewById(R.id.autocomplete_textview) as CustomAutoCompleteTextView
        autocompleteView.setAdapter(adapter)

        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        // TODO: Use the ViewModel

    }

}