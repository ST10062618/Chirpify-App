package com.authorisation.chirpify

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HotspotAdapter(private var hotspots: List<Hotspot>) : RecyclerView.Adapter<HotspotAdapter.HotspotViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotspotViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_hotspots, parent, false)
        return HotspotViewHolder(view)
    }

    override fun onBindViewHolder(holder: HotspotViewHolder, position: Int) {
        val hotspot = hotspots[position]
        holder.bind(hotspot)
    }

    override fun getItemCount() = hotspots.size

    fun updateHotspots(newHotspots: List<Hotspot>) {
        hotspots = newHotspots
        notifyDataSetChanged() // Notify the adapter to refresh the list
    }

    inner class HotspotViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)

        fun bind(hotspot: Hotspot) {
            nameTextView.text = hotspot.name
            descriptionTextView.text = hotspot.description
        }
    }
}
