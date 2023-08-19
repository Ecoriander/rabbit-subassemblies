package com.coriander.rabbit.producer.broker;

import cn.hutool.core.util.StrUtil;
import com.coriander.rabbit.api.Message;
import com.coriander.rabbit.api.MessageProducer;
import com.coriander.rabbit.api.MessageType;
import com.coriander.rabbit.api.SendCallback;
import com.coriander.rabbit.api.exception.MessageRunTimeException;
import com.mysql.cj.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * $ProducerClient 发送消息的实际实现类
 *
 * @author coriander
 */
@Component
public class ProducerClient implements MessageProducer {

    @Autowired
    private RabbitBroker rabbitBroker;


    @Override
    public void send(Message message, SendCallback sendCallback) throws MessageRunTimeException {

    }

    @Override
    public void send(Message message) throws MessageRunTimeException {
        if (message == null || StrUtil.isBlank(message.getTopic())) throw new NullPointerException();

        // 根据不同的消息类型,做不同的处理
        String messageType = message.getMessageType();
        switch (messageType) {
            // 迅速消息
            case MessageType.RAPID:
                rabbitBroker.rapidSend(message);
                break;

            // 确认消息
            case MessageType.CONFIRM:
                rabbitBroker.confirmSend(message);
                break;

            // 可靠性消息
            case MessageType.RELIANT:
                rabbitBroker.reliantSend(message);
                break;

            default:
                break;
        }
    }

    @Override
    public void send(List<Message> messages) throws MessageRunTimeException {

    }
}
