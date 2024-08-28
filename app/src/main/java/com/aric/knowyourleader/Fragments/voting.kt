package com.aric.knowyourleader.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import com.aric.knowyourleader.Candidate
import com.aric.knowyourleader.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class voting : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var votesRef: DatabaseReference
    private lateinit var votingStatusRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
        val database = FirebaseDatabase.getInstance()
        votesRef = database.getReference("votes")
        votingStatusRef = database.getReference("votingStatus").child("isVotingOpen")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_voting, container, false)

        view.findViewById<FrameLayout>(R.id.frameLayout).setOnClickListener {
            checkVotingStatusAndProceed("Ujjawal Khatri")
        }

        view.findViewById<FrameLayout>(R.id.frameLayout3).setOnClickListener {
            checkVotingStatusAndProceed("Tushar Agrawal")
        }

        view.findViewById<FrameLayout>(R.id.frameLayout2).setOnClickListener {
            checkVotingStatusAndProceed("Vasu Sharma")
        }

        view.findViewById<FrameLayout>(R.id.frameLayout5).setOnClickListener {
            checkVotingStatusAndProceed("Aniket Rakwal")
        }

        view.findViewById<FrameLayout>(R.id.frameLayout6).setOnClickListener {
            checkVotingStatusAndProceed("Pranav Khunt")
        }

        view.findViewById<FrameLayout>(R.id.frameLayout4).setOnClickListener {
            checkVotingStatusAndProceed("Raman Singh")
        }

        view.findViewById<FrameLayout>(R.id.frameLayout7).setOnClickListener {
            checkVotingStatusAndProceed("Priyal Maheshwari")
        }

        view.findViewById<FrameLayout>(R.id.frameLayout8).setOnClickListener {
            checkVotingStatusAndProceed("Akshita Behl")
        }

        view.findViewById<FrameLayout>(R.id.frameLayout9).setOnClickListener {
            checkVotingStatusAndProceed("Anish Laddha")
        }

        view.findViewById<FrameLayout>(R.id.frameLayout10).setOnClickListener {
            checkVotingStatusAndProceed("Hussain Haidary")
        }

        view.findViewById<FrameLayout>(R.id.frameLayout11).setOnClickListener {
            checkVotingStatusAndProceed("Manas Yadav")
        }

        view.findViewById<FrameLayout>(R.id.frameLayout12).setOnClickListener {
            checkVotingStatusAndProceed("Priyam Garg")
        }

        view.findViewById<FrameLayout>(R.id.frameLayout13).setOnClickListener {
            checkVotingStatusAndProceed("Saumilya Gupta")
        }

        view.findViewById<FrameLayout>(R.id.frameLayout14).setOnClickListener {
            checkVotingStatusAndProceed("Arnav Rinawa")
        }

        view.findViewById<FrameLayout>(R.id.frameLayout15).setOnClickListener {
            NotParticipated()
        }

        view.findViewById<FrameLayout>(R.id.frameLayout16).setOnClickListener {
            checkVotingStatusAndProceed("Akshit Garg")
        }

        view.findViewById<FrameLayout>(R.id.frameLayout17).setOnClickListener {
            NotParticipated()
        }

        view.findViewById<FrameLayout>(R.id.frameLayout18).setOnClickListener {
            NotParticipated()
        }

        view.findViewById<FrameLayout>(R.id.frameLayout19).setOnClickListener {
            NotParticipated()
        }

        view.findViewById<FrameLayout>(R.id.frameLayout20).setOnClickListener {
            checkVotingStatusAndProceed("Kiri Singhal")
        }

        return view
    }

    private fun NotParticipated(){

        Toast.makeText(context,"The Senator Did Not Wanted To Join In This Initiative!",Toast.LENGTH_SHORT).show()

    }

    private fun checkVotingStatusAndProceed(candidateName: String) {
        votingStatusRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val isVotingOpen = snapshot.getValue(Boolean::class.java) ?: false

                if (isVotingOpen) {
                    checkVoteStatusAndOpenCandidateActivity(candidateName)
                } else {
                    Toast.makeText(context, "Voting lines are currently closed.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun checkVoteStatusAndOpenCandidateActivity(candidateName: String) {
        val userEmail = auth.currentUser?.email ?: return


        if (!isUserEmailValid(userEmail)) {
            Toast.makeText(context, "You are not eligible to vote!", Toast.LENGTH_SHORT).show()
            return
        }


        val userVotesRef = votesRef.child(userEmail.replace(".", ","))

        userVotesRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    var hasVoted = false

                    for (preferenceSnapshot in snapshot.children) {
                        if (preferenceSnapshot.value == candidateName) {
                            hasVoted = true
                            break
                        }
                    }

                    if (hasVoted) {
                        Toast.makeText(context, "You have already voted for $candidateName!", Toast.LENGTH_SHORT).show()
                    } else {
                        openCandidateActivity(candidateName)
                    }
                } else {
                    openCandidateActivity(candidateName)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


//    // Function to validate user ID
//    private fun isUserEmailValid(email: String): Boolean {
//        val userId = email.substringBefore("@")
//        val regex = Regex(
//            "^((23ucs(5[0-9][0-9]|6[0-9][0-9]|7[0-3][0-9]|740))|" +
//                    "(23uec(5[0-9][0-9]|6[0-4][0-9]|650|651))|" +
//                    "(23ucc(5[0-9][0-9]|6[0-2][0-9]|626))|" +
//                    "(23ume(5[0-9][0-9]|5[0-5][0-9]|560)))$"
//        )
//        return regex.matches(userId)
//    }


    private fun isUserEmailValid(email: String): Boolean {
        val userId = email.substringBefore("@")
        val regex = Regex(
            "^((23ucs(5[0-9][0-9]|6[0-9][0-9]|7[0-3][0-9]|740))|" +
                    "(23uec(5[0-9][0-9]|6[0-4][0-9]|650|651))|" +
                    "(23ucc(5[0-9][0-9]|6[0-2][0-9]|626))|" +
                    "(23ume(5[0-9][0-9]|5[0-5][0-9]|560))|" +
                    "(23dec(50[1-9]|51[0-9]|520))|" +
                    "(23dcs(500|50[1-9]|510)))$"
        )
        return regex.matches(userId)
    }






    private fun openCandidateActivity(candidateName: String) {
        val intent = Intent(activity, Candidate::class.java)
        intent.putExtra("candidateName", candidateName)
        startActivity(intent)
    }
}
