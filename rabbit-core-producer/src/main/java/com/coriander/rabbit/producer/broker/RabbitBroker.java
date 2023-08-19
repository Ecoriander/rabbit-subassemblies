package com.coriander.rabbit.producer.broker;

import com.coriander.rabbit.api.Message;

/**
 * $RabbitBroker 具体发送不同种类型消息的接口
 *
 * @author coriander
 */
public interface RabbitBroker {

    /**
     * 发送快速消息
     */
    void rapidSend(Message message);

    /**
     * 发送确认消息
     */
    void confirmSend(Message message);

    /**
     * 发送可靠性消息
     */
    void reliantSend(Message message);

}
