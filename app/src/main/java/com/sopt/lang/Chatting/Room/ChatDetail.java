package com.sopt.lang.Chatting.Room;

import android.support.annotation.Nullable;

/**
 * Created by user on 2018-01-13.
 */

public class ChatDetail {

    private String senderName;
    private String senderToken;
    /*FIXME
    임시로 구분자를 fcm 토큰으로 설정 실제로는 사용자 토큰으로 이용
     */
    private String content;
    private int mediaType;
    private String fileName;

    public ChatDetail() {
    }

    public ChatDetail(String senderName, String senderToken, String content, int mediaType, @Nullable String fileName) {
        this.senderName = senderName;
        this.senderToken = senderToken;
        this.content = content;
        this.mediaType = mediaType;
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderToken() {
        return senderToken;
    }

    public void setSenderToken(String senderToken) {
        this.senderToken = senderToken;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getMediaType() {
        return mediaType;
    }

    public void setMediaType(int mediaType) {
        this.mediaType = mediaType;
    }
}