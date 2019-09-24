package com.jackie.enums;

public enum SearchFreindEnum {
    SUCCESS(0, "OK"),
    USER_NOT_EXIST(1, "无此用户"),
    USER_YOUR_SELF(2, "不可查询自己"),
    USER_ALREADY_ADDED(3, "已添加好友"),

    ;

    public Integer status;
    private String msg;

    SearchFreindEnum(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public static String getMsgByKey(Integer status) {
        for (SearchFreindEnum searchFreindEnum : SearchFreindEnum.values()) {
            if (status == searchFreindEnum.status) {
                return searchFreindEnum.msg;
            }
        }
        return null;
    }
}
