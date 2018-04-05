package com.sopt.lang.Chatting.RoomItem;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.sopt.lang.Chatting.ChattingDetailActivity;
import com.sopt.lang.R;

import java.util.ArrayList;

public class ChattingRoomActivity extends AppCompatActivity {
    RecyclerView roomItemRecyclerView;                      //recyclerView생성
    ArrayList<ChattingInnerRoomData> innerRoomData;           //recyclerView에 들어갈 Data
    LinearLayoutManager innerroomLayoutManager;
    ChattingInnerRoomAdapter innerroomAdapter;
    ImageView chatting_information;
    public static boolean isDate = true;
    public static boolean ismyContent = true;
    public static boolean isyourContent = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting_room);


        roomItemRecyclerView = (RecyclerView) findViewById(R.id.chatting_room_item);
        chatting_information = (ImageView)findViewById(R.id.chatting_information);
        innerRoomData = new ArrayList<ChattingInnerRoomData>();
        innerroomAdapter = new ChattingInnerRoomAdapter(innerRoomData);
        innerroomLayoutManager = new LinearLayoutManager(this);

        roomItemRecyclerView.setHasFixedSize(true);             //각 item의 크기가 일정할 경우 고정
        innerroomLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);    //veirtical로 설정
        roomItemRecyclerView.setLayoutManager(innerroomLayoutManager);  //recyclerView의 레이아웃은 mLayoutManager

        if (isDate) {
            innerRoomData.add(new ChattingInnerRoomData(R.drawable.pic1,"","","12.22"));
            Log.d("isdata1","idata1");
        }
        if(isyourContent){
            innerRoomData.add(new ChattingInnerRoomData(R.drawable.pic2,"하이하이하이","","12.22"));
            Log.d("isdata1","idata1");
        }
        if(ismyContent){
            innerRoomData.add(new ChattingInnerRoomData(R.drawable.pic3,"","하이하이하이","12.22"));
            Log.d("isdata1","idata1");
        }

        innerroomAdapter.notifyDataSetChanged();      //data채우고 adapter갱신
        roomItemRecyclerView.setAdapter(innerroomAdapter);
        Log.d("isdata1","idata1");
        Log.d("count", String.valueOf(innerroomAdapter.getItemCount()));

        chatting_information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChattingRoomActivity.this, ChattingDetailActivity.class);
                startActivity(intent);
            }
        });

    }
}
