package com.jackie.netty;

import java.io.Serializable;

public class ChatMsg implements Serializable {

    private String senderId;   //发送者用户ID
    private String receiverId;//接收者用户ID
    private String msg;       //消息内容
    private String msgId;      //用于消息的签收

    public String getSenderId() {
        return senderId;
    }
    public String getReceiverId() {
        return receiverId;
    }
    public String getMsg() {
        return msg;
    }
    public String getMsgId() {
        return msgId;
    }
    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }
    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }
}
