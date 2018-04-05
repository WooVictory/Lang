package com.sopt.lang.Meeting.MeetingCreate

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapFragment
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.sopt.lang.R
import kotlinx.android.synthetic.main.activity_meeting_create3.*
import java.io.IOException
import java.util.*


class MeetingCreateActivity3 : AppCompatActivity(), OnMapReadyCallback{

    var loc: LatLng? = null
    var meeting_place: String? = null
    var mCoder: Geocoder = Geocoder(this, Locale.KOREAN)
    var meeting_location : LatLng? = null
    var final_meeting_place: String? = null
    var dateStr : String? = null
    internal var myValue = ""


    var mYear: Int? = null
    var mMonth: Int? = null
    var mDay: Int? = null
    var mHour: Int? = null
    var mMinute: Int? = null

    var meetingType : Int = 0
    var meetingTitle : String = "1"
    var meetingIntro : String = "1"
    var imgUrl : String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meeting_create3)

        imgUrl = intent.getStringExtra("imgUrl")
        meetingTitle = intent.getStringExtra("meetingTitle")
        meetingIntro = intent.getStringExtra("meetingIntro")
        meetingType = intent.getIntExtra("meetingType",0)
        myValue = intent.getStringExtra("myValue")

        Log.v("938", myValue)

        val fragmentManager = fragmentManager
        val mapFragment = fragmentManager.findFragmentById(R.id.meeting_map) as MapFragment
        mapFragment!!.getMapAsync(this)


        // 뒤로가기
        meeting3_create_back_btn!!.setOnClickListener {
            finish()
        }

        var cal : Calendar = GregorianCalendar()
        mYear = cal.get(Calendar.YEAR)
        mMonth = cal.get(Calendar.MONTH)
        mDay = cal.get(Calendar.DAY_OF_MONTH)
        mHour = cal.get(Calendar.HOUR_OF_DAY)
        mMinute = cal.get(Calendar.MINUTE)

        // 지도
        meetingcreate_map_search!!.setOnClickListener {
            meeting_place = meeting_place_edit.text.toString()
            if (meeting_place_edit.text.toString().length < 0) {
                val t = Toast.makeText(applicationContext, "장소를 입력해주세요!", Toast.LENGTH_SHORT)
                t.show()
            } else {
                meeting_location = findAddressLocation()
                mapFragment.getMapAsync { googleMap: GoogleMap? //구글맵에 사용자가 입력한 장소 마커표시하기
                    ->
                    googleMap!!.clear()
                    googleMap!!.addMarker(MarkerOptions().position(meeting_location!!).title("모임장소").icon(BitmapDescriptorFactory.fromResource(R.drawable.map_pin))).showInfoWindow()
                    googleMap!!.moveCamera(CameraUpdateFactory.newLatLng(meeting_location))
                    googleMap!!.animateCamera(CameraUpdateFactory.zoomTo(10f))
                }
                /*TODO*///위도경도로 변환된 meeting_location을 서버로 보내야함
                final_meeting_place = findAddressText(meeting_location!!).toString()    //사용자가 설정한 확정 장소 텍스트뷰에 표시
                if (final_meeting_place != null)
                    meeting3_create_next_btn.setImageResource(R.drawable.meetingcreate_next_on)
            }
        }

        // 모든 기입사항을 입력하지 않았을때는 다음버튼이 비활성화이어야 하며 클릭시 입력을 요구하는 토스트가 뜨도록 하자.
        //데이트 피커 사용시 확정 날짜를 어떻게 받아올까?
        meeting3_create_next_btn!!.setOnClickListener {
            Log.v("918","touch")
            if (final_meeting_place == null)
            {
                Toast.makeText(applicationContext, "모든 빈칸을 입력하세요!", Toast.LENGTH_SHORT).show()
            } else {
                var lat: String = loc!!.latitude.toString()
                var lng: String = loc!!.longitude.toString()
                var intent: Intent = Intent(this, MeetingCreateActivity4::class.java)
                intent.putExtra("imgUrl", imgUrl)
                Log.v("meeting3 ",imgUrl!!)
                intent.putExtra("meetingTitle", meetingTitle)
                intent.putExtra("meetingIntro", meetingIntro)
                intent.putExtra("meetingType", 0)
                intent.putExtra("myValue", myValue)
                var date : String = mYear.toString() + mMonth.toString() + mDay.toString()
                intent.putExtra("date", date) //날짜
                intent.putExtra("startHour", mHour!!)
                intent.putExtra("endHour", mHour!!)
                intent.putExtra("lat", lat)
                intent.putExtra("lng", lng)
                Log.v("startTime", mHour.toString())
                Log.v("lat33", lat)
                Log.v("lng33", lng)

                startActivity(intent)
            }

        }
//android.R.style.Theme_Holo_Light_Dialog
        meeting_date!!.setOnClickListener {
            DatePickerDialog(this@MeetingCreateActivity3, AlertDialog.THEME_HOLO_LIGHT, mDateSetListener, mYear!!, mMonth!!, mDay!!).show()
            Log.v("date 849", mYear.toString())
        }
        meeting3_time1!!.setOnClickListener {
            TimePickerDialog(this@MeetingCreateActivity3, AlertDialog.THEME_HOLO_LIGHT, mTimeSetListener, mHour!!, mMinute!!, false). show()
        }
        meeting3_time2!!.setOnClickListener {
            TimePickerDialog(this@MeetingCreateActivity3, AlertDialog.THEME_HOLO_LIGHT, mTimeSetListener2, mHour!!, mMinute!!, false). show()
        }

    }

    //구글맵 초기자동설정
    override fun onMapReady(map: GoogleMap?) {
        val Maru = LatLng(37.4954051, 127.0383379)

        map!!.addMarker(MarkerOptions()
                .position(Maru)
                .title("마루180")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_pin)))
        map!!.moveCamera(CameraUpdateFactory.newLatLng(Maru))
        map!!.animateCamera(CameraUpdateFactory.zoomTo(15f))
    }

    /*TODO*/ //위도경도를 주소로 변환 ---> 서버에서 받을때 필요, 모임상세 페이지에서 모임장소 지도
    fun findAddressText(dragPosition: LatLng) { //위도경도를 주소로 변환 ---> 서버에서 받을때 필요, 모임상세 페이지에서 모임장소 지도에 표시위해
        var list: List<Address>? = null
        try {
            val d1 = dragPosition.latitude
            val d2 = dragPosition.longitude

            list = mCoder.getFromLocation(
                    d1,
                    d2,
                    10)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        if (list != null) {
            if (list.size == 0) {
                Toast.makeText(this,"해당되는 주소 정보는 없습니다",Toast.LENGTH_SHORT).show()
            } else {
                Log.d("test", list[0].getAddressLine(0).toString())
                meeting_place_edit.setText(list[0].getAddressLine(0).toString())
            }
        }
    }

    fun findAddressLocation(): LatLng { //입력된 스트링의 주소를 검색하고 그 결과를 위도경도로 반환
        var addr: List<Address>? = null

        try {
            addr = mCoder.getFromLocationName(meeting_place, 5)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        if (addr != null) { //address형태로
            for (i in addr.indices) {
                val lating = addr[i]
                val lat = lating.getLatitude()
                val lon = lating.getLongitude()
                loc = LatLng(lat, lon)
            }
        }
        else{
            Toast.makeText(this,"해당되는 주소 정보가 없습니다. 다시 입력하세요.",Toast.LENGTH_SHORT).show()
        }
        return loc!!
    }

    val mDateSetListener = DatePickerDialog.OnDateSetListener { datePicker, year , monthOfYear, dayOfMonth ->
        mYear = year
        mMonth = monthOfYear
        mDay = dayOfMonth
        meeting3_date1_text!!.setText(String.format("%d/%d/%d", mYear, mMonth!!.plus(1), mDay))
    }

    val mTimeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hourOfDay, minute ->
        mHour = hourOfDay
        mMinute = minute
        meeting3_time1!!.setText(String.format("%d : %d", mHour, mMinute))
    }

    val mTimeSetListener2 = TimePickerDialog.OnTimeSetListener { timePicker, hourOfDay, minute ->
        mHour = hourOfDay
        mMinute = minute
        meeting3_time2!!.setText(String.format("%d : %d", mHour, mMinute))
    }

}