package com.example.criminalintent

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import java.util.*

private const val ARG_DATE = "date"


class TimePickerFragment : DialogFragment() {

    interface Callbacks {
        fun onTimeSelected(date: Date)

    }



    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val timeListener = TimePickerDialog.OnTimeSetListener {
            _: TimePicker, hour : Int, minute : Int ->

            val date = arguments?.getSerializable(ARG_DATE) as Date
            val calendar = Calendar.getInstance().apply {
                time = date
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
            }

            targetFragment?.let { fragment ->
                (fragment as Callbacks).onTimeSelected(calendar.time)
            }


        }

        val date = arguments?.getSerializable(ARG_DATE) as Date
        val calendar = Calendar.getInstance()
        calendar.time = date

        val initialHour = calendar.get(Calendar.HOUR_OF_DAY)
        val initialMinute = calendar.get(Calendar.MINUTE)

        return TimePickerDialog(requireContext(),
            timeListener,
            initialHour,
            initialMinute,
            true)
    }


    companion object {
        fun newInstance(date: Date) : TimePickerFragment {
            val args = Bundle().apply {
                putSerializable(ARG_DATE, date)

            }
            return TimePickerFragment().apply {
                arguments = args
            }

        }
    }

}