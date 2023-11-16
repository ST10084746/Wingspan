package com.example.wingspan

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.wingspan.Models.BirdInfoItem

class InfoAdapter(
    private val information: ArrayList<BirdInfoItem>,
    private val listener: OnItemClickListener
    ): RecyclerView.Adapter<InfoAdapter.InfoViewHolder>(){

    inner class InfoViewHolder (itemView: View): RecyclerView.ViewHolder(itemView),
    View.OnClickListener{
        var commonName: TextView = itemView.findViewById(R.id.tvCommonName);

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val position: Int = adapterPosition
            listener.onItemClick(position)

        }

    }
    interface OnItemClickListener{
        fun onItemClick(position:Int)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoAdapter.InfoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.info_list_item,parent, false)
        return InfoViewHolder(view);
    }

    override fun onBindViewHolder(holder: InfoAdapter.InfoViewHolder, position: Int) {
        val info = information[position]
        holder.commonName.text = info.comName
    }

    override fun getItemCount(): Int {
        return information.size
    }

}