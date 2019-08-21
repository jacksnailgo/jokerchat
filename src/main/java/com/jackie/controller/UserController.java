package com.jackie.controller;

import com.jackie.pojo.Users;
import com.jackie.pojo.vo.UsersVo;
import com.jackie.service.UserService;
import com.jackie.utils.JSONResult;
import com.jackie.utils.MD5Utils;
import org.apache.catalina.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("u")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/registerOrLogin")
    public JSONResult registerOrLogin(@RequestBody Users user) throws Exception {
        System.out.println(user.getUsername() + "进入.");
        //0.判断用户名和密码不能为空
        if (StringUtils.isBlank(user.getUsername())
                || StringUtils.isBlank(user.getPassword())) {
            return JSONResult.errorMsg("用户名或密码不能为空!");
        }
        //数据库交互，拿到用户数据信息
        //1.判断用户名是否存在，登录/注册
        boolean userNameExist = userService.queryUserNameExist(user.getUsername());
        Users result = null;
        if (userNameExist) {
            //1.1登录
            result = userService.queryUserForLogin(user.getUsername(), MD5Utils.getMD5Str(user.getPassword()));
            if (result == null) {
                return JSONResult.errorMsg("密码错误!");
            }
        } else {
            //1.2注册
            Users users = new Users();
            users.setUsername(user.getUsername());
            users.setPassword(MD5Utils.getMD5Str(user.getPassword()));
            users.setFaceImage("");
            users.setFaceImageBig("");
            //数据库保存用户
            result = userService.createAndSaveUsers(users);

        }
        UsersVo usersVo = new UsersVo();
        BeanUtils.copyProperties(result, usersVo);
        System.out.println();

        return JSONResult.ok(usersVo);
    }

}
