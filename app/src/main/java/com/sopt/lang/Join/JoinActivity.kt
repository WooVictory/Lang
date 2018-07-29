package com.sopt.lang.Join

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import com.sopt.lang.R
import java.io.BufferedReader
import java.io.InputStreamReader

class JoinActivity : AppCompatActivity() {
    internal lateinit var all_check: ImageView
    internal lateinit var tos_check: ImageView
    internal lateinit var privacy_check: ImageView
    internal lateinit var join_next: ImageView
    internal lateinit var privacy_down: ImageView
    internal lateinit var tos_down: ImageView
    internal lateinit var tos: TextView
    internal lateinit var privacy: TextView
    internal var isAll: Boolean? = false
    internal var isTos: Boolean? = false
    internal var isPrivacy: Boolean? = false
    internal var isTos_down: Boolean? = false
    internal var isPrivacy_down: Boolean? = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        all_check = findViewById<View>(R.id.join_all_check) as ImageView
        tos_check = findViewById<View>(R.id.tos_check) as ImageView
        privacy_check = findViewById<View>(R.id.privacy_check) as ImageView
        join_next = findViewById<View>(R.id.join_next) as ImageView
        privacy_down = findViewById<View>(R.id.privacy_down) as ImageView
        tos_down = findViewById<View>(R.id.tos_down) as ImageView

        tos = findViewById<View>(R.id.join_tos) as TextView
        privacy = findViewById<View>(R.id.join_privacy) as TextView

        privacy.visibility = TextView.GONE
        tos.visibility = TextView.GONE

        //txt파일 가져와서 뿌림
        try {
            // getResources().openRawResource()로 raw 폴더의 원본 파일을 가져온다.
            // txt 파일을 InpuStream에 넣는다. (open 한다)
            val in_tos = resources.openRawResource(R.raw.tos)
            val in_privacy = resources.openRawResource(R.raw.privacy)

            if (in_tos != null) {
                val stream = InputStreamReader(in_tos, "utf-8")
                val buffer = BufferedReader(stream)
                var inputString = buffer.use{stream.readText() }

//                var read : String
//                val sb = StringBuilder("")
//
//                for(read in buffer.lines()){
//                    sb.append(read)
//                }
                in_tos.close()
                // id : textView01 TextView를 불러와서
                //메모장에서 읽어온 문자열을 등록한다.
                tos.text = inputString.toString()
                tos.movementMethod = ScrollingMovementMethod()

            }
            if (in_privacy != null) {
                val stream = InputStreamReader(in_privacy, "utf-8")
                val buffer = BufferedReader(stream)
                var inputString = buffer.use{stream.readText()}

                in_privacy.close()


                privacy.text = inputString.toString()
                privacy.movementMethod = ScrollingMovementMethod()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        all_check.setOnClickListener {
            isAll = (!isAll!!)!!
            if (isAll!!) {
                all_check.setImageResource(R.drawable.join_check)
                privacy_check.setImageResource(R.drawable.join_check)
                tos_check.setImageResource(R.drawable.join_check)
                isTos = true
                isPrivacy = true
            } else {
                all_check.setImageResource(R.drawable.join_un_check)
                privacy_check.setImageResource(R.drawable.join_un_check)
                tos_check.setImageResource(R.drawable.join_un_check)
                isTos = false
                isPrivacy = false
            }
            if (isTos!! && isPrivacy!!) {
                join_next.setImageResource(R.drawable.join_next_on)
            } else {
                join_next.setImageResource(R.drawable.join_next_off)
            }
        }

        privacy_check.setOnClickListener {
            isPrivacy = (!isPrivacy!!)!!
            if (isPrivacy!!) {
                privacy_check.setImageResource(R.drawable.join_check)
            } else {
                privacy_check.setImageResource(R.drawable.join_un_check)
            }
            if (isTos!! && isPrivacy!!) {
                join_next.setImageResource(R.drawable.join_next_on)
            } else {
                join_next.setImageResource(R.drawable.join_next_off)
            }
        }

        tos_check.setOnClickListener {
            isTos = (!isTos!!)!!
            if (isTos!!) {
                tos_check.setImageResource(R.drawable.join_check)
            } else {
                tos_check.setImageResource(R.drawable.join_un_check)
            }
            if (isTos!! && isPrivacy!!) {
                join_next.setImageResource(R.drawable.join_next_on)
            } else {
                join_next.setImageResource(R.drawable.join_next_off)
            }
        }

        tos_down.setOnClickListener {
            isTos_down = (!isTos_down!!)!!
            if (isTos_down!!) {
                tos.visibility = TextView.VISIBLE
            } else {
                tos.visibility = TextView.GONE
            }
        }
        privacy_down.setOnClickListener {
            isPrivacy_down = (!isPrivacy_down!!)!!
            if (isPrivacy_down!!) {
                privacy.visibility = TextView.VISIBLE
            } else {
                privacy.visibility = TextView.GONE
            }
        }


        join_next.setOnClickListener {
            if (isTos!! && isPrivacy!!) {
                val join_next = Intent(this, JoinNextActivity::class.java)
                startActivity(join_next);
            }
        }

    }
}
