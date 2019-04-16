package cn.sell.order.Exception;

import cn.sell.order.enums.OrderErrorEnum;

public class OrderException extends RuntimeException {

    private Integer code;

    public OrderException() {
        super();
    }

    public OrderException(String message) {
        super(message);
    }
    public OrderException(OrderErrorEnum orderErrorEnum){
        super(orderErrorEnum.getMsg());
        this.code=orderErrorEnum.getCode();
    }
}
