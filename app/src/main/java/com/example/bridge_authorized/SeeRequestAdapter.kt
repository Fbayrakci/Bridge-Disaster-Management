import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bridge_authorized.DonationAdapter
import com.example.bridge_authorized.R
import com.example.bridge_authorized.SeeRequestItem
import com.example.bridge_authorized.seerequest_item

class SeeRequestAdapter(private val seereqlist: List<SeeRequestItem>) :
    RecyclerView.Adapter<SeeRequestAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // UserDetails için TextView tanımlamaları
        val name: TextView = view.findViewById(R.id.seerequestname)
        val surname: TextView = view.findViewById(R.id.seerequestsurname)
        val phone: TextView = view.findViewById(R.id.seereqPhoneitem)
        val email: TextView = view.findViewById(R.id.seereqemailitem)


        // DonationItem için TextView tanımlamaları
        val category: TextView = view.findViewById(R.id.seereqcategory)
        val item: TextView = view.findViewById(R.id.seereqitem)
        val quantity: TextView = view.findViewById(R.id.seereqquantity)
        val region: TextView = view.findViewById(R.id.seeregion)
        val center: TextView = view.findViewById(R.id.seereqCenterValue)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.see_request_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: SeeRequestAdapter.ViewHolder, position: Int) {
        holder.name.text = seereqlist[position].autname
        holder.surname.text = seereqlist[position].autsurname
        holder.phone.text = seereqlist[position].autphone
        holder.email.text = seereqlist[position].autemail
        holder.category.text = seereqlist[position].autcategory
        holder.item.text = seereqlist[position].autitem
        holder.quantity.text = seereqlist[position].autquantity
        holder.region.text = seereqlist[position].autregion
        holder.center.text = seereqlist[position].autcocenter
    }

    override fun getItemCount() = seereqlist.size
}
