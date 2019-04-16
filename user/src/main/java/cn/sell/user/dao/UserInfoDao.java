package cn.sell.user.dao;

import cn.sell.user.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInfoDao extends JpaRepository<UserInfo,String> {

    UserInfo findByOpenid(String openid);
}
