package com.example.mapthree

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.wingspan.R

class RouteInstructionsAdapter(private val routeInstructions: ArrayList<String>) : RecyclerView.Adapter<RouteInstructionsAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val instructionText: TextView = itemView.findViewById(R.id.stepInstruction)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_direction, parent, false)
        Log.d("CreateViewHolder", "Created")
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentInstruction = routeInstructions[position]
        var currentInstructionPos = position

        if (!currentInstruction.contains("You have reached a waypoint of your trip") &&
            !currentInstruction.startsWith("Step instruction here")) {

            holder.instructionText.text = "$currentInstructionPos. $currentInstruction\n"
        }
    }

    override fun getItemCount() = routeInstructions.size
}