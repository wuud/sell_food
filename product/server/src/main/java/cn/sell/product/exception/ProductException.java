package cn.sell.product.exception;

import cn.sell.product.enums.ProductErrorEnum;

public class ProductException extends RuntimeException {


    private int code;

    public ProductException() {
    }

    public ProductException(String message) {
        super(message);
    }

    public ProductException(ProductErrorEnum productErrorEnum){
        super(productErrorEnum.getMsg());
        this.code=productErrorEnum.getCode();
    }
}
