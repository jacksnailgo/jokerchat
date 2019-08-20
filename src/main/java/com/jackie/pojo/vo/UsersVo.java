package com.jackie.pojo.vo;

import javax.persistence.Column;
import javax.persistence.Id;

/*
后端向前端传送实体的类
 */
public class UsersVo {

    private String id;
    private String username;
    // private String password;
    private String faceImage;
    private String faceImageBig;
    private String nickname;
    private String qrcode;
    //  private String cid;


    public void setId(String id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFaceImage(String faceImage) {
        this.faceImage = faceImage;
    }

    public void setFaceImageBig(String faceImageBig) {
        this.faceImageBig = faceImageBig;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getFaceImage() {
        return faceImage;
    }

    public String getFaceImageBig() {
        return faceImageBig;
    }

    public String getNickname() {
        return nickname;
    }

    public String getQrcode() {
        return qrcode;
    }
}