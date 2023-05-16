package com.example.profilelab.fragments

import com.example.profilelab.ReserveAdapter
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
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.profilelab.R
import com.example.profilelab.view_models.ReserveViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar

class MyReserve : Fragment() {

    private var recyclerAdapter : ReserveAdapter? = null
    private var recyclerView : RecyclerView? = null
    private var swipe : SwipeRefreshLayout? = null
    lateinit var reserveViewModel: ReserveViewModel
    var isLastActionReservation = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (arguments != null)
            isLastActionReservation = requireArguments().getBoolean("reserved", false)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_reserve, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (isLastActionReservation)
            Toast.makeText(activity, "Saved reservation!", Toast.LENGTH_SHORT).show()

        reserveViewModel = ViewModelProvider(this)[ReserveViewModel::class.java]

        recyclerView = getView()?.findViewById(R.id.recyclerview)
        swipe = getView()?.findViewById(R.id.swipe_refresh_layout)

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
        recyclerAdapter = ReserveAdapter(requireActivity().applicationContext)
        recyclerView?.adapter = recyclerAdapter
        setSwipeToDelete()

        swipe?.setOnRefreshListener {
            Log.d("listit", "xaxa")

            reserveViewModel.allFullReservations
            swipe?.isRefreshing = false
        }

        reserveViewModel.allFullReservations.observe(viewLifecycleOwner) { list ->
            list?.let {
                recyclerAdapter!!.updateList(list)
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
                val position = viewHolder.adapterPosition
                val modelClass = recyclerAdapter?.getItemInPosition(position)

                modelClass?.let {
                    val id = modelClass.reservation_id!!

                    reserveViewModel.updateStatus(id, false)

                    Snackbar.make(recyclerView!!, "Item was removed from the list.", Snackbar.LENGTH_LONG)
                        .setAction("UNDO") {
                            reserveViewModel.updateStatus(id, true)
                        }.show()
                }
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