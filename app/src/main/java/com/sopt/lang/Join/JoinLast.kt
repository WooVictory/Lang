package com.sopt.lang.Join

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import com.sopt.lang.Home.HomeActivity
import com.sopt.lang.R

class JoinLast : AppCompatActivity() {

    internal lateinit var start: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_last)


        start = findViewById<View>(R.id.start) as TextView
        start.setOnClickListener {
            val main = Intent(this@JoinLast, HomeActivity::class.java)
            startActivity(main)
        }
    }
}
