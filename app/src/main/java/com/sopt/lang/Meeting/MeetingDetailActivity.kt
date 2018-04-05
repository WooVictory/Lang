package com.sopt.lang.Meeting

import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapFragment
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.sopt.lang.Login.kakao.GlobalApplication
import com.sopt.lang.Meeting.MeetingApply.ApplyActivityFirst
import com.sopt.lang.Meeting.MeetingParticipants.MeetingParticipantsActivity
import com.sopt.lang.Meeting.MeetingReview.MeetingReviewActivity
import com.sopt.lang.Network.Meeting.MeetingDetailData
import com.sopt.lang.Network.Meeting.MeetingDetailDataResponse
import com.sopt.lang.Network.Meeting.MeetingDetailParticipants
import com.sopt.lang.Network.NetworkService
import com.sopt.lang.R
import com.sopt.lang.SharedPreferencesService
import kotlinx.android.synthetic.main.activity_meeting_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.*

class MeetingDetailActivity : AppCompatActivity(), OnMapReadyCallback {


    var mCoder: Geocoder = Geocoder(this, Locale.KOREAN)
    private var networkService: NetworkService? = null
    private var meetingDetailData: MeetingDetailData? = null
    private var meetingDetailParticipantsData: List<MeetingDetailParticipants>? = null
    //    private var token : String = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJrZXkxIjoiOE4rbHR3ZGpISFNETlA3WElmQjAzSHcxbWNVbnRVUUdTcE8ydWVvT21qdWY3Y0ZScGFxbHBTQS85djNYM2U2dlZmbnN1LzlvTDFKWGFoS0g1YURKT1E9PSIsImlhdCI6MTUxNTQ0MjA2MSwiZXhwIjoxNTE4MDM0MDYxfQ.KLCwFKDX3JutkqsJzkJf8ZMx_2xtWQBNE4RIYhBSq1w"
    var meeting_id: Int = 1
    var meeting_list_size: Int = 0

    var place_lat: String? = null
    var place_lng: String? = null
    var meetingParticipantsId: List<MeetingDetailParticipants>? = null
    var key: Int? = null
    var location: LatLng? = null
    var meetingSpot: String? = null
    var myState: Int? = null
    var state: Int = 201
    override fun onStart() {
        super.onStart()
        mMeetingDetail()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meeting_detail)


        key = intent.getIntExtra("key", 0)
        if (key == 1) {
            detail_home_heart_btn!!.setImageResource(R.drawable.home_like_on)
        } else {
            detail_home_heart_btn!!.setImageResource(R.drawable.home_like_off)
        }

        state = intent.getIntExtra("state", 0)

        // 구글 맵 사용


        SharedPreferencesService.instance!!.load(this)
        networkService = GlobalApplication.instance!!.networkService

        meeting_id = intent.getIntExtra("meeting_id", meeting_id!!)
        Log.v("meeting_id_detail", meeting_id.toString())

        cv!!.setOnClickListener {
            cv!!.isClickable = false
        }
        Log.v("436", state.toString())



        detail_home_heart_btn!!.setOnClickListener {
            Toast.makeText(this, "좋아요 누르셨습니다. ", Toast.LENGTH_LONG).show()
        }
        detail_home_share_btn!!.setOnClickListener {
            Toast.makeText(this, "공유(Share)", Toast.LENGTH_SHORT).show()
        }

        detail_home_heart_btn!!.setOnClickListener {
            detail_home_heart_btn.isSelected = !detail_home_heart_btn.isSelected
            if (detail_home_heart_btn.isSelected) {
                detail_home_heart_btn.setImageResource(R.drawable.home_like_on)
            } else {
                detail_home_heart_btn.setImageResource(R.drawable.home_like_off)
            }
        }


        home_detail_participants_list!!.setOnClickListener {
            startActivity(Intent(this, MeetingParticipantsActivity::class.java))
        }

        val content_review = SpannableString("모임 후기")
        content_review.setSpan(UnderlineSpan(), 0, content_review.length, 0)
        home_detail_meeting_review.text = content_review

        val content_review_reading = SpannableString("후기 읽기 2개")
        content_review_reading.setSpan(UnderlineSpan(), 0, content_review.length, 0)
        meeting_review_reading.text = content_review

        home_detail_meeting_review!!.setOnClickListener {
            val intent = Intent(this@MeetingDetailActivity, MeetingReviewActivity::class.java)
            intent.putExtra("key", meeting_id!!)
            this!!.startActivity(intent)
        }

        meeting_review_reading!!.setOnClickListener {
            val intent = Intent(this@MeetingDetailActivity, MeetingReviewActivity::class.java)
            intent.putExtra("key", meeting_id!!)
            this!!.startActivity(intent)
        }


    }

    override fun onMapReady(map: GoogleMap?) {
        var location = LatLng(location!!.latitude, location!!.longitude)

        map!!.addMarker(MarkerOptions()
                .position(location!!)
                .title("마루180")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_pin)))
        map!!.moveCamera(CameraUpdateFactory.newLatLng(location))
        map!!.animateCamera(CameraUpdateFactory.zoomTo(15f))
    }

    fun dpTopx(dp: Int): Int {
        var density: Float = this!!.getResources().getDisplayMetrics().density
        return Math.round((dp.times(density)))
    }

    // 메뉴 추가
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.home_add, menu)
        // Associate searchable configuration with the SearchView
        return true
    }

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
                Toast.makeText(this, "해당되는 주소 정보는 없습니다", Toast.LENGTH_SHORT).show()
            } else {
                Log.d("test", list[0].toString())
                meetingSpot = list[0].getAddressLine(0).toString()
            }
        }
        home_detail_meeting_spot.setText(meetingSpot)
    }

    fun mMeetingDetail() {
        Log.d("test", "isHere")

        val meetingDetailResponse = networkService!!.getMeetingDetail(SharedPreferencesService.instance!!.getPrefStringData("token", "")
        !!, meeting_id!!)
        meetingDetailResponse.enqueue(object : Callback<MeetingDetailDataResponse> {
            override fun onResponse(call: Call<MeetingDetailDataResponse>?, response: Response<MeetingDetailDataResponse>?) {
                if (response!!.isSuccessful) {
                    Log.v("337", "success1")
                    if (response!!.body().status.equals("success")) {
                        meetingDetailData = response!!.body().data
                        Glide.with(applicationContext).load(meetingDetailData!!.info.meeting_image).apply(RequestOptions.centerCropTransform())
                                .into(detail_image)

                        Glide.with(applicationContext).load(meetingDetailData!!.info.hostImage).apply(RequestOptions.circleCropTransform())
                                .into(cicleImage)

                        var tempStr: String = meetingDetailData!!.participants!!.size.toString() + "명 전체보기"
                        var content_list = SpannableString(tempStr)
                        home_detail_participants_list.setText(content_list)

                        if ((meetingDetailData!!.info.review_count == 0) || (meetingDetailData!!.info.review_count == null)) {
                            meetingDetailData!!.average_rating = 0.toString()

                        } else {
                            review_rating_num2!!.setText(meetingDetailData!!.average_rating)
                        }

                        detail_title!!.setText(meetingDetailData!!.info.meeting_title)
                        detail_language!!.setText(meetingDetailData!!.info.meeting_lang)
                        detail_host!!.setText(meetingDetailData!!.info.hostName)
                        detail_purpose!!.setText(meetingDetailData!!.info.meeting_type.toString())
                        home_detail_host!!.setText(meetingDetailData!!.info.hostName)
                        meeting_intro_content!!.setText(meetingDetailData!!.info.meeting_intro)
                        home_detail_meeting_lang!!.setText(meetingDetailData!!.info.meeting_lang)
                        home_detail_meeting_date!!.setText(meetingDetailData!!.info.meeting_date.subSequence(0, 10))
                        //review_rating_num2!!.setText(meetingDetailData!!.average_rating)
                        review_rating2!!.rating = meetingDetailData!!.average_rating.toFloat()


                        meeting_review_reading!!.setText("모임 후기 " + meetingDetailData!!.info.review_count.toString() + "개")
                        review_show_rating!!.rating = meetingDetailData!!.average_rating.toFloat()

                        place_lat = meetingDetailData!!.info.place_lat
                        place_lng = meetingDetailData!!.info.place_lng // 위도 경도를 서버로부터 받음

                        location = LatLng(place_lat!!.toDouble(), place_lng!!.toDouble())
                        Log.v("337", location!!.toString())

                        var size: Int = meetingDetailData!!.participants.size

                        var home_detail_people_profile: Array<ImageView> = arrayOf(home_detail_people_profile1,
                                home_detail_people_profile2, home_detail_people_profile3, home_detail_people_profile4)

                        var home_detail_people: Array<TextView> = arrayOf(home_detail_people1
                                , home_detail_people2, home_detail_people3, home_detail_people4)


                        var line: Array<View> = arrayOf(line1, line2, line3, line4)
                        Log.v("651", "out : " + size.toString())
                        if (size != 0) {
                            Log.v("651", size.toString())

                            for (i in 0..size - 1) {
                                when (i) {
                                    0 -> {
                                        Glide.with(this@MeetingDetailActivity).load(meetingDetailData!!.participants[i].user_image).apply(RequestOptions.circleCropTransform()).into(home_detail_people_profile[i])
                                        Log.v("651", i.toString() + meetingDetailData!!.participants[i].user_name)
                                        home_detail_people[i]!!.setText(meetingDetailData!!.participants[i].user_name)
                                    }
                                    3 -> {
                                        line[i - 1].setBackgroundColor(resources.getColor(R.color.lineColor))
                                        Glide.with(this@MeetingDetailActivity).load(meetingDetailData!!.participants[i].user_image).apply(RequestOptions.circleCropTransform()).into(home_detail_people_profile[i])
                                        home_detail_people[i]!!.setText(meetingDetailData!!.participants[i].user_name)
                                        Log.v("651", i.toString() + meetingDetailData!!.participants[i].user_name)

                                    }
                                    4 -> {
                                        line[i - 1].setBackgroundColor(resources.getColor(R.color.lineColor))
                                        meeting_detail_more.setImageResource(R.drawable.lounge_more)
                                        Log.v("651", i.toString() + meetingDetailData!!.participants[i].user_name)
                                    }
                                    1, 2 -> {
                                        line[i - 1].setBackgroundColor(resources.getColor(R.color.lineColor))
                                        Glide.with(this@MeetingDetailActivity).load(meetingDetailData!!.participants[i].user_image).apply(RequestOptions.circleCropTransform()).into(home_detail_people_profile[i])
                                        home_detail_people[i]!!.setText(meetingDetailData!!.participants[i].user_name)
                                        Log.v("651", i.toString() + meetingDetailData!!.participants[i].user_name)

                                    }
                                }

                            }
                        } else {
                            meeting_detail_no_participant.visibility = View.VISIBLE
                            home_detail_participants.visibility = View.GONE
                            Log.v("test903", "in else")
                        }


                        Log.v("test903", "review count" + meetingDetailData!!.info.review_count.toString())

                        if ((meetingDetailData!!.info.review_count == 0) || (meetingDetailData!!.info.review_count == null)) {
                            meeting_detail_no_review.visibility = View.VISIBLE
                            meeting_detail_review_exist.visibility = View.GONE
                            Log.v("test903", "in if")
                        } else {
                            var reviewUserId: String = meetingDetailData!!.recent_review.user_id // 유저의 id
                            review_name!!.setText(meetingDetailData!!.recent_review.user_name)
                            review_profile_img.layoutParams.height = dpTopx(50)
                            Glide.with(applicationContext).load(meetingDetailData!!.recent_review.user_image).apply(RequestOptions.centerCropTransform())
                                    .into(review_profile_img)

                            Log.v("test1202", review_rating1!!.rating.toString())
                            //review_show_rating!!.rating = review_average_rating.toFloat() // 평균 평점 레이팅
                            review_rating1.rating = meetingDetailData!!.recent_review.review_rating.toFloat()
                            review_rating_num1!!.setText(meetingDetailData!!.recent_review.review_rating)
                            review_context!!.setText(meetingDetailData!!.recent_review.review_content)
                        }
                        myState = meetingDetailData!!.my_state // 내 상태 저장
                        Log.v("416", myState.toString())

                        meetingDetailParticipantsData = meetingDetailData!!.participants


                        Log.v("제목", meetingDetailData!!.info.meeting_title)

                        /*FIXME
                        구글 맵 이용해서 지도 띄우기
                        * */
                        val fragmentManager = fragmentManager
                        val mapFragment = fragmentManager.findFragmentById(R.id.map) as MapFragment
                        mapFragment.getMapAsync(this@MeetingDetailActivity)

                        meetingSpot = findAddressText(location!!).toString()
                        Log.v("test9898", meetingSpot)

                        //home_detail_meeting_spot!!.setText(meetingSpot!!)fa

                        //test(myState!!)
                        test(myState!!)

                    }
                }
            }

            override fun onFailure(call: Call<MeetingDetailDataResponse>?, t: Throwable?) {
            }

        })
    }

    /*FIXME
상태 나누기
* */
    /*   fun test(state : Int){
           when(state) {
               201 -> { // 참여가능
                   apply_btn!!.setOnClickListener{
                       val intent : Intent = Intent(this, ApplyActivityFirst::class.java) // 첫번째 모임 신청 페이지로 meeting_id 전달
                       intent.putExtra("meeting_id",meeting_id)
                       startActivity(intent)
                       apply_btn!!.setImageResource(R.drawable.apply_apply_cnacle)
                       //mMeetingDetail()
                   }
               }
               202 -> { // 대기중
                   //mMeetingDetail()
                   apply_btn!!.visibility = View.INVISIBLE
                   apply_cancel_btn!!.setImageResource(R.drawable.apply_apply_cnacle)
                   apply_cancel_btn!!.setOnClickListener{
                       Toast.makeText(this@MeetingDetailActivity, "취소", Toast.LENGTH_SHORT).show()
                   }

               }
               203 -> { // 참여중
                   apply_btn!!.visibility = View.INVISIBLE
                   apply_cancel_btn!!.visibility = View.INVISIBLE
                   participants_cancel_btn!!.setImageResource(R.drawable.apply_cancellation)
                   participants_cancel_btn!!.setOnClickListener{
                       Toast.makeText(this@MeetingDetailActivity, "취소", Toast.LENGTH_SHORT).show()
                   }

               }
               204-> { // 방장
                   apply_btn!!.visibility = View.INVISIBLE
                   apply_cancel_btn!!.visibility = View.INVISIBLE
                   participants_cancel_btn!!.visibility = View.INVISIBLE
                   management_btn!!.setOnClickListener{

                   }
               }

           }*/
    fun test(state: Int) {
        when (state) {
            201 -> { // 참여가능
                apply_btn!!.setOnClickListener {
                    val intent: Intent = Intent(this, ApplyActivityFirst::class.java) // 첫번째 모임 신청 페이지로 meeting_id 전달
                    intent.putExtra("meeting_id", meeting_id)
                    startActivity(intent)
                    //mMeetingDetail()
                }
            }
            202 -> { // 대기중
                //mMeetingDetail()
                apply_btn!!.visibility = View.INVISIBLE
                apply_cancel_btn!!.setImageResource(R.drawable.apply_apply_cnacle)
                apply_cancel_btn!!.setOnClickListener {
                  //  Toast.makeText(this@MeetingDetailActivity, "취소", Toast.LENGTH_SHORT).show()
                }

            }
            203 -> { // 참여중
                apply_btn!!.visibility = View.INVISIBLE
                apply_cancel_btn!!.visibility = View.INVISIBLE
                participants_cancel_btn!!.setImageResource(R.drawable.apply_cancellation)
                participants_cancel_btn!!.setOnClickListener {
                    //Toast.makeText(this@MeetingDetailActivity, "취소", Toast.LENGTH_SHORT).show()
                }

            }
            204 -> { // 방장
                apply_btn!!.visibility = View.INVISIBLE
                apply_cancel_btn!!.visibility = View.INVISIBLE
                participants_cancel_btn!!.visibility = View.INVISIBLE
                management_btn!!.setOnClickListener {

                }
            }

        }
      //  mMeetingDetail()
    }
}