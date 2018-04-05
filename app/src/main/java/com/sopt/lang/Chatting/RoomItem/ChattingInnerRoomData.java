package com.sopt.lang.Chatting.RoomItem;

/**
 * Created by johee on 2018-01-07.
 */

public class ChattingInnerRoomData {
    public int your_profile;
    public String your_content;
    public String my_content;
    public String date;

    public ChattingInnerRoomData(int your_profile, String your_content, String my_content,String date) {
        this.your_profile = your_profile;
        this.your_content = your_content;
        this.my_content = my_content;
        this.date = date;
    }

//    public ChattingInnerRoomData(String date) {
//        this.date = date;
//    }
}
