package com.sopt.lang.Intro

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.sopt.lang.Home.HomeActivity
import com.sopt.lang.R
import com.sopt.lang.SharedPreferencesService


class Splash : AppCompatActivity() {
//    private var token : String = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJrZXkxIjoiOE4rbHR3ZGpISFNETlA3WElmQjAzSHcxbWNVbnRVUUdTcE8ydWVvT21qdWY3Y0ZScGFxbHBTQS85djNYM2U2dlZmbnN1LzlvTDFKWGFoS0g1YURKT1E9PSIsImlhdCI6MTUxNTQ0MjA2MSwiZXhwIjoxNTE4MDM0MDYxfQ.KLCwFKDX3JutkqsJzkJf8ZMx_2xtWQBNE4RIYhBSq1w"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val intro = Intent(this, IntroActivity::class.java)
        val main = Intent(this, HomeActivity::class.java)
        var face_token: String? = null
        face_token = ""
        SharedPreferencesService.instance!!.load(this)
        SharedPreferencesService.instance!!.setPrefData("badgeCount", 0)

//        SharedPreferencesService.instance!!.setPrefData("token",token)

//               try {
//            val info = packageManager.getPackageInfo("com.sopt.lang", PackageManager.GET_SIGNATURES)
//            for (signature in info.signatures) {
//                val md = MessageDigest.getInstance("SHA")
//                md.update(signature.toByteArray())
//                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
//            }
//        } catch (e: PackageManager.NameNotFoundException) {
//            e.printStackTrace()
//        } catch (e: NoSuchAlgorithmException) {
//            e.printStackTrace()
//        }
        Log.v("토토토큰", SharedPreferencesService.instance!!.getPrefStringData("name", ""))

        Log.v("토토토큰", SharedPreferencesService.instance!!.getPrefStringData("token", ""))
        val hd = Handler()
        hd.postDelayed({
            /*            if(Session.getCurrentSession().checkAndImplicitOpen() ||
                                !face_token.equals(SharedPreferencesService.instance!!.getPrefStringData("token","")))*/
            if (SharedPreferencesService.instance!!.getPrefStringData("token")!!.isNotEmpty()) {
                startActivity(main)
            } else {
                startActivity(intro)
            }
            finish()
        }, 1500)

    }
}
