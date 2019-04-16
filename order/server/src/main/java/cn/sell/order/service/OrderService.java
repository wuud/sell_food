package cn.sell.order.service;

import cn.sell.order.Exception.OrderException;
import cn.sell.order.dao.OrderDetailDao;
import cn.sell.order.dao.OrderMasterDao;
import cn.sell.order.dto.OrderDTO;
import cn.sell.order.enums.OrderErrorEnum;
import cn.sell.order.enums.OrderStatusEnum;
import cn.sell.order.enums.PayStatusEnum;
import cn.sell.order.model.OrderDetail;
import cn.sell.order.model.OrderMaster;
import cn.sell.order.utils.KeyUtil;
import cn.sell.product.client.ProductClient;
import cn.sell.product.common.DecreaseStockInput;
import cn.sell.product.common.ProductInfoOutput;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    OrderDetailDao orderDetailDao;
    @Autowired
    OrderMasterDao orderMasterDao;
    @Autowired
    ProductClient feignProductClient;

    /**
     * 查询商品信息(调用商品服务)
     * 计算总价
     * 扣库存(调用商品服务)
     * 订单入库
     */
    public OrderDTO createOrder(OrderDTO orderDTO) {
        //每调用一次这个方法生成一个唯一的订单id
        String orderId = KeyUtil.genUniqueKey();

        // 查询商品信息(调用商品服务)
        List<String> list = orderDTO.getOrderDetailList().stream()
                .map(OrderDetail::getProductId)
                .collect(Collectors.toList());
        List<ProductInfoOutput> productInfoList = feignProductClient.getListForOrder(list);
        // 计算总价
        BigDecimal orderAmount = new BigDecimal(BigInteger.ZERO);
        for (OrderDetail orderDetail : orderDTO.getOrderDetailList()) {
            for (ProductInfoOutput productInfo : productInfoList) {
                if (productInfo.getProductId().equals(orderDetail.getProductId())) {
                    orderAmount = orderAmount.add(productInfo.getProductPrice()
                            .multiply(new BigDecimal(orderDetail.getProductQuantity())));
                    BeanUtils.copyProperties(productInfo, orderDetail);
                    //订单详情入库
                    orderDetail.setOrderId(orderId);
                    orderDetail.setDetailId(KeyUtil.genUniqueKey());
                    orderDetailDao.save(orderDetail);
                }
            }
        }
        // 扣库存(调用商品服务)
        List<DecreaseStockInput> decreaseStockInputList = orderDTO.getOrderDetailList().stream()
                .map(e -> new DecreaseStockInput(e.getProductId(), e.getProductQuantity()))
                .collect(Collectors.toList());
        feignProductClient.decrProductStock(decreaseStockInputList);

        //订单入库
        OrderMaster orderMaster = new OrderMaster();
        orderDTO.setOrderId(orderId);
        BeanUtils.copyProperties(orderDTO, orderMaster);
        orderMaster.setOrderAmount(orderAmount);
        orderMaster.setOrderStatus(OrderStatusEnum.NEW.getCode());
        orderMaster.setPayStatus(PayStatusEnum.WAIT.getCode());
        orderMasterDao.save(orderMaster);

        return orderDTO;
    }

    public OrderDTO finish(String orderid) {
        //首先查找OrderMaster
        Optional<OrderMaster> op = orderMasterDao.findById(orderid);
        if (!op.isPresent()) {
            throw new OrderException(OrderErrorEnum.ORDER_NOT_EXIST);
        }
        //判断订单状态
        OrderMaster orderMaster = op.get();
        if (!OrderStatusEnum.NEW.getCode().equals(orderMaster.getOrderStatus())) {
            throw new OrderException(OrderErrorEnum.ORDER_STATUS_ERROR);
        }
        //修改订单状态为已完结
        orderMaster.setOrderStatus(OrderStatusEnum.FINISHED.getCode());
        orderMasterDao.save(orderMaster);

        //查询订单详情
        List<OrderDetail> orderDetailList = orderDetailDao.findByOrderId(orderid);
        if (CollectionUtils.isEmpty(orderDetailList)) {
            throw new OrderException(OrderErrorEnum.ORDER_DETAIL_NOT_EXIST);
        }
        //设置返回对象
        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(orderMaster, orderDTO);
        orderDTO.setOrderDetailList(orderDetailList);

        return orderDTO;
    }

}
