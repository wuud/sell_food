package cn.sell.order.utils;

import cn.sell.order.dto.OrderDTO;
import cn.sell.order.form.FormOrder;
import cn.sell.order.model.OrderDetail;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ConveterUtil {

    public static OrderDTO orderFormToDTO(FormOrder formOrder) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setBuyerName(formOrder.getName());
        orderDTO.setBuyerAddress(formOrder.getAddress());
        orderDTO.setBuyerOpenid(formOrder.getOpenid());
        orderDTO.setBuyerPhone(formOrder.getPhone());
        List<OrderDetail> orderDetailList = null;
        Gson gson = new Gson();
        try {
            orderDetailList = gson.fromJson(formOrder.getItems(), new TypeToken<List<OrderDetail>>() {
            }.getType());
        } catch (Exception e) {
            log.error("JSON 转换错误，{}", formOrder.getItems());
        }
        orderDTO.setOrderDetailList(orderDetailList);

        return orderDTO;
    }
}
