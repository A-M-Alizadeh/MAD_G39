package com.example.profilelab.fragments

import TimeAdapter
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.profilelab.R
import com.example.profilelab.databinding.TimetableBottomsheetBinding
import com.example.profilelab.view_models.ReserveViewModel
import com.example.profilelab.view_models.TimeSlotViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.DelicateCoroutinesApi
import java.util.*


class TimeSlotFragment : BottomSheetDialogFragment() {
    val db = FirebaseFirestore.getInstance()
    private lateinit var binding: TimetableBottomsheetBinding
    var selectedTime : String = null.toString()
    private lateinit var timeAdapter: TimeAdapter
//    lateinit var db : AppDB
    private var selectedDate: String = ""
    var descriptionText: String = ""
    private var selectedCourt : String = ""
    private var selectedSport : String = ""
    private var courtLoc :String = ""
    var lastCheckedPosition = -1
    lateinit var reserveViewModel: ReserveViewModel


    val timeSlotData = ArrayList<TimeSlotViewModel>()

//    private lateinit var taskViewModel: TaskViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (dialog as? BottomSheetDialog)?.behavior?.state = STATE_EXPANDED

//        db = AppDB.getDatabase(requireContext())

        binding.timetableRecycler.layoutManager = LinearLayoutManager(context)
        val clickListener = object : TimeAdapter.OnTimeClickListener {
            override fun onTimeClick(position: Int) {
                Log.e("----->TAG", "onTimeClick: $position - $lastCheckedPosition")
                selectedTime = timeSlotData[position].toString()
                if (lastCheckedPosition != position) {
                    if (lastCheckedPosition == -1) {
                        lastCheckedPosition = position
                        timeSlotData[lastCheckedPosition].checked = true
                    }else {
                        timeSlotData[lastCheckedPosition].checked = false
                        lastCheckedPosition = position
                        timeSlotData[lastCheckedPosition].checked = true
                    }
                    timeAdapter.notifyDataSetChanged()
                }
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
            val bundle = Bundle()
            bundle.putBoolean("reserved", true)
            val myReserve = MyReserve()
            myReserve.arguments = bundle

            activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)?.selectedItemId = R.id.my_reservations
        }

        reserveViewModel = ViewModelProvider(this)[ReserveViewModel::class.java]

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (arguments != null) {
            selectedDate = requireArguments().getString("selectedDate", "")
            descriptionText = requireArguments().getString("descriptionText", "")
            selectedCourt = requireArguments().getString("selectedCourt", "")
            selectedSport = requireArguments().getString("selectedSport", "")
            courtLoc = requireArguments().getString("courtLoc", "")
        }

        binding = TimetableBottomsheetBinding.inflate(inflater,container,false)
        return binding.root
    }

    private fun getTimeTable():ArrayList<TimeSlotViewModel>{//(date:String){
        db.collection("timeslots")
        .get()
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                timeSlotData.clear()
                for (document in task.result) {
                    Log.d(TAG, document.id + " => " + document.data)
                    timeSlotData.add(
                        TimeSlotViewModel(
                            document.id,
                            document.data["startTime"].toString(),
                            document.data["endTime"].toString()
                    ))
                    timeAdapter.notifyDataSetChanged()
                }
            } else {
                Log.w(TAG, "Error getting documents.", task.exception)
            }
        }
//        GlobalScope.launch(Dispatchers.IO) {
//            val times = db.timeSlotDao().getAll()
//            val taken = db.reservationDao().findByDate(selectedDate)
//            for (tkn in taken){
//                Log.d("taken", tkn.toString())
//            }
//            requireActivity().runOnUiThread {
//                timeSlotData.clear()
//                for (time in times) {
//                    time.id?.let { TimeSlotViewModel(it, time.start_time, time.end_time) }?.let { timeSlotData.add(it) }
//                }
//                timeAdapter.notifyDataSetChanged()
//            }
//        }
        return timeSlotData
    }

    private fun reserve(){
        if (
            selectedCourt == "" ||
            selectedSport == "" ||
            selectedTime == ""||
            selectedDate == "" ||
            lastCheckedPosition == -1
        ) {
            Toast.makeText(requireContext(), "Please select all fields", Toast.LENGTH_SHORT).show()
            return
        } else {

            val user = Firebase.auth.currentUser

            if (user != null) {
                var loc = hashMapOf(
                    "lat" to 0,
                    "lng" to 0
                )
                var rsrv = hashMapOf(
                    "user_id" to user.uid,
                    "date" to selectedDate,
                    "court" to selectedCourt,
                    "endTime" to selectedTime.split("-")[1].trim(),
                    "sport" to selectedSport,
                    "startTime" to selectedTime.split("-")[0].trim(),
                    "location" to loc,
                    "description" to descriptionText,
                    "status" to true
                )
                Log.e("rsrv", rsrv.toString() + " " + selectedTime.split(","))

                reserveViewModel.add(rsrv)

                requireActivity().runOnUiThread {
                    selectedTime = ""
                    selectedDate = ""
                    selectedCourt = ""
                    selectedSport = ""
                    descriptionText = ""
                }
            }
            }
    }

//    @OptIn(DelicateCoroutinesApi::class)
//    private fun getCourtSportId(courtId:Int, sportId:Int, callback: (Int) -> Unit){
//        GlobalScope.launch(Dispatchers.IO) {
//            val courtSportId = db.courtSportDao().getByCourtIdAndSportId(courtId, sportId)
//            callback(courtSportId.id)
//        }
//    }
}