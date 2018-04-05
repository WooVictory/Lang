package com.sopt.lang.HomeFragment

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TabHost
import android.widget.TextView
import com.sopt.lang.Alarm.AlarmAdapter
import com.sopt.lang.Alarm.AlarmData
import com.sopt.lang.Login.kakao.GlobalApplication
import com.sopt.lang.Network.Alarm.AlarmMeetingData
import com.sopt.lang.Network.Alarm.AlarmMeetingDataResponse
import com.sopt.lang.Network.Alarm.AlarmMyData
import com.sopt.lang.Network.Alarm.AlarmMyDataResponse
import com.sopt.lang.Network.NetworkService
import com.sopt.lang.R
import com.sopt.lang.SharedPreferencesService
import kotlinx.android.synthetic.main.fragment_alarm.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by sec on 2018-01-08.
 */
class AlarmFragment : Fragment(), View.OnClickListener {

    //    var view : View? = null
    private var networkService: NetworkService? = null

    var ts1 : TabHost.TabSpec? = null
    var ts2 : TabHost.TabSpec? = null
    var tabhost : TabHost? = null
    var which_tab : Int? = null

    private var meeting_alarm_list: RecyclerView? = null
    private var meeting_alram_datas : ArrayList<AlarmData>? = null
    private var meeting_alram_adapter : AlarmAdapter? = null
    var meetingAlramData: List<AlarmMeetingData>? = null


    private var my_alarm_list: RecyclerView? = null
    private var my_alram_datas : ArrayList<AlarmData>? = null
    private var my_alram_adapter : AlarmAdapter? = null
    var myAlramData: List<AlarmMyData>? = null


    var meeting_alarm_tab : View? = null
    var my_alarm_tab : View? = null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater!!.inflate(R.layout.fragment_alarm, container, false)

        networkService = GlobalApplication.instance!!.networkService
        SharedPreferencesService.instance!!.load(context)
//
//        view = findViewById(R.id.root_view)
        v.tabhost
        v.tabhost!!.setup()


        ts1 = v.tabhost!!.newTabSpec("meeting_alarm_list")
        ts1!!.setIndicator("모임 소식")
        ts1!!.setContent(R.id.meeting_alarm_list)
        v.tabhost!!.addTab(ts1)

        ts2 = v.tabhost!!.newTabSpec("my_alarm_list")
        ts2!!.setIndicator("내 소식")
        ts2!!.setContent(R.id.my_alarm_list)
        v.tabhost!!.addTab(ts2)

        v.tabhost!!.setCurrentTabByTag("tab1")
        which_tab = 1

        v.meeting_alarm_tab
        v.my_alarm_tab

        val tv = v.tabhost!!.tabWidget.getChildAt(v.tabhost!!.getCurrentTab()).findViewById<View>(android.R.id.title) as TextView
        tv.setTextColor(Color.parseColor("#111111"))

        v.tabhost!!.setOnTabChangedListener(TabHost.OnTabChangeListener {
            for (i in 0 until v.tabhost!!.getTabWidget().getChildCount()) {
//                v.lounge_tabhost!!.getTabWidget().getChildAt(i).setBackgroundColor(this.getResources().getColor(R.color.another_tab))
                val tv = v.tabhost!!.tabWidget.getChildAt(i).findViewById<View>(android.R.id.title) as TextView
                tv.setTextColor(Color.parseColor("#bcbcbc"))
            }
//            v.lounge_tabhost!!.getTabWidget().getChildAt(v.lounge_tabhost!!.getCurrentTab()).setBackgroundColor(this.getResources().getColor(R.color.choosed_tab))
            val tv = v.tabhost!!.tabWidget.getChildAt(v.tabhost!!.getCurrentTab()).findViewById<View>(android.R.id.title) as TextView
            tv.setTextColor(Color.parseColor("#111111"))

            if(which_tab == 1){
                which_tab = 2
                alarmMeeting()
                v.meeting_alarm_tab!!.setBackgroundColor(this.getResources().getColor(R.color.another_tab))
                v.my_alarm_tab!!.setBackgroundColor(this.getResources().getColor(R.color.choosed_tab))
            }else{
                /**fixme**/
                alarmMy()
                which_tab = 1
                v.meeting_alarm_tab!!.setBackgroundColor(this.getResources().getColor(R.color.choosed_tab))
                v.my_alarm_tab!!.setBackgroundColor(this.getResources().getColor(R.color.another_tab))
            }
        })


        v.meeting_alarm_list
        v.meeting_alarm_list!!.layoutManager = LinearLayoutManager(context)

        meeting_alram_datas = ArrayList<AlarmData>()

        v.my_alarm_list
        v.my_alarm_list!!.layoutManager = LinearLayoutManager(context)

        my_alram_datas = ArrayList<AlarmData>()

        alarmMeeting()
        alarmMy()

        my_alram_adapter = AlarmAdapter(my_alram_datas, context)
        my_alram_adapter!!.setOnItemClickListener(this)
        v.my_alarm_list!!.adapter = my_alram_adapter
        return v
    }

    fun alarmMeeting(){

        val alarmMeetingResponse = networkService!!.alarmListMeeting(SharedPreferencesService.instance!!.getPrefStringData("token")!!, 401)
        alarmMeetingResponse.enqueue(object : Callback<AlarmMeetingDataResponse> {
            override fun onResponse(call: Call<AlarmMeetingDataResponse>?, response: Response<AlarmMeetingDataResponse>?) {
                if (response!!.isSuccessful) {
                    if (response!!.body().status.equals("success")) {
                        GlobalApplication.instance!!.makeToast("성공")
                        meetingAlramData = response!!.body().data

                        var size: Int = response!!.body().data!!.size
                        meeting_alram_datas!!.clear()
                        for (i in 1..size) {
                            meeting_alram_datas!!.add(AlarmData(
                                    meetingAlramData!![i - 1].noti_content,
                                    "0",
                                    meetingAlramData!![i - 1].noti_time,
                                    meetingAlramData!![i - 1].user_id,
                                    meetingAlramData!![i - 1].meeting_noti_id
                            ))
                        }
                    } else {
                        GlobalApplication.instance!!.makeToast("정보를 확인해주세요")
                    }
                }
            }

            override fun onFailure(call: Call<AlarmMeetingDataResponse>?, t: Throwable?) {
                Log.v("444", t.toString())

                GlobalApplication.instance!!.makeToast("통신 상태를 확인해주세요")
            }
        })

    }
    fun alarmMy(){

        val alarmMyResponse = networkService!!.alarmListMy(SharedPreferencesService.instance!!.getPrefStringData("token")!!, 400)
        alarmMyResponse.enqueue(object : Callback<AlarmMyDataResponse> {
            override fun onResponse(call: Call<AlarmMyDataResponse>?, response: Response<AlarmMyDataResponse>?) {
                if (response!!.isSuccessful) {
                    if (response!!.body().status.equals("success")) {
                        GlobalApplication.instance!!.makeToast("성공")
                        myAlramData = response!!.body().data

                        var size: Int = response!!.body().data!!.size
                        my_alram_datas!!.clear()
                        for (i in 1..size) {
                            my_alram_datas!!.add(AlarmData(
                                    myAlramData!![i - 1].noti_content,
                                    myAlramData!![i - 1].user_image,
                                    myAlramData!![i - 1].noti_time,
                                    myAlramData!![i - 1].user_id,
                                    myAlramData!![i - 1].my_notification_id
                            ))
                        }

                    } else {
                        GlobalApplication.instance!!.makeToast("정보를 확인해주세요")
                    }
                }
            }

            override fun onFailure(call: Call<AlarmMyDataResponse>?, t: Throwable?) {
                Log.v("444", t.toString())

                GlobalApplication.instance!!.makeToast("통신 상태를 확인해주세요")
            }
        })


    }




    override fun onClick(v: View?) {
        /*val idx : Int = loungeList!!.getChildAdapterPosition(v)
        val key : String? = loungeDatas!!.get(idx).key

        val intent = Intent(applicationContext, LoungeDetailActivity::class.java)
        intent.putExtra("key", key)
        startActivity(intent)*/
    }
}