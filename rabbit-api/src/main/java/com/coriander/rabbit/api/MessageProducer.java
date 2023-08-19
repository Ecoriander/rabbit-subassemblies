package com.coriander.rabbit.api;

import com.coriander.rabbit.api.exception.MessageRunTimeException;

import java.util.List;

/**
 * $MessageProducer
 *
 * @author coriander
 */
public interface MessageProducer {

    /**
     * Send.
     * 消息发送 附带SendCallback回调执行响应的业务逻辑处理
     *
     * @param message      the message
     * @param sendCallback to send callback
     * @throws MessageRunTimeException the message run time exception
     */
    void send(Message message, SendCallback sendCallback) throws MessageRunTimeException;

    /**
     * Send.
     * 消息发送
     *
     * @param message the message
     * @throws MessageRunTimeException the message run time exception
     */
    void send(Message message) throws MessageRunTimeException;

    /**
     * Send.
     * 消息批量发送
     *
     * @param messages the messages
     * @throws MessageRunTimeException the message run time exception
     */
    void send(List<Message> messages) throws MessageRunTimeException;
}
