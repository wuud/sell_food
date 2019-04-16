package cn.sell.order.dto;

import cn.sell.order.model.OrderDetail;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 *  order传输数据
 */
@Data
public class OrderDTO {

    private String orderId;

    private String buyerName;

    private String buyerPhone;

    private String buyerAddress;

    private String buyerOpenid;

    private BigDecimal orderAmount;

    private Integer orderStatus;

    private Integer payStatus;

    private List<OrderDetail> orderDetailList;

}
