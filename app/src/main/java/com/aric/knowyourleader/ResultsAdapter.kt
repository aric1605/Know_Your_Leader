package com.aric.knowyourleader

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class CandidateResult(val candidateName: String, val points: Int)

class ResultsAdapter(private val resultsList: List<CandidateResult>, private val showTop12: Boolean) :
    RecyclerView.Adapter<ResultsAdapter.ResultsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_result, parent, false)
        return ResultsViewHolder(view)
    }

    override fun onBindViewHolder(holder: ResultsViewHolder, position: Int) {
        val result = resultsList[position]

        holder.tvCandidateName.text = result.candidateName
        holder.tvPoints.text = if (showTop12 && position >= 12) "" else "${result.points} points"
    }

    override fun getItemCount() = resultsList.size

    class ResultsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCandidateName: TextView = itemView.findViewById(R.id.tvCandidateName)
        val tvPoints: TextView = itemView.findViewById(R.id.tvPoints)
    }
}
