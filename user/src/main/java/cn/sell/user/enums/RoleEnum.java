package cn.sell.user.enums;

import lombok.Getter;

@Getter
public enum RoleEnum {
    BUYER(1,"买家"),
    SELLER(2,"卖家")
    ;

    private Integer code;

    private String msg;

    RoleEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
