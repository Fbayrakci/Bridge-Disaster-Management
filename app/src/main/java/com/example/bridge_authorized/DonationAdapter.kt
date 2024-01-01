package com.example.bridge_authorized

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlin.system.exitProcess

class DonationAdapter(
    var mList: ArrayList<DonationTypeData>,
    private val onItemClicked: (Class<*>) -> Unit
) : RecyclerView.Adapter<DonationAdapter.DonationTypeViewHolder>() {
    inner class DonationTypeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgDonation: ImageView = itemView.findViewById(R.id.imgSection)
        val typeDonation: TextView = itemView.findViewById(R.id.txtSection)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClicked(mList[position].activityClass)
                }
            }
        }
    }
    fun setFilteredList(mList: ArrayList<DonationTypeData>) {
        this.mList = mList
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DonationTypeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.each_item, parent, false)
        return DonationTypeViewHolder(view)
    }
    override fun onBindViewHolder(holder: DonationTypeViewHolder, position: Int) {
        holder.imgDonation.setImageResource(mList[position].donationImg)
        holder.typeDonation.text = mList[position].donationType
    }
    override fun getItemCount(): Int {
        return mList.size
    }
}