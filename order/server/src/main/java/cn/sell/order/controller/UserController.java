package cn.sell.order.controller;

import cn.sell.order.configuration.UserConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    UserConfig userConfig;

    @GetMapping("/getConfig")
    public String get(){
        return userConfig.toString();
    }

}
