package com.jackie.enums;

public enum FriendRequestOperationTypeEnum {
    IGNOGRE(0, "忽略好友请求"),
    ACCEPT(1, "通过好友请求"),

    ;
    private Integer type;
    private String msg;

    FriendRequestOperationTypeEnum(Integer type, String msg) {
        this.type = type;
        this.msg = msg;
    }

    public Integer getType() {
        return type;
    }

    public static String getMsgByType(Integer type) {
        for (FriendRequestOperationTypeEnum operationTypeEnum : FriendRequestOperationTypeEnum.values()) {
            if (operationTypeEnum.getType() == type) {
                return operationTypeEnum.msg;
            }
        }
        return null;
    }
}
