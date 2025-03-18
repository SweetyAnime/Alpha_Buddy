package com.nk.alphabuddy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class EventAdapter(private val events: List<Event>) :
    RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.event_item, parent, false) // Make sure `event_item.xml` exists
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]
        holder.bind(event)
    }

    override fun getItemCount(): Int = events.size

    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val eventImage: ImageView = itemView.findViewById(R.id.eventImage)
        private val eventName: TextView = itemView.findViewById(R.id.eventName)
        private val eventDate: TextView = itemView.findViewById(R.id.eventDate)
        private val daysRemaining: TextView = itemView.findViewById(R.id.daysRemaining)

        fun bind(event: Event) {
            eventName.text = event.name
            eventDate.text = "Date: ${event.date}"
            daysRemaining.text = event.remainingTime // No extra "days" text

            // Load image using Glide
            Glide.with(itemView.context)
                .load(event.imageURL) // Corrected field name
                .placeholder(android.R.color.darker_gray)
                .into(eventImage)
        }
    }
}
