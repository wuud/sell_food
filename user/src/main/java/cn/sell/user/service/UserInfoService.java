package cn.sell.user.service;

import cn.sell.user.dao.UserInfoDao;
import cn.sell.user.model.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserInfoService {

    @Autowired
    UserInfoDao userInfoDao;

    public UserInfo getByOpenid(String openid){
        return userInfoDao.findByOpenid(openid);
    }
}
