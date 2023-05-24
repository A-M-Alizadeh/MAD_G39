package com.example.profilelab.fragments

import android.content.ContentValues.TAG
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.profilelab.R
import com.example.profilelab.databinding.FragmentCalendarBinding
import com.example.profilelab.view_models.FireCourt
import com.example.profilelab.view_models.FireSportVM
import com.google.firebase.firestore.FirebaseFirestore
import com.stacktips.view.CalendarListener
import com.stacktips.view.DayDecorator
import com.stacktips.view.DayView
import com.stacktips.view.utils.CalendarUtils
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap


class CalendarFrag : Fragment() {

    val db = FirebaseFirestore.getInstance()
    var selectedDate: String = ""
    var descriptionText: String = ""
    var selectedCourt : String = null.toString()
    var selectedSport : String = null.toString()
    var courtLoc :String = null.toString()
    var courtName :String = null.toString()
    private lateinit var binding: FragmentCalendarBinding

    private val courtData = ArrayList<FireCourt>()
    private val sportData = ArrayList<FireSportVM>()
    private lateinit var courtAdapter: ArrayAdapter<FireCourt>
    private lateinit var sportAdapter: ArrayAdapter<FireSportVM>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCalendarBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getCourts()
        courtAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, courtData)
        binding.courtSpinner.adapter = courtAdapter
        courtAdapter.setDropDownViewResource(R.layout.spinner_row)

        binding.courtSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                selectedCourt = courtData[position].id
                courtName = courtData[position].name
                courtLoc = courtData[position].location["lat"].toString() + "," + courtData[position].location["lon"].toString()
                getSports(selectedCourt)
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

        sportAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, sportData)
        binding.sportSpinner.adapter = sportAdapter
        sportAdapter.setDropDownViewResource(R.layout.spinner_row)
        binding.sportSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                selectedSport = sportData[position].name
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }
        //end spinner

        binding.description.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                descriptionText = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
            }
        })

        val currentCalendar = Calendar.getInstance(Locale.getDefault())
        val decorators = ArrayList<DayDecorator>()
        decorators.add(DisabledColorDecorator())

        binding.calendarView.firstDayOfWeek = Calendar.MONDAY
        binding.calendarView.setShowOverflowDate(false)

        binding.calendarView.decorators = decorators
        binding.calendarView.refreshCalendar(currentCalendar)
        binding.calendarView.setCalendarListener(object : CalendarListener {
            override fun onDateSelected(date: Date?) {
                val df = SimpleDateFormat("dd-MM-yyyy")

                val today = Date()

                val todayFormat = df.format(today)
                val selectedDateFormat = df.format(date)

                if ( today.before(date) or todayFormat.equals(selectedDateFormat) )
                    selectedDate = selectedDateFormat
                else {
                    selectedDate = ""
                    Toast.makeText(activity, "Chosen date is not valid", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onMonthChanged(date: Date?) {
//                val df = SimpleDateFormat("MM-yyyy")
//                Toast.makeText(activity, df.format(date), Toast.LENGTH_SHORT).show()
            }
        })

        binding.timesheetBtn.setOnClickListener {
            val timeSlotFragment = TimeSlotFragment()
            val bundle = Bundle()

            if (selectedDate.isNotEmpty()) {
                bundle.putString("selectedDate", selectedDate)
                bundle.putString("descriptionText", descriptionText)
                bundle.putString("selectedCourt", courtName)
                bundle.putString("courtLoc", courtLoc)
                bundle.putString("selectedSport", selectedSport)
                timeSlotFragment.arguments = bundle
                timeSlotFragment.show(parentFragmentManager, "timeSlotSelect")
            } else
                Toast.makeText(activity, "Date has not chosen yet!", Toast.LENGTH_SHORT).show()
        }
    }

    private class DisabledColorDecorator : DayDecorator {
        override fun decorate(dayView: DayView) {
            if (CalendarUtils.isPastDay(dayView.date)) {
                val color: Int = Color.parseColor("#a9afb9")
                dayView.setBackgroundColor(color)
            }
        }
    }

    private fun getCourts(){
        db.collection("courts")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result) {
                        document.data["location"]?.let {
                            document.data["sports"]?.let { it1 ->
                                courtData.add(
                                    FireCourt(
                                        document.id,
                                        document.data["name"].toString(),
                                        it as HashMap<String, Double>,
                                        it1 as HashMap<String, String>,
                                        document.data["address"].toString()
                                    )
                                )
                            }
                        }
                            }
                    courtAdapter.notifyDataSetChanged()
                } else {
                    Log.w(TAG, "------> Error getting documents.", task.exception)
                }
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getSports(courtId:String){
        Log.e(TAG, "------> Called getSports")
        for (court in courtData){
            if (court.id == courtId){
                sportData.clear()
                for (sport in court.sports){
                    sportData.add(FireSportVM("1", sport.value))
                }
                sportAdapter.notifyDataSetChanged()
            }
        }
    }
}