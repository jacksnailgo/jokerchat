package com.jackie.service.impl;

import com.jackie.mapper.UsersMapper;
import com.jackie.pojo.Users;
import com.jackie.service.UserService;
import com.jackie.utils.FastDFSClient;
import com.jackie.utils.FileUtils;
import com.jackie.utils.QRCodeUtils;
import org.apache.catalina.User;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import java.io.IOException;

@Service
public class UserServiceImp implements UserService {
    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private Sid sid;
    @Autowired
    private QRCodeUtils qrCodeUtils;
    @Autowired
    private FastDFSClient fastDFSClient;

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


}
