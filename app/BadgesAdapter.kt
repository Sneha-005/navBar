package com.example.navbar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class BadgesAdapter(private val badgeList: List<UpcomingBadge>) : RecyclerView.Adapter<BadgesAdapter.BadgeViewHolder>() {

    // ViewHolder class that holds the views for each badge
    class BadgeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val badgeIcon: ImageView = itemView.findViewById(R.id.badgeIcon)
        val badgeName: TextView = itemView.findViewById(R.id.badgeName)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BadgeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_badges, parent, false) // Inflate the badge item layout
        return BadgeViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: BadgeViewHolder, position: Int) {
        val badge = badgeList[position]

        // Set badge name
        holder.badgeName.text = badge.name

        // Load badge icon using Glide (or any other image loading library)
        Glide.with(holder.itemView.context)
            .load(badge.icon) // URL of the badge icon
            .into(holder.badgeIcon)
    }

    // Return the size of the dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return badgeList.size
    }
}
