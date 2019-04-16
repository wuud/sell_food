package cn.sell.user.enums;

import lombok.Getter;

@Getter
public enum LoginErrorStatusEnum {
    LOGIN_FAIL(1,"登录失败"),
    ROLE_ERROR(2,"角色权限有误")

    ;

    private Integer code;

    private String msg;


    LoginErrorStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
