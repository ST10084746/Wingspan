package com.example.wingspan

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.wingspan.Models.Observation
import com.example.wingspan.Models.ObservationItem

class SightingAdapter(private val observations: ArrayList<ObservationItem>): RecyclerView.Adapter<SightingAdapter.SightingViewHolder>() {
    inner class SightingViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var speciesCode: TextView = itemView.findViewById(R.id.tvSpecCode)
        var sciName: TextView = itemView.findViewById(R.id.tvSciName)
        var comName: TextView = itemView.findViewById(R.id.tvComName)

    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SightingAdapter.SightingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_view,parent, false)
        return SightingViewHolder(view);

    }

    override fun onBindViewHolder(holder: SightingAdapter.SightingViewHolder, position: Int) {
        val ob = observations[position]
        holder.speciesCode.text = ob.speciesCode
        holder.sciName.text = ob.sciName
        holder.comName.text = ob.comName
    }

    override fun getItemCount(): Int {
        return observations.size
    }
}