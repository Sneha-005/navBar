package com.example.navbar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SubmissionAdapter(private val submissions: List<Submission>) :
    RecyclerView.Adapter<SubmissionAdapter.SubmissionViewHolder>() {

    inner class SubmissionViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val langTextView: TextView = itemView.findViewById(R.id.langTextView)
        val statusDisplayTextView: TextView = itemView.findViewById(R.id.statusTextView)
        val timestampTextView: TextView = itemView.findViewById(R.id.timestampTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            SubmissionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_submission, parent, false)
        return SubmissionViewHolder(view)
    }

    override fun onBindViewHolder(holder: SubmissionViewHolder, position: Int) {
        val submission = submissions[position]
        holder.titleTextView.text = submission.title
        holder.langTextView.text = "Language: ${submission.lang}"
        holder.statusDisplayTextView.text = "Status: ${submission.statusDisplay}"
        holder.timestampTextView.text = "Submitted at: ${submission.timestamp}"
    }

    override fun getItemCount(): Int{
        return submissions.size
    }
}
