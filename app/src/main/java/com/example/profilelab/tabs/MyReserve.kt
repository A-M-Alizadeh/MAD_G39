package com.example.profilelab.tabs

import ReserveAdapter
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.profilelab.AppDB
import com.example.profilelab.R
import com.example.profilelab.databinding.ActivityMainBinding
import com.example.profilelab.databinding.FragmentMyReserveBinding
import com.example.profilelab.view_models.ReserveViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MyReserve.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyReserve : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var recyclerData = ArrayList<ReserveViewModel>()
    private var recyclerAdapter : ReserveAdapter? = null
    private var recyclerView : RecyclerView? = null
    private var swipe : SwipeRefreshLayout? = null
    lateinit var db : AppDB

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
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_reserve, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = AppDB.getDatabase(requireContext())

        recyclerView = getView()?.findViewById<RecyclerView>(R.id.recyclerview)
        val emptyView = getView()?.findViewById<View>(R.id.empty_view)
        swipe = getView()?.findViewById<SwipeRefreshLayout>(R.id.swipe_refresh_layout)

        recyclerView?.layoutManager = LinearLayoutManager(context)
        recyclerView?.itemAnimator = DefaultItemAnimator()
//        recyclerView?.addItemDecoration(
//            DividerItemDecoration(
//                context,
//                DividerItemDecoration.VERTICAL,
//            )
//        )

//        if (getDate().isEmpty()) {
//            recyclerView?.visibility = View.GONE
//            emptyView?.visibility = View.VISIBLE
//        } else {
//            recyclerView?.visibility = View.VISIBLE
//            emptyView?.visibility = View.GONE
//        }
        recyclerAdapter = ReserveAdapter(requireActivity().applicationContext, getData(){
            recyclerAdapter?.notifyDataSetChanged()
        })
        recyclerView?.adapter = recyclerAdapter
        setSwipeToDelete()

        swipe?.setOnRefreshListener {
            getData(){
                recyclerAdapter?.notifyDataSetChanged()
            }
            swipe?.isRefreshing = false
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MyReserve.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MyReserve().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun setSwipeToDelete() {
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val modelClass = recyclerData[viewHolder.adapterPosition]
                val position = viewHolder.adapterPosition
                recyclerData.removeAt(position)
                changeStatus(modelClass.id, false){
                    recyclerAdapter?.notifyItemRemoved(position)
                }
                Snackbar.make(recyclerView!!, "Item was removed from the list.", Snackbar.LENGTH_LONG)
                    .setAction("UNDO") {
                        recyclerData.add(position, modelClass)
                        changeStatus(modelClass.id, true){
                            recyclerAdapter?.notifyItemInserted(position)
                        }
                    }.show()
            }

            override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
                return 1f
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                setDeleteIcon(c, viewHolder, dX, dY, isCurrentlyActive)
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }

        }).attachToRecyclerView(recyclerView)
    }

    private fun getData(callback: () -> Unit) : ArrayList<ReserveViewModel>{
        recyclerData.clear()
        GlobalScope.launch(Dispatchers.IO) {
            for (rsrv in db.reservationDao().getInDetails()) {
                Log.e("RESERVATION------>", rsrv.toString())
                rsrv.id?.let {
                    ReserveViewModel(
                        it,
                        rsrv.name,
                        rsrv.title,
                        rsrv.start_time,
                        rsrv.end_time,
                        rsrv.date_,
                        rsrv.status,
                        rsrv.description)
                }?.let { recyclerData.add(it) }
                requireActivity().runOnUiThread(callback)
            }
        }
        return recyclerData
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun changeStatus(id: Int, status: Boolean, callback: () -> Unit = {}) {
        GlobalScope.launch(Dispatchers.IO) {
            db.reservationDao().update(id, status)
            requireActivity().runOnUiThread(callback)
        }
    }

    private fun setDeleteIcon(
        c: Canvas,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        isCureentlyActive: Boolean,
    ){
        val mClearPaint = Paint()
        mClearPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        val mBackground = ColorDrawable()
        val backgroundColor = Color.parseColor("#EF5350")
        val deleteDrawable = requireContext().getDrawable(R.drawable.ic_delete)
        val intrinsicWidth = deleteDrawable?.intrinsicWidth
        val intrinsicHeight = deleteDrawable?.intrinsicHeight
        val itemView = viewHolder.itemView
        val itemHeight = itemView.height
        val isCancelled = dX == 0f && !isCureentlyActive
        if (isCancelled) {
            c.drawRect(
                itemView.right + dX,
                itemView.top.toFloat(),
                itemView.right.toFloat(),
                itemView.bottom.toFloat(),
                mClearPaint
            )
            return
        }
        mBackground.color = backgroundColor
        mBackground.setBounds(
            itemView.right + dX.toInt(),
            itemView.top,
            itemView.right,
            itemView.bottom
        )
        mBackground.draw(c)
        val deleteIconTop = itemView.top + (itemHeight - intrinsicHeight!!) / 2
        val deleteIconMargin = (itemHeight  - intrinsicHeight) / 2
        val deleteIconLeft = itemView.right - deleteIconMargin - intrinsicWidth!!
        val deleteIconRight = itemView.right - deleteIconMargin
        val deleteIconBottom = deleteIconTop + intrinsicHeight
        deleteDrawable.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom)
        deleteDrawable.draw(c)

    }

}