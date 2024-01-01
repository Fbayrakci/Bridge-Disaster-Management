package com.example.bridge_authorized

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SeeDonationTypeAdapter(
    private val donationList: List<SeeDonationType>,
    private val listener: OnDonationItemClicked
) : RecyclerView.Adapter<SeeDonationTypeAdapter.ViewHolder>() {

    interface OnDonationItemClicked {
        fun onItemReceived(donationType: SeeDonationType)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.donorNameitem)
        val surname: TextView = view.findViewById(R.id.donorSurnameitem)
        val phone: TextView = view.findViewById(R.id.DonorPhoneitem)
        val email: TextView = view.findViewById(R.id.donorEmailitem)
        val category: TextView = view.findViewById(R.id.txtSectionCategoryitem)
        val item: TextView = view.findViewById(R.id.txtSectionUrunitem)
        val quantity: TextView = view.findViewById(R.id.txtSectionAdetitem)
        val receivedButton: Button = view.findViewById(R.id.btnTeslimAldim)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.see_donations_itemm, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val donation = donationList[position]

        holder.receivedButton.setOnClickListener {
            listener.onItemReceived(donation)
        }

        holder.name.text = donation.userDetails["name"] ?: "N/A"
        holder.surname.text = donation.userDetails["surname"] ?: "N/A"
        holder.phone.text = donation.userDetails["phone"] ?: "N/A"
        holder.email.text = donation.userDetails["email"] ?: "N/A"
        holder.category.text = donation.donationItems[0]["category"] ?: "N/A"
        holder.item.text = donation.donationItems[0]["item"] ?: "N/A"
        holder.quantity.text = donation.donationItems[0]["quantity"] ?: "N/A"
    }

    override fun getItemCount(): Int {
        return donationList.size
    }
}
