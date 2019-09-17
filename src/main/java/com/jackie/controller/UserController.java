package com.jackie.controller;

import com.jackie.pojo.Users;
import com.jackie.pojo.bo.UsersBo;
import com.jackie.pojo.vo.UsersVo;
import com.jackie.service.UserService;
import com.jackie.utils.FastDFSClient;
import com.jackie.utils.FileUtils;
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
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("u")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private FastDFSClient fastDFSClient;
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

    @PostMapping("/uploadFaceBase64")
    public JSONResult uploadFaceBase64(@RequestBody UsersBo usersBo) throws Exception {
        //获取前端传送过来的base64字符串，然后转换为文件对象再上传
        String base64Data = usersBo.getUserData();
        //定义路径
        String userFacePath = "C:\\" + usersBo.getUserId() + "userface64.png";
        FileUtils.base64ToFile(userFacePath, base64Data);
        MultipartFile multipartFile = FileUtils.fileToMultipart(userFacePath);
        //返回相应的文件地址,fastDFS存放了大图和小图
        /**
         大图地址：123i1yhfaef13.png  小图地址：123i1yhfaef13_80x80.png*/
        String url = fastDFSClient.uploadBase64(multipartFile);
        System.out.println(url);
        //获取缩略图的url
        String thump = "_80x80.";
        String[] strings = url.split("\\.");
        String thumpUrl = strings[0] + thump + strings[1];

        //存入数据库
        Users user = new Users();
        user.setId(usersBo.getUserId());
        user.setFaceImage(thumpUrl);
        user.setFaceImageBig(url);

        userService.updateUserInfo(user);
        return JSONResult.ok(user);
    }

}
