package com.jackie.mapper;

import com.jackie.pojo.Users;
import com.jackie.pojo.vo.FriendRequstVo;
import com.jackie.utils.MyMapper;

import java.util.List;

public interface UsersMapperCustom extends MyMapper<Users> {

    public List<FriendRequstVo> queryFriendRequestList(String acceptUserId);
}
