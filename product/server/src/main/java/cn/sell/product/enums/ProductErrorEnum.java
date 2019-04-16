package cn.sell.product.enums;

import lombok.Getter;

@Getter
public enum  ProductErrorEnum {
    PRODUCT_NOT_EXIST(0,"商品不存在"),
    PRODUCT_STOCK_ERROR(1,"商品库存不足")
    ;

    private int code;

    private String msg;

    ProductErrorEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
