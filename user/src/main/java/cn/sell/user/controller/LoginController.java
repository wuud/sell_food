package cn.sell.user.controller;

import cn.sell.user.VO.ResultVO;
import cn.sell.user.constant.CookieConstant;
import cn.sell.user.constant.RedisConstant;
import cn.sell.user.enums.LoginErrorStatusEnum;
import cn.sell.user.enums.RoleEnum;
import cn.sell.user.model.UserInfo;
import cn.sell.user.service.UserInfoService;
import cn.sell.user.utils.CookieUtil;
import cn.sell.user.utils.ResultVOUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping(value = "/login")
public class LoginController {

    @Autowired
    UserInfoService userInfoService;
    @Autowired
    RedisTemplate redisTemplate;

    /**
     * 买家的登录
     */
    @RequestMapping(value = "/buyer")
    public ResultVO buyer(@RequestParam("openid") String openid,
                          HttpServletResponse response, HttpServletRequest request) {
        //根据openid拿到用户数据
        UserInfo userInfo = userInfoService.getByOpenid(openid);
        if (userInfo == null) {
            return ResultVOUtil.error(LoginErrorStatusEnum.LOGIN_FAIL);
        }
        //判断用户权限是否匹配
        if (!RoleEnum.BUYER.getCode().equals(userInfo.getRole())) {
            return ResultVOUtil.error(LoginErrorStatusEnum.ROLE_ERROR);
        }
        CookieUtil.set(response, CookieConstant.OPENID, openid, CookieConstant.expire);

        return ResultVOUtil.success();
    }

    /**
     * 卖家的登录
     */
    @RequestMapping(value = "/seller")
    public ResultVO seller(@RequestParam("openid") String openid,
                           HttpServletRequest request, HttpServletResponse response) {
        //判断是否已经登录
        Cookie cookie = CookieUtil.get(request, CookieConstant.TOKEN);
        if (cookie != null && !StringUtils.isEmpty(""+redisTemplate.opsForValue().get(
                        String.format(RedisConstant.TOKEN_TEMPLATE,cookie.getValue())))) {
            return ResultVOUtil.success();
        }
        //根据openid拿到用户数据
        UserInfo userInfo = userInfoService.getByOpenid(openid);
        if (userInfo == null) {
            return ResultVOUtil.error(LoginErrorStatusEnum.LOGIN_FAIL);
        }
        //判断用户权限是否匹配
        if (!userInfo.getRole().equals(RoleEnum.SELLER.getCode())) {
            return ResultVOUtil.error(LoginErrorStatusEnum.ROLE_ERROR);
        }

        //生成cookie，并存储到redis
        String token = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(String.format(RedisConstant.TOKEN_TEMPLATE,token),
                openid,CookieConstant.expire, TimeUnit.SECONDS);
        System.out.println(openid);
        System.out.println(redisTemplate.opsForValue().get(String.format(RedisConstant.TOKEN_TEMPLATE,token)));
        // 将cookie添加到返回头
        CookieUtil.set(response, CookieConstant.TOKEN, token, CookieConstant.expire);

        return ResultVOUtil.success();

    }
}
