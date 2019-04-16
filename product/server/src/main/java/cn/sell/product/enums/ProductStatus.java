package cn.sell.product.enums;

import lombok.Getter;

@Getter
public enum ProductStatus {
    UP(0, "正在上架"),
    DOWN(1, "已下架");

    private int code;
    private String message;

    ProductStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

    ProductStatus() {
    }
}
