package com.example.profilelab.tabs

import TimeAdapter
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.profilelab.AppDB
import com.example.profilelab.R
import com.example.profilelab.entities.Reservation
import com.example.profilelab.view_models.CourtViewModel
import com.example.profilelab.view_models.SportViewModel
import com.example.profilelab.view_models.TimeSlotViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.stacktips.view.CalendarListener
import com.stacktips.view.CustomCalendarView
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


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
    lateinit var db : AppDB
    var selectedDate: String = ""
    var descriptionText: String = ""
    var selectedCourt : Int = -1
    var selectedSport : Int = -1
    var selectedTime : Int = -1
    val courtData = ArrayList<CourtViewModel>()
    val sportData = ArrayList<SportViewModel>()
    val timeSlotData = ArrayList<TimeSlotViewModel>()
    private lateinit var courtSpinner: Spinner
    private lateinit var sportSpinner: Spinner
    private lateinit var timeAdapter: TimeAdapter
    private lateinit var timeRecycler: RecyclerView
    private lateinit var mCalendarView: CustomCalendarView
    private lateinit var descriptionEt : EditText
    private lateinit var courtAdapter: ArrayAdapter<CourtViewModel>
    private lateinit var sportAdapter: ArrayAdapter<SportViewModel>

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
        mCalendarView = viewThis.findViewById(R.id.calendarView)
        val timetableBtn = viewThis.findViewById<Button>(R.id.timesheet_btn)
        db = AppDB.getDatabase(requireContext())

        //spinner
        courtSpinner = viewThis.findViewById(R.id.court_spinner)
        sportSpinner = viewThis.findViewById(R.id.sport_spinner)
        descriptionEt = viewThis.findViewById(R.id.description)

        courtAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, courtData)
        courtSpinner.adapter = courtAdapter
        courtAdapter.setDropDownViewResource(R.layout.spinner_row)
        getCourts()

        descriptionEt.addTextChangedListener(object : TextWatcher {
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

        courtSpinner.onItemSelectedListener = object :
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
        sportSpinner.adapter = sportAdapter
        sportAdapter.setDropDownViewResource(R.layout.spinner_row)


        sportSpinner.onItemSelectedListener = object :
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
        mCalendarView.firstDayOfWeek = Calendar.MONDAY
        mCalendarView.setShowOverflowDate(false)
        mCalendarView.refreshCalendar(currentCalendar)
        mCalendarView.setCalendarListener(object : CalendarListener {
            override fun onDateSelected(date: Date?) {
                val df = SimpleDateFormat("dd-MM-yyyy")
                selectedDate = df.format(date)
//                Toast.makeText(activity, df.format(date), Toast.LENGTH_SHORT).show()
            }

            override fun onMonthChanged(date: Date?) {
//                val df = SimpleDateFormat("MM-yyyy")
//                Toast.makeText(activity, df.format(date), Toast.LENGTH_SHORT).show()
            }
        })

        timetableBtn.setOnClickListener {
            val dialog = BottomSheetDialog(requireContext())
            dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
            val view = inflater.inflate(R.layout.timetable_bottomsheet, container, false)
            val btnClose = view.findViewById<Button>(R.id.time_btn)
            timeRecycler = view.findViewById(R.id.timetable_recycler)
            timeRecycler.layoutManager = LinearLayoutManager(context)
            val clickListener = object : TimeAdapter.OnTimeClickListener {
                override fun onTimeClick(position: Int) {
                    selectedTime = timeSlotData[position].id
//                    Toast.makeText(requireContext(), timeSlotData[position].toString(), Toast.LENGTH_SHORT).show()
                }
            }
            timeAdapter = TimeAdapter(
                requireActivity().applicationContext,
                getTimeTable(),
                clickListener
            )
            timeRecycler.adapter = timeAdapter
            btnClose.setOnClickListener {
                reserve()
                dialog.dismiss()
            }
            dialog.setCancelable(false)
            dialog.setContentView(view)
            dialog.show()
        }

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

    private fun getTimeTable():ArrayList<TimeSlotViewModel>{//(date:String){
        GlobalScope.launch(Dispatchers.IO) {
            val times = db.timeSlotDao().getAll()
            val taken = db.reservationDao().findByDate(selectedDate)
            for (tkn in taken){
                Log.d("taken", tkn.toString())
            }
            requireActivity().runOnUiThread {
                timeSlotData.clear()
                for (time in times) {
                    time.id?.let { TimeSlotViewModel(it, time.start_time, time.end_time) }?.let { timeSlotData.add(it) }
                }
                timeAdapter.notifyDataSetChanged()
            }
        }
        return timeSlotData
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun getCourtSportId(courtId:Int, sportId:Int, callback: (Int) -> Unit){
        GlobalScope.launch(Dispatchers.IO) {
            val courtSportId = db.courtSportDao().getByCourtIdAndSportId(courtId, sportId)
            callback(courtSportId.id)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun reserve() {
        if (selectedCourt == -1 || selectedSport == -1 || selectedTime == -1 || selectedDate == "") {
            Toast.makeText(requireContext(), "Please select all fields", Toast.LENGTH_SHORT).show()
            return
        }
        getCourtSportId(selectedCourt, selectedSport) { courtSportId ->
            GlobalScope.launch(Dispatchers.IO) {
                val reservation = Reservation(
                    id = null,
                    user_id = 1,
                    date_ = selectedDate,
                    court_sports_id = courtSportId,
                    time_slot_id = selectedTime,
                    description = descriptionText,
                    status = 1
                )
                db.reservationDao().insert(reservation)
            }
            requireActivity().runOnUiThread {
                selectedTime = -1
                selectedDate = ""
                selectedCourt = -1
                selectedSport = -1
                descriptionText = ""
                descriptionEt.setText("")
                mCalendarView.refreshCalendar(Calendar.getInstance(Locale.getDefault()))

            }
        }
    }
}