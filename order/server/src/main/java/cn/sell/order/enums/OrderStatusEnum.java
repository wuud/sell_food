package cn.sell.order.enums;

import lombok.Getter;

@Getter
public enum OrderStatusEnum {
    NEW(0,"新建"),
    FINISHED(1,"已支付"),
    CANCLE(2,"已取消")
    ;


    private Integer code;

    private String msg;

    OrderStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
