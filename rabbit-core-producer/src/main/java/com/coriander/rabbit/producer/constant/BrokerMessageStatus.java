package com.coriander.rabbit.producer.constant;

/**
 * $BrokerMessageStatus 消息的发送状态
 *
 * @author coriander
 */
public enum BrokerMessageStatus {
    /**
     * 待确认
     */
    SENDING("0"),

    /**
     * 投递成功
     */
    SEND_OK("1"),

    /**
     * 投递失败
     */
    SEND_FAIL("2");

    private String code;

    private BrokerMessageStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }
}
