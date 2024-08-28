package com.aric.knowyourleader

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.aric.knowyourleader.Fragments.profile
import com.aric.knowyourleader.Fragments.results
import com.aric.knowyourleader.Fragments.voting
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        Replace_fragment(voting())

        val bot = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        bot.setOnItemSelectedListener {

            when(it.itemId){

                R.id.voting ->Replace_fragment(voting())
                R.id.results ->Replace_fragment(results())
                R.id.profile ->Replace_fragment(profile())

                else ->{



                }

            }
            true

        }

    }

    private fun Replace_fragment(fragment : Fragment){

        val fm =supportFragmentManager
        val ft = fm.beginTransaction()

        ft.replace(R.id.frame,fragment)
        ft.commit()

    }

}