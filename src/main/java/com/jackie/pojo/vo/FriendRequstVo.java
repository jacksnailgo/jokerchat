package com.jackie.pojo.vo;

/**
 * 发送方的信息
 */
public class FriendRequstVo {

    private String sendUserId;
    private String sendUserName;
    private String sendFaceImage;
    private String sendNickname;

    public String getSendUserId() {
        return sendUserId;
    }

    public String getSendUserName() {
        return sendUserName;
    }

    public String getSendFaceImage() {
        return sendFaceImage;
    }

    public String getSendNickname() {
        return sendNickname;
    }

    public void setSendUserId(String sendUserId) {
        this.sendUserId = sendUserId;
    }

    public void setSendUserName(String sendUserName) {
        this.sendUserName = sendUserName;
    }

    public void setSendFaceImage(String sendFaceImage) {
        this.sendFaceImage = sendFaceImage;
    }

    public void setSendNickname(String sendNickname) {
        this.sendNickname = sendNickname;
    }
}