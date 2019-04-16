package cn.sell.order.controller;

import cn.sell.order.Exception.OrderException;
import cn.sell.order.VO.ResultVO;
import cn.sell.order.VO.ResultVOUtil;
import cn.sell.order.dto.OrderDTO;
import cn.sell.order.form.FormOrder;
import cn.sell.order.service.OrderService;
import cn.sell.order.utils.ConveterUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/order")
public class OrderController {

    @Autowired
    OrderService orderService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResultVO createOrder(@Valid FormOrder formOrder,
                                BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error("订单创建错误");
            throw new OrderException(bindingResult.getFieldError().getDefaultMessage());
        }
        OrderDTO orderDTO = ConveterUtil.orderFormToDTO(formOrder);

        if (CollectionUtils.isEmpty(orderDTO.getOrderDetailList())) {
            log.error("购物车信息为空");
            throw new OrderException("购物车信息为空");
        }
        OrderDTO res = orderService.createOrder(orderDTO);
        Map<String, String> map = new HashMap<>();
        map.put("orderId", res.getOrderId());
        return ResultVOUtil.success(map);
    }

    @RequestMapping(value = "/finish")
    public ResultVO<OrderDTO> finish(@RequestParam("orderid")String orderid){
        return ResultVOUtil.success(orderService.finish(orderid));
    }
}
