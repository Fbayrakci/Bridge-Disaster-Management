import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bridge_authorized.R
import com.example.bridge_authorized.SeeRequestItem

class SeeRequestAdapter(
    var seereqlist: List<SeeRequestItem>,
    private val onRequestSent: (SeeRequestItem, position: Int) -> Unit
) : RecyclerView.Adapter<SeeRequestAdapter.ViewHolder>() {

    fun updateItem(position: Int) {
        notifyItemChanged(position)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.seerequestname)
        val surname: TextView = view.findViewById(R.id.seerequestsurname)
        val phone: TextView = view.findViewById(R.id.seereqPhoneitem)
        val email: TextView = view.findViewById(R.id.seereqemailitem)
        val category: TextView = view.findViewById(R.id.seereqcategory)
        val item: TextView = view.findViewById(R.id.seereqitem)
        val quantity: TextView = view.findViewById(R.id.seereqquantity)
        val region: TextView = view.findViewById(R.id.seeregion)
        val center: TextView = view.findViewById(R.id.seereqCenterValue)
        val btnSendRequest: Button = view.findViewById(R.id.btnSendRequest)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.see_request_item, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: SeeRequestAdapter.ViewHolder, position: Int) {
        val currentRequest = seereqlist[position]

        if (currentRequest.isSended) {
            // If isSended is true, hide the itemView
            holder.itemView.visibility = View.GONE
            holder.itemView.layoutParams = RecyclerView.LayoutParams(0, 0)
        } else {
            // If isSended is false, show the itemView
            holder.itemView.visibility = View.VISIBLE
            holder.itemView.layoutParams = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            // Populate other views
            holder.name.text = currentRequest.autname
            holder.surname.text = currentRequest.autsurname
            holder.phone.text = currentRequest.autphone
            holder.email.text = currentRequest.autemail
            holder.category.text = currentRequest.autcategory
            holder.item.text = currentRequest.autitem
            holder.quantity.text = currentRequest.autquantity
            holder.region.text = currentRequest.autregion
            holder.center.text = currentRequest.autcocenter

            // Send Request button click listener
            holder.btnSendRequest.setOnClickListener {
                onRequestSent(currentRequest, position)
            }
        }
    }






    override fun getItemCount() = seereqlist.size
}