
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.profilelab.R
import com.example.profilelab.view_models.ReserveViewModel

class ReserveAdapter(context: Context, mList: ArrayList <ReserveViewModel>) : RecyclerView.Adapter<ReserveAdapter.ViewHolder>() {


    private val context: Context
    private val data : ArrayList<ReserveViewModel>
    init {
        this.context = context
        this.data = mList
    }

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        var view = LayoutInflater.from(context)
                .inflate(R.layout.row_item_reserve, parent, false)
        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val itemsViewModel = data[position]

        if (!itemsViewModel.status) {
            holder.container.setBackgroundColor(ContextCompat.getColor(context, R.color.light_red))
        }

        // sets the text to the textview from our itemHolder class
        holder.tv_date.text = itemsViewModel.date
        holder.tv_court.text = itemsViewModel.Court
        holder.tv_sport.text = itemsViewModel.sport
        holder.tv_start.text = itemsViewModel.start
        holder.tv_end.text = itemsViewModel.end
        holder.tv_description.text = itemsViewModel.description

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return data.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val container :LinearLayout = itemView.findViewById(R.id.container_layout)
        val tv_date: TextView = itemView.findViewById(R.id.tv_date)
        val tv_court: TextView = itemView.findViewById(R.id.tv_court_name)
        val tv_sport: TextView = itemView.findViewById(R.id.tv_sport_name)
        val tv_start: TextView = itemView.findViewById(R.id.tv_time_start)
        val tv_end: TextView = itemView.findViewById(R.id.tv_time_end)
        val tv_description: TextView = itemView.findViewById(R.id.tv_description)

    }
}