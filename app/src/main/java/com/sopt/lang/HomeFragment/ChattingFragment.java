package com.sopt.lang.HomeFragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.sopt.lang.Chatting.ChattingRoom;
import com.sopt.lang.Chatting.Recommend.ChattingRecommendAdapter;
import com.sopt.lang.Chatting.Recommend.ChattingRecommendData;
import com.sopt.lang.Chatting.Room.ChattingRoomData;
import com.sopt.lang.Chatting.RoomListAdapter;
import com.sopt.lang.Chatting.User;
import com.sopt.lang.Login.kakao.GlobalApplication;
import com.sopt.lang.Network.Chatting.FriendRecommendDatas;
import com.sopt.lang.Network.Chatting.FriendRecommendResult;
import com.sopt.lang.Network.NetworkService;
import com.sopt.lang.R;
import com.sopt.lang.SharedPreferencesService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ChattingFragment extends Fragment {
    RecyclerView roomListRcv;                      //recyclerView생성
    List<ChattingRoomData> roomList;           //recyclerView에 들어갈 Data
    RecyclerView recommend_recyclerView;
    List<ChattingRecommendData> recommendData;
    LinearLayoutManager recommendLayoutManager;//recyclerView의 레이아웃
    RoomListAdapter roomListAdapter;
    ChattingRecommendAdapter recommendAdapter;
    List<String> nop;
    String name;
    NetworkService server;

    ArrayList<FriendRecommendDatas> datas;

    ImageView recommend_friends;
    boolean isrecommend = false;
    DatabaseReference reference, user;
    public static final String SERVER_KEY = "AAAAfmPK2OQ:APA91bFn9ay9MSPUptniUxnV9Gv_alSU1HLzI7T0uUxye8EiRl21g3_sH-Prtuw5d2t-OSxpcDB58sDaLKFLNNt5LiIQMNnBFjv-2SApC9UfgqeTGnBtjcQ7cWmQTVu1hjMA_O3zWTx7";


    public ChattingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        server = GlobalApplication.Companion.getInstance().getNetworkService();

        View view = inflater.inflate(R.layout.fragment_chatting, container, false);
        SharedPreferencesService.Companion.getInstance().load(getContext());
        Context context = view.getContext();

        recommend_friends = (ImageView)view.findViewById(R.id.recommend_friends);
        recommend_recyclerView = (RecyclerView) view.findViewById(R.id.chatting_recommend);
        roomListRcv = (RecyclerView) view.findViewById(R.id.chatting_item);

        recommend_recyclerView = (RecyclerView) view.findViewById(R.id.chatting_recommend);
        datas= new ArrayList<FriendRecommendDatas>();
        recommendAdapter = new ChattingRecommendAdapter(datas);
        recommendLayoutManager = new LinearLayoutManager(context);

        recommend_recyclerView.setHasFixedSize(true);             //각 item의 크기가 일정할 경우 고정
        recommendLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);    //HORIZONTAL 설정
        recommend_recyclerView.setLayoutManager(recommendLayoutManager);  //recyclerView의 레이아웃은 mLayoutManager
        recommend_recyclerView.setAdapter(recommendAdapter);

        Call<FriendRecommendResult> friendRecommendResponse = server.friendRecommend(SharedPreferencesService.Companion.getInstance().getPrefStringData("token"));
        friendRecommendResponse.enqueue(new Callback<FriendRecommendResult>() {
            @Override
            public void onResponse(Call<FriendRecommendResult> call, Response<FriendRecommendResult> response) {
                if (response.isSuccessful()) {// 응답코드 200
                    // datas = response.body().data;
                    datas.addAll(response.body().data);

                    Log.d("LoginTest", "요청메시지:" +"성공");
//                    findid = FindIdResult.result.userId;
//                    isFindIdSuccess = FindIdResult.message.equals("ID exists") ? true : false;
//                    //message=  FindIdResult.message;
                }
            }

            @Override
            public void onFailure(Call<FriendRecommendResult> call, Throwable t) {
                Log.d("LoginTest", "요청메시지:" + t.toString());
            }
        });

        recommendAdapter.notifyDataSetChanged();      //data채우고 adapter갱신
        recommend_recyclerView.setAdapter(recommendAdapter);

        recommend_friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isrecommend = !isrecommend;
                if (isrecommend) {
                    recommend_recyclerView.setVisibility(View.VISIBLE);
                    recommend_friends.setImageResource(R.drawable.chatting_recommend_arrow_up);
                } else {
                    recommend_friends.setImageResource(R.drawable.chatting_recommend_arrow_down);
                    recommend_recyclerView.setVisibility(View.GONE);
                }
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        user = FirebaseDatabase.getInstance().getReference("user");

//        SharedPreferencesService.Companion.getInstance().setPrefData("chat", "임수정");

        reference = FirebaseDatabase.getInstance()
                .getReference("roomList");
        setRecycler();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                roomList.clear();


                for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                    ChattingRoomData tempData = new ChattingRoomData();
                    tempData = dataSnapshot2.getValue(ChattingRoomData.class);
                    List<String> nop = (List<String>) dataSnapshot2.getValue(ChattingRoomData.class).getNumberOfPeople();
                    for(int i =0; i<nop.size(); i++){
                        if(SharedPreferencesService.Companion.getInstance().getPrefStringData("chat").equals(nop.get(i)))
                            roomList.add(tempData);
                    }
                }

                Log.i("room count", roomList.size() + "");
                roomListAdapter.updateList(roomList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    public View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final int pos = roomListRcv.getChildAdapterPosition(view);

            FirebaseMessaging.getInstance().subscribeToTopic("LANG");

            reference.child(roomList.get(pos).getRoomName())
                    .child("numberOfPeople")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            Iterator<DataSnapshot> child = dataSnapshot.getChildren().iterator();
                            nop = new ArrayList<String>();

                            while (child.hasNext()) {

                                if (child.next().getValue().equals(SharedPreferencesService.Companion.getInstance().getPrefStringData("fcm_token"))) {
                                    return;
                                }
                            }
                            if(dataSnapshot.getValue() != null)
                                nop = (List<String>) dataSnapshot.getValue();

                            nop.add(SharedPreferencesService.Companion.getInstance().getPrefStringData("fcm_token"));

                            reference.child(roomList.get(pos).getRoomName()).child("numberOfPeople").setValue(nop);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


            Intent intent = new Intent(getContext(), ChattingRoom.class);
            intent.putExtra("chat_room_name", roomList.get(pos).getRoomName());
            intent.putExtra("chat_user_name", name);
            startActivity(intent);
        }
    };

    public void setRecycler() {
        roomList = new ArrayList<>();
        roomListRcv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        roomListAdapter = new RoomListAdapter(roomList, onClickListener);
        roomListRcv.setAdapter(roomListAdapter);



    }

}