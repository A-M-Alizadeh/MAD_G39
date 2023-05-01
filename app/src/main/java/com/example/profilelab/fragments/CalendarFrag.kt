package com.example.profilelab.fragments

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.profilelab.AppDB
import com.example.profilelab.R
import com.example.profilelab.databinding.FragmentCalendarBinding
import com.example.profilelab.view_models.CourtViewModel
import com.example.profilelab.view_models.SportViewModel
import com.stacktips.view.CalendarListener
import com.stacktips.view.DayDecorator
import com.stacktips.view.DayView
import com.stacktips.view.utils.CalendarUtils
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class CalendarFrag : Fragment() {

    lateinit var db : AppDB
    var selectedDate: String = ""
    var descriptionText: String = ""
    var selectedCourt : Int = -1
    var selectedSport : Int = -1
    private lateinit var binding: FragmentCalendarBinding

    val courtData = ArrayList<CourtViewModel>()
    val sportData = ArrayList<SportViewModel>()
    private lateinit var courtAdapter: ArrayAdapter<CourtViewModel>
    private lateinit var sportAdapter: ArrayAdapter<SportViewModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCalendarBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = AppDB.getDatabase(requireContext())

        courtAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, courtData)
        binding.courtSpinner?.adapter = courtAdapter
        courtAdapter.setDropDownViewResource(R.layout.spinner_row)
        getCourts()

        binding.description?.addTextChangedListener(object : TextWatcher {
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

        binding.courtSpinner?.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                selectedCourt = courtData[position].id
                getSports(courtData[position].id)
//                Toast.makeText(requireContext(), "" + courtData[position].name+"  :  "+ courtData[position].id, Toast.LENGTH_SHORT).show()
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }
        sportAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, sportData)
        binding.sportSpinner?.adapter = sportAdapter
        sportAdapter.setDropDownViewResource(R.layout.spinner_row)

        binding.sportSpinner?.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                selectedSport = sportData[position].id
//                Toast.makeText(requireContext(), "" + sportData[position] + position, Toast.LENGTH_SHORT).show()
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }
        //end spinner

        val currentCalendar = Calendar.getInstance(Locale.getDefault())
        val decorators = ArrayList<DayDecorator>()
        decorators.add(DisabledColorDecorator())

        binding.calendarView.firstDayOfWeek = Calendar.MONDAY
        binding.calendarView.setShowOverflowDate(false)

        binding.calendarView.setDecorators(decorators)
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
                bundle.putInt("selectedCourt", selectedCourt)
                bundle.putInt("selectedSport", selectedSport)
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

    @OptIn(DelicateCoroutinesApi::class)
    private fun getCourts(): ArrayList<CourtViewModel> {
        GlobalScope.launch(Dispatchers.IO) {
            val courts = db.courtDao().getAll()
            for (court in courts){
                court.id?.let { CourtViewModel(it, court.name) }?.let { courtData.add(it) }
            }
            courtAdapter.notifyDataSetChanged()
        }
        return courtData
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun getSports(courtId:Int){
        GlobalScope.launch(Dispatchers.IO) {
            val sports = db.courtSportDao().getInDetail(courtId)
            requireActivity().runOnUiThread {
                sportData.clear()
                for (sport in sports) {
                    sport.id?.let { SportViewModel(it, sport.title) }?.let { sportData.add(it) }
                }
                sportAdapter.notifyDataSetChanged()
            }
        }
    }
}