package com.sopt.lang.Intro

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import com.sopt.lang.R
import com.sopt.lang.Login.LoginActivity


class IntroActivity : AppCompatActivity() {
    var vp: ViewPager? = null
    var tabLayout: TabLayout?=null
    var login_btn : TextView?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)


        vp = findViewById<View>(R.id.vp) as ViewPager
        login_btn = findViewById<View>(R.id.login_btn) as TextView
        tabLayout = findViewById<View>(R.id.tab_layout) as TabLayout

        vp!!.setAdapter(PagerAdapter(supportFragmentManager))
        vp!!.setCurrentItem(0)

        tabLayout!!.setupWithViewPager(vp!!, true)

        login_btn!!.setOnClickListener {
            val login = Intent(this, LoginActivity::class.java)
            startActivity(login)
        }
    }

}
