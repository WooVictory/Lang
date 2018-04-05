package com.sopt.lang.OrganizerPage

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.TabHost
import com.sopt.lang.Login.kakao.GlobalApplication
import com.sopt.lang.Meeting.MeetingParticipants.MeetingParticipantsData
import com.sopt.lang.Network.ManagementMeeting.ManagementParticipantsData
import com.sopt.lang.Network.ManagementMeeting.ManagementParticipantsResponse
import com.sopt.lang.Network.NetworkService
import com.sopt.lang.R
import com.sopt.lang.SharedPreferencesService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

/**
 * Created by user on 2018-01-12.
 */
class OrganizerPageActivity : AppCompatActivity(), View.OnClickListener {

    private var networkService: NetworkService? = null
    var view: View? = null

    var ts1: TabHost.TabSpec? = null
    var ts2: TabHost.TabSpec? = null
    var tab_host: TabHost? = null
    var which_tab: Int? = null

    private var meeting_awaiter_list: RecyclerView? = null
    private var meeting_awaiter_datas: ArrayList<MeetingParticipantsData>? = null
    private var meeting_awaiter_adapter: AwaiterAdapter? = null

    private var meeting_participants_list: RecyclerView? = null
    private var meeting_participants_datas: ArrayList<MeetingParticipantsData>? = null
    private var meeting_participants_adapter: OrganizerPageAdapter? = null

    var meeting_awaiter_tab: View? = null
    var meeting_participants_tab: View? = null
    var ManagementParticipantsData: List<ManagementParticipantsData>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_organizer_page)
        networkService = GlobalApplication.instance!!.networkService
        SharedPreferencesService.instance!!.load(this)
        ManagementParticipantsData = ArrayList<ManagementParticipantsData>()

        meeting_awaiter_datas = ArrayList<MeetingParticipantsData>()

        view = findViewById<View>(R.id.root_view)
        tab_host = findViewById<TabHost>(R.id.tabhost) as TabHost
        tab_host!!.setup()

        ts1 = tab_host!!.newTabSpec("meeting_awaiter_list")
        ts1!!.setIndicator("신청대기")
        ts1!!.setContent(R.id.meeting_awaiter_list)
        tab_host!!.addTab(ts1)

        ts2 = tab_host!!.newTabSpec("meeting_participants_list")
        ts2!!.setIndicator("참여자")
        ts2!!.setContent(R.id.meeting_participants_list)
        tab_host!!.addTab(ts2)

        tab_host!!.setCurrentTabByTag("tab1")
        which_tab = 1

        meeting_awaiter_tab = findViewById<View>(R.id.meeting_awaiter_tab) as View
        meeting_participants_tab = findViewById<View>(R.id.meeting_participants_tab) as View


        tab_host!!.setOnTabChangedListener {
            if (which_tab == 1) {
                which_tab = 2
                meeting_awaiter_tab!!.setBackgroundColor(this.getResources().getColor(R.color.another_tab))
                meeting_participants_tab!!.setBackgroundColor(this.getResources().getColor(R.color.choosed_tab))
            } else {
                which_tab = 1
                meeting_awaiter_tab!!.setBackgroundColor(this.getResources().getColor(R.color.choosed_tab))
                meeting_participants_tab!!.setBackgroundColor(this.getResources().getColor(R.color.another_tab))
            }
        }

        meeting_awaiter_list = findViewById<View>(R.id.meeting_awaiter_list) as RecyclerView
        meeting_awaiter_list!!.layoutManager = LinearLayoutManager(this)

        //      meeting_awaiter_datas = ArrayList<MeetingParticipantsData>()

        meeting_participants_list = findViewById<View>(R.id.meeting_participants_list) as RecyclerView
        meeting_participants_list!!.layoutManager = LinearLayoutManager(this)

        meeting_participants_datas = ArrayList<MeetingParticipantsData>()

        setMeetingAwaiterData()
        setMeetingParticipantsData()

    }

    fun setMeetingAwaiterData() { // 서버로 부터 데이터 받기
        Awaiter()
    }

    fun setMeetingParticipantsData() { // 서버로 부터 데이터 받기

        meeting_participants_datas = ArrayList<MeetingParticipantsData>()
        Participants()
        meeting_participants_adapter = OrganizerPageAdapter(meeting_participants_datas, this)
        meeting_participants_list!!.adapter = meeting_participants_adapter
    }

    override fun onClick(v: View?) {
        /*val idx : Int = loungeList!!.getChildAdapterPosition(v)
        val key : String? = loungeDatas!!.get(idx).key

        val intent = Intent(applicationContext, LoungeDetailActivity::class.java)
        intent.putExtra("key", key)
        startActivity(intent)*/
    }

    fun Awaiter() {
        val participantsResponse = networkService!!.getParticipants(SharedPreferencesService.instance!!.getPrefStringData("token")!!, 202, intent.getIntExtra("meeting_id", 0))
        participantsResponse.enqueue(object : Callback<ManagementParticipantsResponse> {
            override fun onResponse(call: Call<ManagementParticipantsResponse>?, response: Response<ManagementParticipantsResponse>?) {
                if (response!!.isSuccessful) {
                    if (response!!.body().status.equals("success")) {
                        GlobalApplication.instance!!.makeToast("성공")
                        ManagementParticipantsData = response!!.body().data

                        // 승인대기일때

                        var size: Int = response!!.body().data!!.size
                        meeting_awaiter_datas!!.clear()

                        //drawable mymeeting_wait

                        for (i in 1..size) {

                            if(ManagementParticipantsData!![i - 1].user_image ==null){
                                meeting_awaiter_datas!!.add(MeetingParticipantsData(
                                        ManagementParticipantsData!![i - 1].user_name,
                                        "0",
                                        0,
                                        ManagementParticipantsData!![i -1].user_id,
                                        ManagementParticipantsData!![i-1].meeting_user_id
                                ))
                            }else{
                                meeting_awaiter_datas!!.add(MeetingParticipantsData(
                                        ManagementParticipantsData!![i - 1].user_name,
                                        ManagementParticipantsData!![i - 1].user_image,
                                        0,ManagementParticipantsData!![i -1].user_id,
                                        ManagementParticipantsData!![i-1].meeting_user_id
                                ))
                            }

                        }

                    } else {
                        GlobalApplication.instance!!.makeToast("정보를 확인해주세요")
                    }
                }
            }

            override fun onFailure(call: Call<ManagementParticipantsResponse>?, t: Throwable?) {
                Log.v("444", t.toString())

                GlobalApplication.instance!!.makeToast("통신 상태를 확인해주세요")
            }
        })

        meeting_awaiter_adapter = AwaiterAdapter(meeting_awaiter_datas, this)
        meeting_awaiter_list!!.adapter = meeting_awaiter_adapter
    }


    fun Participants() {
        val participantsResponse = networkService!!.getParticipants(SharedPreferencesService.instance!!.getPrefStringData("token")!!, 203, intent.getIntExtra("meeting_id", 0))
        participantsResponse.enqueue(object : Callback<ManagementParticipantsResponse> {
            override fun onResponse(call: Call<ManagementParticipantsResponse>?, response: Response<ManagementParticipantsResponse>?) {
                if (response!!.isSuccessful) {
                    if (response!!.body().status.equals("success")) {
                        GlobalApplication.instance!!.makeToast("성공")
                        ManagementParticipantsData = response!!.body().data

                        var size: Int = response!!.body().data!!.size
                        meeting_participants_datas!!.clear()

                        for (i in 1..size) {
                            if (ManagementParticipantsData!![i - 1].approval_state == 204) {

                                if (ManagementParticipantsData!![i - 1].user_image == null) {
                                    meeting_participants_datas!!.add(MeetingParticipantsData(
                                            ManagementParticipantsData!![i - 1].user_name,
                                            "0",
                                            R.drawable.meetinginformation_organizer,
                                            ManagementParticipantsData!![i -1].user_id,
                                            ManagementParticipantsData!![i-1].meeting_user_id
                                    ))
                                } else {
                                    meeting_participants_datas!!.add(MeetingParticipantsData(
                                            ManagementParticipantsData!![i - 1].user_name,
                                            ManagementParticipantsData!![i - 1].user_image,
                                            R.drawable.meetinginformation_organizer,
                                            ManagementParticipantsData!![i -1].user_id,
                                            ManagementParticipantsData!![i-1].meeting_user_id
                                    ))
                                }

                            } else {
                                meeting_participants_datas!!.add(MeetingParticipantsData(
                                        ManagementParticipantsData!![i - 1].user_name,
                                        ManagementParticipantsData!![i - 1].user_image,
                                        0,
                                        ManagementParticipantsData!![i -1].user_id,
                                        ManagementParticipantsData!![i-1].meeting_user_id
                                ))
                            }

                        }


                    } else {
                        GlobalApplication.instance!!.makeToast("정보를 확인해주세요")
                    }
                }
            }

            override fun onFailure(call: Call<ManagementParticipantsResponse>?, t: Throwable?) {
                Log.v("444", t.toString())

                GlobalApplication.instance!!.makeToast("통신 상태를 확인해주세요")
            }
        })

    }

}






