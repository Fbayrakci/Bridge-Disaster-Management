import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bridge_authorized.R
import com.example.bridge_authorized.SeeDonationType

class SeeDonationTypeAdapter(
    private val donationList: List<SeeDonationType>,
    private val onDonationButtonClicked: (SeeDonationType, ViewGroup, Map<Pair<String, String>, Int>) -> Unit
) : RecyclerView.Adapter<SeeDonationTypeAdapter.ViewHolder>()  {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.donorNameitem)
        val surname: TextView = view.findViewById(R.id.donorSurnameitem)
        val phone: TextView = view.findViewById(R.id.DonorPhoneitem)
        val email: TextView = view.findViewById(R.id.donorEmailitem)
        val btnTeslimAldim: Button = view.findViewById(R.id.btnTeslimAldim)
        val donationItemsContainer: ViewGroup = view.findViewById(R.id.donationItemsContainer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutResId = if (viewType == 1) R.layout.see_donations_itemm else R.layout.authorizeddonations
        val view = LayoutInflater.from(parent.context).inflate(layoutResId, parent, false)
        return ViewHolder(view)
    }

    override fun getItemViewType(position: Int): Int {
        return if (donationList[position].isOrdinary) 1 else 2
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val donation = donationList[position]

        holder.name.text = donation.userDetails["name"] ?: "N/A"
        holder.surname.text = donation.userDetails["surname"] ?: "N/A"
        holder.phone.text = donation.userDetails["phone"] ?: "N/A"
        holder.email.text = donation.userDetails["email"] ?: "N/A"

        holder.donationItemsContainer.removeAllViews()
        val layoutResId = if (donation.isOrdinary) R.layout.see_donations_item_db else R.layout.authorizeddonations_item_db
        donation.donationItems.forEach { item ->
            val itemView = LayoutInflater.from(holder.itemView.context).inflate(layoutResId, holder.donationItemsContainer, false)
            val category: TextView = itemView.findViewById(R.id.txtSectionCategory)
            val itemTextView: TextView = itemView.findViewById(R.id.txtSectionUrun)
            val quantity: TextView = itemView.findViewById(R.id.txtSectionAdet)

            category.text = item["category"] ?: "N/A"
            itemTextView.text = item["item"] ?: "N/A"
            quantity.text = item["quantity"] ?: "N/A"

            holder.donationItemsContainer.addView(itemView)
        }

        holder.btnTeslimAldim.setOnClickListener {
            val aggregatedQuantities = mutableMapOf<Pair<String, String>, Int>()

            donation.donationItems.forEach { item ->
                val category = item["category"] ?: "N/A"
                val itemName = item["item"] ?: "N/A"
                val quantity = item["quantity"]?.toIntOrNull() ?: 0

                val key = Pair(category, itemName)
                aggregatedQuantities[key] = aggregatedQuantities.getOrDefault(key, 0) + quantity
            }

            holder.btnTeslimAldim.isEnabled = false
            holder.btnTeslimAldim.text = "In Storage"
            holder.btnTeslimAldim.setBackgroundColor(holder.itemView.context.resources.getColor(R.color.black))
            holder.btnTeslimAldim.setTextColor(holder.itemView.context.resources.getColor(R.color.white))

            onDonationButtonClicked(donation, holder.donationItemsContainer, aggregatedQuantities)
        }

        if (donation.isreceived) {
            holder.itemView.visibility = View.GONE
        } else {
            holder.itemView.visibility = View.VISIBLE
        }
    }

    override fun getItemCount() = donationList.size
}