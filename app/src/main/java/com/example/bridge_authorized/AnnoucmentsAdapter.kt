package com.example.bridge_authorized

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class AnnoucmentsAdapter(
    var mList: ArrayList<AnnoucmentsTypeData>,
    private val onItemClicked: (Int) -> Unit
) : RecyclerView.Adapter<AnnoucmentsAdapter.AnnoucmentsTypeViewHolder>() {

    inner class AnnoucmentsTypeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgAnnoucment: ImageView = itemView.findViewById(R.id.imgSection)
        val typeAnnoucment: TextView = itemView.findViewById(R.id.txtSection)
        val typeHeader: TextView = itemView.findViewById(R.id.txtTitle)
        val typeContent: TextView = itemView.findViewById(R.id.txtContent)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClicked(position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnnoucmentsTypeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.each_item_annoucments, parent, false)
        return AnnoucmentsTypeViewHolder(view)
    }

    override fun onBindViewHolder(holder: AnnoucmentsTypeViewHolder, position: Int) {
        val annoucment = mList[position]
        holder.typeAnnoucment.text = annoucment.annoucmentsType
        holder.typeHeader.text = annoucment.annoucmentsCategory
        holder.typeContent.text = annoucment.anooucmentdescription
        Picasso.get().load(mList.get(position).annoucmentsImg).into(holder.imgAnnoucment)

    }

    override fun getItemCount(): Int {
        return mList.size
    }
}
