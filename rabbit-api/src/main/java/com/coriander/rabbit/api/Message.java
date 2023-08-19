package com.coriander.rabbit.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * $Message 消息体
 *
 * @author coriander
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Message implements Serializable {
    private static final long serialVersionUID = -4775637962750879131L;

    /**
     * 消息的唯一ID
     */
    @Builder.Default
    private String messageId = UUID.randomUUID().toString();

    /**
     * 消息的主题
     */
    private String topic;

    /**
     * 消息的路由规则
     */
    @Builder.Default
    private String routingKey = "";

    /**
     * 消息的附加属性
     */

    @Builder.Default
    private Map<String, Object> attributes = new HashMap<>();

    /**
     * 延迟消息的参数配置
     */
    private int delayMills;

    /**
     * 消息类型：默认为confirm消息类型
     */
    @Builder.Default
    private String messageType = MessageType.CONFIRM;


    public Message(String messageId, String topic, String routingKey, Map<String, Object> attributes, int delayMills) {
        this.messageId = messageId;
        this.topic = topic;
        this.routingKey = routingKey;
        this.attributes = attributes;
        this.delayMills = delayMills;
    }
}
