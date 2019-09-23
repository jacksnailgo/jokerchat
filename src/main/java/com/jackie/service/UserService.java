package com.jackie.service;


import com.jackie.pojo.Users;

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
}
