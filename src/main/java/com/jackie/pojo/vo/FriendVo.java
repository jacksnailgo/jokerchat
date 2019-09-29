package com.jackie.pojo.vo;

/**
 * 好友列表中的好友信息
 */
public class FriendVo {

    private String friendId;
    private String friendUsername;
    private String friendFaceImage;
    private String friendNickname;

    public String getFriendId() {
        return friendId;
    }

    public String getFriendUsername() {
        return friendUsername;
    }

    public String getFriendFaceImage() {
        return friendFaceImage;
    }

    public String getFriendNickname() {
        return friendNickname;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    public void setFriendUsername(String friendUsername) {
        this.friendUsername = friendUsername;
    }

    public void setFriendFaceImage(String friendFaceImage) {
        this.friendFaceImage = friendFaceImage;
    }

    public void setFriendNickname(String friendNickname) {
        this.friendNickname = friendNickname;
    }
}
