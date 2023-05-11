
import android.content.Context
import android.provider.MediaStore.Audio.Radio
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.profilelab.R
import com.example.profilelab.view_models.TimeSlotViewModel

class TimeAdapter(context: Context, mList: ArrayList <TimeSlotViewModel>, private var listener: OnTimeClickListener): RecyclerView.Adapter<TimeAdapter.ViewHolder>() {


    private val context: Context
    val data : ArrayList<TimeSlotViewModel>
    init {
        this.context = context
        this.data = mList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.time_table_row_item, parent, false)
        return ViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemsViewModel = data[position]
        holder.start.text = itemsViewModel.startTime
        holder.end.text = itemsViewModel.endTime

        holder.radio.isChecked = itemsViewModel.checked;
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(ItemView: View,private val listener: OnTimeClickListener) : RecyclerView.ViewHolder(ItemView), View.OnClickListener {

        var start: TextView = itemView.findViewById(R.id.start_time_tv)
        var end: TextView = itemView.findViewById(R.id.end_time_tv)
        var radio : RadioButton = itemView.findViewById(R.id.radio_btn)

        init {
            radio.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            listener.onTimeClick(bindingAdapterPosition)
//            Log.e("----->TAG", "radioooo onClick: $adapterPosition")
        }


    }
    interface OnTimeClickListener {
        fun onTimeClick(position: Int)
    }


}