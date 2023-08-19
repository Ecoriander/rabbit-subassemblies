package com.coriander.rabbit.api;

/**
 * $MessageListener 消费者监听消息
 *
 * @author coriander
 */
public interface MessageListener {

    void onMessage(Message message);
}
