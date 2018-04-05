package com.sopt.lang.Chatting.Room;

import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by johee on 2018-01-06.
 */

public class ChattingRoomData {
    private String roomName;
    private String leaderName;
    private List<String> numberOfPeople;
    private ChatDetail content;
    private int badge;
    private String recentMessage;
    private String recentMessageTime;
    private int notiCount;
    private int totalCount;

    public ChattingRoomData() {
    }

    public ChattingRoomData(String roomName, String leaderName, List<String> numberOfPeople, ChatDetail content, int badge, String recentMessage, String recentMessageTime, int notiCount, int totalCount) {
        this.roomName = roomName;
        this.leaderName = leaderName;
        this.numberOfPeople = numberOfPeople;
        this.content = content;
        this.badge = badge;
        this.recentMessage = recentMessage;
        this.recentMessageTime = recentMessageTime;
        this.notiCount = notiCount;
        this.totalCount = totalCount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getNotiCount() {
        return notiCount;
    }

    public void setNotiCount(int notiCount) {
        this.notiCount = notiCount;
    }

    public String getRecentMessage() {
        return recentMessage;
    }

    public void setRecentMessage(String recentMessage) {
        this.recentMessage = recentMessage;
    }

    public String getRecentMessageTime() {
        return recentMessageTime;
    }

    public void setRecentMessageTime(String recentMessageTime) {
        this.recentMessageTime = recentMessageTime;
    }

    public int getBadge() {
        return badge;
    }

    public void setBadge(int badge) {
        this.badge = badge;
    }

    public ChatDetail getContent() {
        return content;
    }

    public void setContent(ChatDetail content) {
        this.content = content;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getLeaderName() {
        return leaderName;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }

    public List<String> getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(List<String> numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }
}