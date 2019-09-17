package com.jackie.service.impl;

import com.jackie.mapper.UsersMapper;
import com.jackie.pojo.Users;
import com.jackie.service.UserService;
import org.apache.catalina.User;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

@Service
public class UserServiceImp implements UserService {
    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private Sid sid;

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


        //TODO  为每个用户生成二维码
        users.setQrcode("");
        users.setId(userId);
        users.setNickname("");
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
    private Users queryById(String userId) {
        return usersMapper.selectByPrimaryKey(userId);

    }

}
