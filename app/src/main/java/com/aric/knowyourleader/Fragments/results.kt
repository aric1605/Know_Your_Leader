package com.aric.knowyourleader.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aric.knowyourleader.CandidateResult
import com.aric.knowyourleader.R
import com.aric.knowyourleader.ResultsAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class results : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var resultsAdapter: ResultsAdapter
    private lateinit var resultsList: MutableList<CandidateResult>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_results, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewResults)
        recyclerView.layoutManager = LinearLayoutManager(activity)

        resultsList = mutableListOf()
        resultsAdapter = ResultsAdapter(resultsList,true)
        recyclerView.adapter = resultsAdapter

        fetchResults()

        return view
    }

    private fun fetchResults() {
        val database = FirebaseDatabase.getInstance()
        val votesRef = database.getReference("votes")

        votesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val candidateScores = mutableMapOf<String, Int>()

                snapshot.children.forEach { userSnapshot ->
                    userSnapshot.children.forEach { voteSnapshot ->
                        val candidate = voteSnapshot.getValue(String::class.java) ?: ""
                        val preference = voteSnapshot.key ?: ""

                        val points = when (preference) {
                            "First Preference" -> 5
                            "Second Preference" -> 3
                            "Third Preference" -> 1
                            else -> 0
                        }

                        candidateScores[candidate] =
                            candidateScores.getOrDefault(candidate, 0) + points
                    }
                }

                // Sort by points in descending order
                val sortedResults = candidateScores.map { CandidateResult(it.key, it.value) }
                    .sortedByDescending { it.points }

                resultsList.clear()
                resultsList.addAll(sortedResults)
                resultsAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database errors
            }
        })
    }


}