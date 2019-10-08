package com.jackie.controller;

import com.jackie.enums.FriendRequestOperationTypeEnum;
import com.jackie.enums.SearchFreindEnum;
import com.jackie.pojo.Users;
import com.jackie.pojo.bo.UsersBo;
import com.jackie.pojo.vo.FriendRequstVo;
import com.jackie.pojo.vo.FriendVo;
import com.jackie.pojo.vo.UsersVo;
import com.jackie.service.UserService;
import com.jackie.utils.FastDFSClient;
import com.jackie.utils.FileUtils;
import com.jackie.utils.JSONResult;
import com.jackie.utils.MD5Utils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
            users.setCid(user.getCid());
            users.setFaceImage("");
            users.setFaceImageBig("");
            if (user.getNickname() == null) {
                users.setNickname(user.getUsername());
            }
            users.setCreateTime(System.currentTimeMillis());
            //数据库保存用户
            result = userService.createAndSaveUsers(users);

        }
        UsersVo usersVo = new UsersVo();
        BeanUtils.copyProperties(result, usersVo);

        return JSONResult.ok(usersVo);
    }

    @PostMapping("/uploadFaceBase64")
    public JSONResult uploadFaceBase64(@RequestBody UsersBo usersBo) throws Exception {
        //获取前端传送过来的base64字符串，然后转换为文件对象再上传
        String base64Data = usersBo.getUserData();
        //定义路径
        String userFacePath = "C:\\Users\\Jackie\\Desktop\\tempPicture\\" + usersBo.getUserId() + "userface64.png";
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
        Users updateUserInfo = userService.updateUserInfo(user);
        return JSONResult.ok(updateUserInfo);
    }

    @PostMapping("/setNickName")
    public JSONResult setNickName(@RequestBody UsersBo usersBo) {
        System.out.println("修改昵称");
        String userId = usersBo.getUserId();
        //存入数据库
        Users user = new Users();
        user.setId(usersBo.getUserId());
        user.setNickname(usersBo.getUserData());
        Users updateUserInfo = userService.updateUserInfo(user);
        return JSONResult.ok(updateUserInfo);


    }

    /**
     * 根据账号匹配查询，不是模糊查询
     *
     * @param usersBo
     * @return
     */
    @PostMapping("/search")
    public JSONResult search(@RequestBody UsersBo usersBo) {
        System.out.println("搜索好友");
        String userId = usersBo.getUserId();  //自己的ID
        String friendName = usersBo.getUserData(); //搜索好友的名稱
        if ("".equals(friendName)) {
            return JSONResult.errorMsg("搜索错误");
        }
        // 0 代表查詢OK  1无此用户 2不可查询自己 3已添加好友
        Integer status = userService.preconditionsSearchFriends(userId, friendName);
        if (status == SearchFreindEnum.SUCCESS.status) {
            Users friend = userService.queryUsersByUserName(friendName);
            UsersVo usersVo = new UsersVo();
            BeanUtils.copyProperties(friend, usersVo);
            return JSONResult.ok(usersVo);
        } else {
            String msgByKey = SearchFreindEnum.getMsgByKey(status);
            return JSONResult.errorMsg(msgByKey);
        }
    }

    /**
     * 添加好友
     *
     * @param usersBo
     * @return
     */
    @PostMapping("/addFriends")
    public JSONResult requestAddFriend(@RequestBody UsersBo usersBo) {
        String myId = usersBo.getUserId();
        String friendName = usersBo.getUserData();
        if (StringUtils.isBlank(myId) || StringUtils.isBlank(friendName)) {
            return JSONResult.errorMsg("用户名为空");
        }
        //前置条件
        Integer status = userService.preconditionsSearchFriends(myId, friendName);
        if (status == SearchFreindEnum.SUCCESS.status) {
            userService.sendFriendRequest(myId, friendName);
        } else {
            String errorMsg = SearchFreindEnum.getMsgByKey(status);
            return JSONResult.errorMsg(errorMsg);
        }
        return JSONResult.ok();
    }

    @PostMapping("/getFriendsRequest")
    public JSONResult getFriendsRequest(String userId) {
        System.out.println("查看好友请求");
        if (StringUtils.isBlank(userId)) {
            return JSONResult.errorMsg("请求接收朋友申请错误");
        }
        //查询用户接收到的朋友申请
        List<FriendRequstVo> friendRequstVos = userService.queryFriendRequestList(userId);
        for (FriendRequstVo vo : friendRequstVos) {
            System.out.println("好友请求" + vo.getSendUserId() + "," + vo.getSendUserName());
        }
        return JSONResult.ok(friendRequstVos);
    }

    @PostMapping("/operateFriendRequest")
    public JSONResult operateFriendRequest(String userId, String friendId, Integer type) {
        System.out.println("操作好友请求");
        if (StringUtils.isBlank(userId)
                || StringUtils.isBlank(friendId)
                || type == null) {
            return JSONResult.errorMsg("");
        }
        //1.如果没有对应枚举值，操作失败
        if (StringUtils.isBlank(FriendRequestOperationTypeEnum.getMsgByType(type))) {
            return JSONResult.errorMsg("没有对应操作类型");
        } else if (type == FriendRequestOperationTypeEnum.ACCEPT.getType()) {
            //接受好友请求
            userService.acceptFriendRequest(userId, friendId);
        } else if (type == FriendRequestOperationTypeEnum.IGNOGRE.getType()) {
            //忽略好友请求
            userService.deleteFriendRequest(userId, friendId);
        }

        return JSONResult.ok();
    }

    @PostMapping("/getFriendsList")
    public JSONResult getFriendsList(String userId) throws Exception {
        if (StringUtils.isBlank(userId)) {
            return JSONResult.errorMsg("获取好友列表失败");
        }
        List<FriendVo> friendList = userService.getFriendsList(userId);
        return JSONResult.ok(friendList);
    }

}
