package com.jackie.service.impl;

import com.jackie.enums.SearchFreindEnum;
import com.jackie.mapper.FriendsRequestMapper;
import com.jackie.mapper.MyFriendsMapper;
import com.jackie.mapper.UsersMapper;
import com.jackie.mapper.UsersMapperCustom;
import com.jackie.pojo.FriendsRequest;
import com.jackie.pojo.MyFriends;
import com.jackie.pojo.Users;
import com.jackie.pojo.vo.FriendRequstVo;
import com.jackie.service.UserService;
import com.jackie.utils.FastDFSClient;
import com.jackie.utils.FileUtils;
import com.jackie.utils.QRCodeUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImp implements UserService {
    @Autowired
    private UsersMapper usersMapper;
    @Autowired
    private MyFriendsMapper myFriendsMapper;
    @Autowired
    private FriendsRequestMapper friendsRequestMapper;
    @Autowired
    private Sid sid;
    @Autowired
    private QRCodeUtils qrCodeUtils;
    @Autowired
    private FastDFSClient fastDFSClient;
    @Autowired
    private UsersMapperCustom usersMapperCustom;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean queryUserNameExist(String username) {
        Users u = new Users();
        u.setUsername(username);
        //查询工作,根据username查询
        Users result = usersMapper.selectOne(u);
        return result != null ? true : false;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users queryUserForLogin(String username, String password) {

        Example example = new Example(Users.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username", username);
        criteria.andEqualTo("password", password);
        Users result = usersMapper.selectOneByExample(example);
        return result;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Users createAndSaveUsers(Users users) {
        //用户ID
        String userId = sid.nextShort();

        String qrcodePath = "C://Users//Jackie//Desktop//tempPicture//qrcode//" + userId + "qrcode64.png";
        qrCodeUtils.createQRCode(qrcodePath, "wechat_qrcode:" + users.getUsername());
        MultipartFile qrcodeFile = FileUtils.fileToMultipart(qrcodePath);
        String qrcodeURL = "";
        try {
            qrcodeURL = fastDFSClient.uploadFile(qrcodeFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        users.setQrcode(qrcodeURL);
        users.setId(userId);
        usersMapper.insert(users);
        return users;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Users updateUserInfo(Users user) {
        usersMapper.updateByPrimaryKeySelective(user);
        return queryById(user.getId());
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public Users queryById(String userId) {
        return usersMapper.selectByPrimaryKey(userId);

    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Integer preconditionsSearchFriends(String myUserId, String userName) {
        Users user = queryUsersByUserName(userName);
        //1.用户不存在，返回无此用户
        if (user == null) {
            return SearchFreindEnum.USER_NOT_EXIST.status;
        }

        //2.用户是自己
        if (user.getId() == myUserId) {
            return SearchFreindEnum.USER_YOUR_SELF.status;
        }
        //3.用户已添加好友  从friends表中查询到了
        Example example = new Example(MyFriends.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("myUserId", myUserId);
        criteria.andEqualTo("myFriendUserId", user.getId());
        MyFriends myFriendsRealtionShip = myFriendsMapper.selectOneByExample(example);
        if (myFriendsRealtionShip != null) {
            return SearchFreindEnum.USER_ALREADY_ADDED.status;
        }
        return SearchFreindEnum.SUCCESS.status;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users queryUsersByUserName(String username) {
        Example ue = new Example(Users.class);
        Example.Criteria criteria = ue.createCriteria();
        criteria.andEqualTo("username", username);
        return usersMapper.selectOneByExample(ue);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void sendFriendRequest(String myId, String friendName) {

        Users friend = queryUsersByUserName(friendName);

        //查询好友记录表
        Example example = new Example(FriendsRequest.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("sendUserId", myId);
        criteria.andEqualTo("acceptUserId", friend.getId());
        FriendsRequest friendsRequest = friendsRequestMapper.selectOneByExample(example);

        if (friendsRequest == null) {
            //如果不是你的好友，并且好友记录没有添加，新增好友记录
            String requestId = sid.nextShort();
            FriendsRequest request = new FriendsRequest();
            request.setId(requestId);
            request.setAcceptUserId(friend.getId());
            request.setSendUserId(myId);
            request.setRequestDateTime(new Date());
            friendsRequestMapper.insert(request);

        }
    }

    public List<FriendRequstVo> queryFriendRequestList(String acceptUserId) {
        return usersMapperCustom.queryFriendRequestList(acceptUserId);

    }


}
