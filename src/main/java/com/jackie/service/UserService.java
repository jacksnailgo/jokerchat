package com.jackie.service;


import com.jackie.pojo.Users;
import com.jackie.pojo.vo.FriendRequstVo;
import com.jackie.pojo.vo.FriendVo;

import java.util.List;

public interface UserService {

    /*@Description: 判断用户名是否存在
     * @Param: [username]
     * @Date: 2019/8/20
     */
    public boolean queryUserNameExist(String username);

    /*@Description: 查询用户是否存在，返回用户
     * @Param: [username, password]
     * @return: com.jackie.pojo.Users
     * @Author: Jackie
     * @Date: 2019/8/20
     */
    public Users queryUserForLogin(String username, String password);

    /*@Description: 注册创建用户
     * @Param: [username, password]
     * @return: com.jackie.pojo.Users
     * @Author: Jackie
     * @Date: 2019/8/20
     */
    public Users createAndSaveUsers(Users users);

    /**
     * 修改用户记录
     */
    Users updateUserInfo(Users users);

    Users queryById(String id);

    Integer preconditionsSearchFriends(String myUserId, String friendUserId);

    Users queryUsersByUserName(String username);

    /**
     * 添加好友请求记录，保存到数据库
     */
    void sendFriendRequest(String myId, String friendName);

    List<FriendRequstVo> queryFriendRequestList(String acceptUserId);

    /**
     * 删除好友请求
     */
    int deleteFriendRequest(String sendId, String accepterId);

    /**
     * 通过好友请求1.保存好友 2.逆向保存好友 3.删除好友请求记录
     */
    void acceptFriendRequest(String sendId, String accepterId);

    List<FriendVo> getFriendsList(String userId);
}
