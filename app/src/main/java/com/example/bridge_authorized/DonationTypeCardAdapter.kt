package com.example.bridge_authorized

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DonationTypeCardAdapter(var mListCard: ArrayList<DonationTypeCard>,
                              private val onDeleteClicked: (Int) -> Unit,
                              private val onDeleteFromSharedPreferences: (Int) -> Unit
) : RecyclerView.Adapter<DonationTypeCardAdapter.DonationTypeCardViewHolder>()  {

    inner class DonationTypeCardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgDonation: ImageView = itemView.findViewById(R.id.imgSectionCard)
        val categoryDonation: TextView = itemView.findViewById(R.id.txtSectionCategory)
        val txtUrun: TextView = itemView.findViewById(R.id.txtSectionUrun)
        val txtAdetBos: TextView = itemView.findViewById(R.id.txtAdetBos)
        val txtAdet: TextView = itemView.findViewById(R.id.txtSectionAdet)
        val deleteIcon: ImageView = itemView.findViewById(R.id.delete_icon)

        init {
            deleteIcon.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    Log.d("DeleteIcon", "Tıklama pozisyonu: $adapterPosition")
                    onDeleteClicked(adapterPosition)
                    onDeleteFromSharedPreferences(adapterPosition)
                } else {
                    Log.d("DeleteIcon", "Geçersiz pozisyon")
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DonationTypeCardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sepet_item, parent, false)
        return DonationTypeCardViewHolder(view)
    }

    override fun onBindViewHolder(holder: DonationTypeCardViewHolder, position: Int) {
        val currentItem = mListCard[position]
        holder.imgDonation.setImageResource(currentItem.imgCategory)
        holder.categoryDonation.text = currentItem.txtCategory
        holder.txtUrun.text = currentItem.txtUrun
        holder.txtAdetBos.text = currentItem.txtAdetBos
        holder.txtAdet.text = currentItem.txtAdet
    }

    override fun getItemCount(): Int {
        return mListCard.size
    }
}

