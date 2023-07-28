package com.czq.apiorder.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单状态枚举类
 */
public enum OrderStatusEnum {

    TOBEPAID("未支付", 0),

    DONE("已支付", 1),
    FAILURE("支付超时", 2);

    private final String text;

    private final int value;

    OrderStatusEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 获取值列表
     *
     * @return
     */
    public static List<Integer> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    public int getValue() {
        return value;
    }

    public String getText() {
        return text;
    }
}
