package com.example.profilelab.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.profilelab.R
import com.stacktips.view.CalendarListener
import com.stacktips.view.CustomCalendarView
import java.text.SimpleDateFormat
import java.util.*


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CalendarFrag.newInstance] factory method to
 * create an instance of this fragment.
 */
class CalendarFrag : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val viewThis: View = inflater.inflate(R.layout.fragment_calendar, container, false)
        val mCalendarView = viewThis.findViewById<CustomCalendarView>(R.id.calendarView)

        val currentCalendar = Calendar.getInstance(Locale.getDefault())
        mCalendarView.setFirstDayOfWeek(Calendar.MONDAY)
        mCalendarView.setShowOverflowDate(false)
        mCalendarView.refreshCalendar(currentCalendar)
        mCalendarView.setCalendarListener(object : CalendarListener {
            override fun onDateSelected(date: Date?) {
                val df = SimpleDateFormat("dd-MM-yyyy")
                Toast.makeText(activity, df.format(date), Toast.LENGTH_SHORT).show()
            }

            override fun onMonthChanged(date: Date?) {
                val df = SimpleDateFormat("MM-yyyy")
                Toast.makeText(activity, df.format(date), Toast.LENGTH_SHORT).show()
            }
        })
//        val btn = viewThis.findViewById<Button>(R.id.buttonView)
//
//        mCalendarView.minDate = System.currentTimeMillis() - 1000
//
//        mCalendarView.setOnDateChangeListener{ view, year, month, dayOfMonth ->
//            // Note that months are indexed from 0. So, 0 means January, 1 means february, 2 means march etc.
//            val msg = "Selected date is " + dayOfMonth + "/" + (month + 1) + "/" + year
//            Log.d("aaaaa", msg)
//            Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
//        }

        return viewThis
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Calendar.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CalendarFrag().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}