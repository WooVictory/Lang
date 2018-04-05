package com.sopt.lang.Meeting.MeetingCreate

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.RequestManager
import com.google.android.gms.maps.model.LatLng
import com.sopt.lang.Login.kakao.GlobalApplication
import com.sopt.lang.Network.Base.BaseModel
import com.sopt.lang.Network.NetworkService
import com.sopt.lang.R
import com.sopt.lang.SharedPreferencesService
import kotlinx.android.synthetic.main.activity_meeting_create4.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import java.io.*
import java.util.*

class MeetingCreateActivity4 : AppCompatActivity() {


    var loc: LatLng? = null
    var meeting_place: String? = null
    var mCoder: Geocoder = Geocoder(this, Locale.KOREAN)
    var meeting_location : LatLng? = null
    var final_meeting_place: String? = null
    var date : String? = null
    internal var myValue = ""
    var image : MultipartBody.Part?=null



    var mYear: Int? = null
    var mMonth: Int? = null
    var mDay: Int? = null
    var startHour: Int? = null
    var endHour: Int? = null

    var mMinute: Int? = null

    var meetingType : Int = 0
    var meetingTitle : String = "1"
    var meetingIntro : String = "1"
    var imgUrl : String ="no"
    var lat : String?=null
    var lng : String?=null
    private var string_minimum_length : Int=0
    var cnt : Int=0
    private var networkService: NetworkService? = null

    private var requestManager : RequestManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meeting_create4)
        SharedPreferencesService.instance!!.load(this)
        networkService = GlobalApplication.instance!!.networkService


        imgUrl = intent.getStringExtra("imgUrl")
        meetingTitle = intent.getStringExtra("meetingTitle")
        meetingIntro = intent.getStringExtra("meetingIntro")
        meetingType = intent.getIntExtra("meetingType",0)
        myValue = getIntent().getStringExtra("myValue")

        Log.v("931",meetingIntro)
        Log.v("1014", imgUrl!!)

        date = getIntent().getStringExtra("date")
        startHour = getIntent().getIntExtra("startHour",0)
        endHour = getIntent().getIntExtra("endHour",0)

        lat = getIntent().getStringExtra("lat")
        lng = getIntent().getStringExtra("lng")
        image = settingImage(imgUrl!!)




        string_minimum_length = meeting4_question.text.toString().length
        if(string_minimum_length>10)
        {
            meeting4_create_upload_btn.setImageResource(R.drawable.meetingcreate_upload_on)
        }else
        {
            meeting4_create_upload_btn.setImageResource(R.drawable.meetingcreate_upload_off)
        }


        meeting4_question.addTextChangedListener(object:TextWatcher{
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(meeting4_question.text.toString().length<5){
                    meeting4_create_upload_btn.setImageResource(R.drawable.meetingcreate_upload_off)
                    cnt=0
                }
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(meeting4_question.text.toString().length>=5){
                    meeting4_create_upload_btn.setImageResource(R.drawable.meetingcreate_upload_on)
                    cnt=1
                }
            }

        })
        meeting4_create_upload_btn!!.setOnClickListener{
            if(cnt == 1){
                mMeetingCreate()
                startActivity(Intent(this, MeetingCreateActivity5::class.java))
            }else{
                Toast.makeText(this, "질문을 작성해주세요.", Toast.LENGTH_SHORT).show()
            }

        }
        meeting4_create_back_btn!!.setOnClickListener{
            finish()
        }

    }
    fun mMeetingCreate()
    {
        Log.v("SW", "success")

        var title : RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"),meetingTitle)
        var intro : RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"),meetingIntro)
        var value : RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"),myValue)
        var date : RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"),date)
        var lat : RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"),lat)
        var lng : RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"),lng)
        var question : RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"),meeting4_question.text.toString())
        var img : MultipartBody.Part? = settingImage(imgUrl!!)

        Log.v("image",image.toString())

        Log.v("SW2", "success")


        val meetingCreateResponse = networkService!!.meetingCreate(SharedPreferencesService.instance!!.getPrefStringData("token")!!,title,value,
                intro,meetingType,date,startHour!!,endHour!!,lat,lng,question,img!!)
        meetingCreateResponse!!.enqueue(object : retrofit2.Callback<BaseModel>{
            override fun onResponse(call: Call<BaseModel>?, response: Response<BaseModel>?) {
                Log.v("SW3", "success")
                if(response!!.isSuccessful)
                {
                    Log.v("SW4", "success")
                    if(response!!.body().status.equals("success"))
                    {
                        Log.v("SW5", "success2")
                        Toast.makeText(this@MeetingCreateActivity4, "성공",Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<BaseModel>?, t: Throwable?) {

            }

        })


    }
    fun settingImage(imgUrl: String): MultipartBody.Part? {
        Log.v("test137", "imgUrl : " + imgUrl)
        var body: MultipartBody.Part? = null
        var file: File? = null

        val options = BitmapFactory.Options()

        var `in`: InputStream? = null
        try {
            `in` = this@MeetingCreateActivity4.getContentResolver().openInputStream(Uri.parse(imgUrl!!))
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }

        val bitmap = BitmapFactory.decodeStream(`in`, null, options)
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos)

        try {
            `in`!!.close()

        } catch (e: IOException) {
            e.printStackTrace()
        }

        val photoBody = RequestBody.create(MediaType.parse("image/jpg"), baos.toByteArray())

        file = File(imgUrl!!)

        body = MultipartBody.Part.createFormData("image", file!!.getName(), photoBody)
        bitmap.recycle()
        return body!!
    }


}
