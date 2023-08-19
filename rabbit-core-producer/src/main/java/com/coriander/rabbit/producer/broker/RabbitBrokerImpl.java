package com.coriander.rabbit.producer.broker;

import cn.hutool.core.date.DateUtil;
import com.coriander.rabbit.api.Message;
import com.coriander.rabbit.api.MessageType;
import com.coriander.rabbit.producer.constant.BrokerMessageConst;
import com.coriander.rabbit.producer.constant.BrokerMessageStatus;
import com.coriander.rabbit.producer.entity.BrokerMessage;
import com.coriander.rabbit.producer.service.BrokerMessageService;
import com.rabbitmq.tools.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * $RabbitBrokerImpl 发送不同类型的消息实现类
 *
 * @author coriander
 */
@Slf4j
@Component
public class RabbitBrokerImpl implements RabbitBroker {

    @Autowired
    private BrokerMessageService brokerMessageService;
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
        message.setMessageType(MessageType.RELIANT);
        BrokerMessage brokerMessageInfo = brokerMessageService.selectByMessageId(message.getMessageId());
        if (brokerMessageInfo == null) {
            // 1. 把数据库的消息发送日志先记录好
            Date now = new Date();
            BrokerMessage brokerMessage = new BrokerMessage();
            brokerMessage.setMessageId(message.getMessageId());
            brokerMessage.setStatus(BrokerMessageStatus.SENDING.getCode());
            // tryCount 在最开始发送的时候不需要进行设置
            brokerMessage.setNextRetry(DateUtil.offsetMinute(now, BrokerMessageConst.TIMEOUT));
            brokerMessage.setCreateTime(now);
            brokerMessage.setUpdateTime(now);
            brokerMessage.setMessage(message);
            brokerMessageService.insertSelective(brokerMessage);
        }

        // 2. 执行真正的发送消息逻辑
        this.sendKernel(message);
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
