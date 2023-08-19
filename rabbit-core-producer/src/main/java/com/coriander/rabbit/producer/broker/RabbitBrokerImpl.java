package com.coriander.rabbit.producer.broker;

import com.coriander.rabbit.api.Message;
import com.coriander.rabbit.api.MessageType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * $RabbitBrokerImpl 发送不同类型的消息实现类
 *
 * @author coriander
 */
@Slf4j
@Component
public class RabbitBrokerImpl implements RabbitBroker {

    @Autowired
    private RabbitTemplateContainer rabbitTemplateContainer;

    @Override
    public void rapidSend(Message message) {
        message.setMessageType(MessageType.RAPID);
        this.sendKernel(message);
    }

    @Override
    public void confirmSend(Message message) {
        message.setMessageType(MessageType.CONFIRM);
        this.sendKernel(message);
    }

    @Override
    public void reliantSend(Message message) {

    }

    /**
     * $sendKernel 发送消息的核心方法 使用异步线程池进行发送消息
     *
     * @param message 消息体
     */
    private void sendKernel(Message message) {
        AsyncBaseQueue.submit(() -> {
            CorrelationData correlationData = new CorrelationData(
                    String.format("%s#%s#%s",
                            message.getMessageId(),
                            System.currentTimeMillis(),
                            message.getMessageType()));
            String topic = message.getTopic();
            String routingKey = message.getRoutingKey();
            RabbitTemplate rabbitTemplate = rabbitTemplateContainer.getTemplate(message);
            rabbitTemplate.convertAndSend(topic, routingKey, message, correlationData);
            log.info("#RabbitBrokerImpl.sendKernel# send to rabbitmq, messageId: {}", message.getMessageId());
        });
    }

}
