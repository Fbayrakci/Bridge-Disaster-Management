package com.example.bridge_authorized

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlin.system.exitProcess

class DonorAnnoucmentAdapter(
    var mListan: ArrayList<DonorAnnoucmentTypeData>,
    private val onItemClicked: (Class<*>) -> Unit
) : RecyclerView.Adapter<DonorAnnoucmentAdapter.DonorAnnoucmentTypeViewHolder>() {
    inner class DonorAnnoucmentTypeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgDonation: ImageView = itemView.findViewById(R.id.imgSection)
        val typeDonation: TextView = itemView.findViewById(R.id.txtSection)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClicked(mListan[position].activityClass)
                }
            }
        }
    }
    fun setFilteredList(mList: ArrayList<DonorAnnoucmentAdapter>) {
        this.mListan = mListan
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DonorAnnoucmentTypeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.each_item, parent, false)
        return DonorAnnoucmentTypeViewHolder(view)
    }
    override fun onBindViewHolder(holder: DonorAnnoucmentTypeViewHolder, position: Int) {
        holder.imgDonation.setImageResource(mListan[position].annoucmentImg)
        holder.typeDonation.text = mListan[position].annoucmentType
    }
    override fun getItemCount(): Int {
        return mListan.size
    }
}