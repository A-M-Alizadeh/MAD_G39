package com.example.profilelab.fragments

import TimeAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.profilelab.AppDB
import com.example.profilelab.databinding.TimetableBottomsheetBinding
import com.example.profilelab.entities.Reservation
import com.example.profilelab.view_models.TimeSlotViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList


class TimeSlotFragment : BottomSheetDialogFragment() {

    private lateinit var binding: TimetableBottomsheetBinding
    var selectedTime : Int = -1
    private lateinit var timeAdapter: TimeAdapter
    lateinit var db : AppDB
    private var selectedDate: String = ""
    var descriptionText: String = ""
    private var selectedCourt : Int = -1
    private var selectedSport : Int = -1


    val timeSlotData = ArrayList<TimeSlotViewModel>()

//    private lateinit var taskViewModel: TaskViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (dialog as? BottomSheetDialog)?.behavior?.state = STATE_EXPANDED

        db = AppDB.getDatabase(requireContext())

        binding.timetableRecycler.layoutManager = LinearLayoutManager(context)
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
        binding.timetableRecycler.adapter = timeAdapter

        binding.timeBtn.setOnClickListener {
            reserve()
            dismiss()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (arguments != null) {
            selectedDate = requireArguments().getString("selectedDate", "")
            descriptionText = requireArguments().getString("descriptionText", "")
            selectedCourt = requireArguments().getInt("selectedCourt", -1)
            selectedSport = requireArguments().getInt("selectedSport", -1)
        }

        binding = TimetableBottomsheetBinding.inflate(inflater,container,false)
        return binding.root
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
    private fun reserve() {
        if (
            selectedCourt == -1 ||
            selectedSport == -1 ||
            selectedTime == -1 ||
            selectedDate == ""
        ) {
            Toast.makeText(requireContext(), "Please select all fields", Toast.LENGTH_SHORT).show()
            return
        } else {
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
                }
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun getCourtSportId(courtId:Int, sportId:Int, callback: (Int) -> Unit){
        GlobalScope.launch(Dispatchers.IO) {
            val courtSportId = db.courtSportDao().getByCourtIdAndSportId(courtId, sportId)
            callback(courtSportId.id)
        }
    }
}