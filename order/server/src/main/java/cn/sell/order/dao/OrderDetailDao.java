package cn.sell.order.dao;

import cn.sell.order.model.OrderDetail;
import cn.sell.order.model.OrderMaster;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDetailDao extends JpaRepository<OrderDetail,String> {

    List<OrderDetail> findByOrderId(String orderId);
}
