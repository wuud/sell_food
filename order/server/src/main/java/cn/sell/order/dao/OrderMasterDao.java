package cn.sell.order.dao;

import cn.sell.order.model.OrderMaster;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderMasterDao extends JpaRepository<OrderMaster,String> {

}
