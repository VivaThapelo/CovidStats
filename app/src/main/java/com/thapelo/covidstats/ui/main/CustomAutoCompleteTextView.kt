package com.thapelo.covidstats.ui.main

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.widget.AutoCompleteTextView

@SuppressLint("AppCompatCustomView")
class CustomAutoCompleteTextView(context: Context?, attributeSet: AttributeSet) :
    AutoCompleteTextView(
        context!!,
        attributeSet
    ) {
    // TextView length is always enough to Filter and showSuggestions
    override fun enoughToFilter(): Boolean {
        return true
    }

    // on Focus, showSuggestions
    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
        if (focused && filter != null) {
            performFiltering(text, 0)
        }
    }
}