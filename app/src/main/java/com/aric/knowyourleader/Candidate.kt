package com.aric.knowyourleader

import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Candidate : AppCompatActivity() {

    private lateinit var tvCandidateName: TextView
    private lateinit var radioGroup: RadioGroup
    private lateinit var btnSubmitVote: Button

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var votesRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_candidate)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        tvCandidateName = findViewById(R.id.tvCandidateName)
        radioGroup = findViewById(R.id.radioGroup)
        btnSubmitVote = findViewById(R.id.btnSubmitVote)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        votesRef = database.getReference("votes")

        val candidateName = intent.getStringExtra("candidateName")
        tvCandidateName.text = candidateName

        val userEmail = auth.currentUser?.email ?: ""

        // Validate the email address against the allowed range
        if (!isUserIdValid(userEmail)) {
            Toast.makeText(this, "You are not eligible to vote!", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        btnSubmitVote.setOnClickListener {
            val selectedOptionId = radioGroup.checkedRadioButtonId

            if (selectedOptionId == -1) {
                Toast.makeText(this, "Please select a preference!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val selectedRadioButton = findViewById<RadioButton>(selectedOptionId)
            val preference = when (selectedRadioButton.id) {
                R.id.radioFirstPreference -> "First Preference"
                R.id.radioSecondPreference -> "Second Preference"
                R.id.radioThirdPreference -> "Third Preference"
                else -> ""
            }

            val userVotesRef = votesRef.child(userEmail.replace(".", ",")) // To handle Firebase keys safely

            userVotesRef.get().addOnSuccessListener { snapshot ->
                val existingVote = snapshot.child(preference).getValue(String::class.java)
                if (existingVote != null) {
                    Toast.makeText(
                        this,
                        "You have already voted for $preference!",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@addOnSuccessListener
                }

                userVotesRef.child(preference).setValue(candidateName)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Vote submitted successfully!", Toast.LENGTH_SHORT)
                            .show()
                        finish()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(
                            this,
                            "Failed to submit vote: ${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }
        }
    }

    private fun isUserIdValid(email: String): Boolean {
        val regex = Regex(
            "^((23ucs(5[0-9][0-9]|6[0-9][0-9]|7[0-3][0-9]|740))|" +
                    "(23uec(5[0-9][0-9]|6[0-4][0-9]|650|651))|" +
                    "(23ucc(5[0-9][0-9]|6[0-2][0-9]|626))|" +
                    "(23ume(5[0-9][0-9]|5[0-5][0-9]|560)))@lnmiit.ac.in$"
        )
        return regex.matches(email)
    }
}
