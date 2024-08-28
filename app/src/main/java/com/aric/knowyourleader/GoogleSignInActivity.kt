//package com.aric.knowyourleader
//
//import android.content.Intent
//import android.os.Bundle
//import android.util.Log
//import android.widget.Button
//import android.widget.Toast
//import androidx.activity.enableEdgeToEdge
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.view.ViewCompat
//import androidx.core.view.WindowInsetsCompat
//import com.aric.knowyourleader.MainActivity
//import com.aric.knowyourleader.R
//import com.google.android.gms.auth.api.signin.GoogleSignIn
//import com.google.android.gms.auth.api.signin.GoogleSignInClient
//import com.google.android.gms.auth.api.signin.GoogleSignInOptions
//import com.google.android.gms.common.api.ApiException
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.GoogleAuthProvider
//import com.google.firebase.database.FirebaseDatabase
//
//class GoogleSignInActivity : AppCompatActivity() {
//
//    private lateinit var auth: FirebaseAuth
//
//    //554914929417-r9caqoqn0qpep5hcgtuu4ebigbm1mdep.apps.googleusercontent.com
//
//    private lateinit var googleSignInClient: GoogleSignInClient
//
//    companion object {
//        private const val RC_SIGN_IN = 9001
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContentView(R.layout.activity_google_sign_in)
//        setupWindowInsets()
//
//        auth = FirebaseAuth.getInstance()
//
//        // Configure Google Sign-In options
//        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken(getString(R.string.default_web_client_id))
//            .requestEmail()
//            .build()
//
//        googleSignInClient = GoogleSignIn.getClient(this, gso)
//
//        val btnSignIn = findViewById<Button>(R.id.btnGoogleSignIn)
//        btnSignIn.setOnClickListener {
//            signInWithGoogle()
//        }
//    }
//
//    private fun signInWithGoogle() {
//        val signInIntent = googleSignInClient.signInIntent
//        startActivityForResult(signInIntent, RC_SIGN_IN)
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (requestCode == RC_SIGN_IN) {
//            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
//            try {
//                val account = task.getResult(ApiException::class.java)!!
//                val email = account.email ?: ""
//
//                // Check if email ends with @lnmiit.ac.in
//                if (email.endsWith("@lnmiit.ac.in")) {
//                    firebaseAuthWithGoogle(account.idToken!!)
//                } else {
//                    googleSignInClient.signOut()
//                    Toast.makeText(this, "Email must be @lnmiit.ac.in", Toast.LENGTH_SHORT).show()
//                }
//            } catch (e: ApiException) {
//                Log.w("GoogleSignInActivity", "Google sign-in failed", e)
//                Toast.makeText(this, "Google sign-in failed: ${e.message}", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//
//    private fun firebaseAuthWithGoogle(idToken: String) {
//        val credential = GoogleAuthProvider.getCredential(idToken, null)
//        auth.signInWithCredential(credential)
//            .addOnCompleteListener(this) { task ->
//                if (task.isSuccessful) {
//                    val user = auth.currentUser
//                    user?.let {
//                        saveUserDataToDatabase(it.displayName ?: "", it.email ?: "")
//                    }
//                } else {
//                    Toast.makeText(this, "Authentication Failed.", Toast.LENGTH_SHORT).show()
//                }
//            }
//    }
//
//    private fun saveUserDataToDatabase(name: String, email: String) {
//        val userId = auth.currentUser?.uid ?: return
//        val userMap = mapOf("name" to name, "email" to email)
//
//        FirebaseDatabase.getInstance().getReference("users")
//            .child(userId)
//            .setValue(userMap)
//            .addOnSuccessListener {
//                Toast.makeText(this, "User registered successfully!", Toast.LENGTH_SHORT).show()
//                navigateToMainActivity()
//            }
//            .addOnFailureListener {
//                Toast.makeText(this, "Failed to write to database: ${it.message}", Toast.LENGTH_SHORT).show()
//            }
//    }
//
//    private fun navigateToMainActivity() {
//        val intent = Intent(this, MainActivity::class.java)
//        startActivity(intent)
//        finish()
//    }
//
//    private fun setupWindowInsets() {
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
//    }
//}
