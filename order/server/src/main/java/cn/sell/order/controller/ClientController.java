package cn.sell.order.controller;


import cn.sell.product.client.ProductClient;
import cn.sell.product.common.DecreaseStockInput;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;


/**
 * 调用product服务的指定端口
 */
@RestController
@Slf4j
public class ClientController {

    @Autowired
    ProductClient feignProductClient;
//    @Autowired
//    RestTemplate restTemplate;

    @RequestMapping(value = "/test")
    public String test(){
        //1.直接使用RestTemplate访问写死的URL
        // String resp=restTemplate.getForObject("http://localhost:8081/test",String.class);

        // 2.使用端点名来访问，需要使用@LoadBalanced注解
        // String resp=restTemplate.getForObject("http://PRODUCT/test",String.class);

        //3.使用FeighClient
        feignProductClient.decrProductStock(
                Arrays.asList(new DecreaseStockInput("1",2),
                        new DecreaseStockInput("2",2)));
        return "ok";
    }


}
