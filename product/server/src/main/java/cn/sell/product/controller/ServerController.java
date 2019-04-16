package cn.sell.product.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@RestController
public class ServerController {

    @RequestMapping(value = "/test",method = RequestMethod.POST)
    public String rest(String openid, HttpServletRequest request){
        System.out.println("test");
        System.out.println(openid);
        Cookie[] cookies=request.getCookies();
        if(cookies==null){
            System.out.println("cookies is null");
            return "null";
        }
        for(Cookie cookie:cookies){
            System.out.println("Name: "+cookie.getName()+" Value: "+cookie.getValue());
        }
        return "ok";
    }
}
