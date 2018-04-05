package com.sopt.lang.Meeting.MeetingCreate

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.sopt.lang.R
import kotlinx.android.synthetic.main.activity_meeting_create1.*

class MeetingCreateActivity1 : AppCompatActivity() {

    var cnt : Int=0
    var imgUrl : String = "11"

    //var MeetingCreateData.meetingdata = MeetingCreateData(title,)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meeting_create1)


        // 사진 선택
        meeting_create_photo!!.setOnClickListener{
            Toast.makeText(this, "사진", Toast.LENGTH_SHORT).show()
            cnt +=1
            if(cnt == 1)
            {
                val permissionCheck = ContextCompat.checkSelfPermission(this@MeetingCreateActivity1, Manifest.permission.READ_EXTERNAL_STORAGE)

                if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(this@MeetingCreateActivity1, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 0)
                    // 권한 없음
                }
                upload_pic_gallery() // 갤러리 접근

            }
        }

        // 취소버튼
        meeting_cancel_btn!!.setOnClickListener{
            finish()
        }

        // 다음 버튼
        meeting_create_next_btn!!.setOnClickListener{
            if(cnt == 1)
            {
                val intent : Intent = Intent(this, MeetingCreateActivity2::class.java)
                intent.putExtra("imgUrl",imgUrl!!)
                startActivity(intent)
            }else{
                Toast.makeText(this, "사진을 선택해주세요.", Toast.LENGTH_SHORT).show()
            }
        }


    }

    fun upload_pic_gallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = android.provider.MediaStore.Images.Media.CONTENT_TYPE
        intent.data = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        startActivityForResult(intent, 20)
    }

    fun change_to_on(){
        meeting_create_next_btn!!.setImageResource(R.drawable.apply_next_on)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == 20) {
            if (resultCode == Activity.RESULT_OK) {
                Glide.with(this).load(data.getData()).into(meeting_create_photo)
                change_to_on()
                imgUrl = data.getData().toString()
                Log.v(" imgUrl : ", imgUrl)
                //toMeetingCreate()
            }
        }
    }
    /* fun toMeetingCreate()
     {
         Log.v("817 imgUrl",MeetingCreateData.meetingdata!!.imgUrl)
     }*/
}