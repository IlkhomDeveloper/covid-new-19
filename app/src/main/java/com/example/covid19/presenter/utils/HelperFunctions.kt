package com.example.covid19.presenter.utils

import android.app.DatePickerDialog
import android.content.Context
import android.widget.TextView
import java.util.*

object HelperFunctions {

    fun showDateDialog(context: Context, editText: TextView) {
        val c = Calendar.getInstance()
        val year = c[Calendar.YEAR]
        val month = c[Calendar.MONTH]
        val day = c[Calendar.DAY_OF_MONTH]
        val dpd = DatePickerDialog(context, { _, y, m, d ->
            var textTime = ""
            textTime = if (m < 9 && d < 10) {
                "$y-0${m + 1}-0$d"
            } else if (m < 9 && d >= 10) {
                "$y-0${m + 1}-$d"
            } else if (m >= 9 && d < 10) {
                "$y-${m + 1}-0$d"
            } else {
                "$y-${m + 1}-$d"
            }

            editText.text = textTime

        }, year, month, day)
        dpd.show()
    }
}