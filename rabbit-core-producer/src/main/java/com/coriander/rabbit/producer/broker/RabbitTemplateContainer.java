package com.coriander.rabbit.producer.broker;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.coriander.rabbit.api.Message;
import com.coriander.rabbit.api.MessageType;
import com.coriander.rabbit.common.convert.GenericMessageConverter;
import com.coriander.rabbit.common.convert.RabbitMessageConverter;
import com.coriander.rabbit.common.serializer.Serializer;
import com.coriander.rabbit.common.serializer.SerializerFactory;
import com.coriander.rabbit.common.serializer.impl.JacksonSerializerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * $RabbitTemplateContainer 池化RabbitTemplate封装
 *
 * <p>每一个topic 对应一个RabbitTemplate
 * <p>1.	提高发送的效率
 * <p>2. 	可以根据不同的需求制定化不同的RabbitTemplate, 比如每一个topic 都有自己的routingKey规则
 *
 * @author coriander
 */
@Slf4j
@Component
public class RabbitTemplateContainer implements RabbitTemplate.ConfirmCallback {

    private Map<String /* TOPIC */, RabbitTemplate> rabbitMap = MapUtil.newConcurrentHashMap();

    private SerializerFactory serializerFactory = JacksonSerializerFactory.INSTANCE;

    @Autowired
    private ConnectionFactory connectionFactory;

    public RabbitTemplate getTemplate(Message message) {
        if (message == null) throw new NullPointerException();

        String topic = message.getTopic();
        RabbitTemplate rabbitTemplate = rabbitMap.get(topic);
        if (rabbitTemplate != null) {
            return rabbitTemplate;
        }
        log.info("#RabbitTemplateContainer.getTemplate# topic: {} is not exists, create one", topic);

        RabbitTemplate newTemplate = new RabbitTemplate(connectionFactory);
        newTemplate.setExchange(topic);
        newTemplate.setRoutingKey(message.getRoutingKey());
        newTemplate.setRetryTemplate(new RetryTemplate());

        // 添加序列化反序列化和converter对象
        Serializer serializer = serializerFactory.create();
        GenericMessageConverter gmc = new GenericMessageConverter(serializer);
        RabbitMessageConverter rmc = new RabbitMessageConverter(gmc);
        newTemplate.setMessageConverter(rmc);

        String messageType = message.getMessageType();
        // 非迅速消息,都需要做消息应答
        if (!MessageType.RAPID.equals(messageType)) {
            newTemplate.setConfirmCallback(this);
        }

        rabbitMap.putIfAbsent(topic, newTemplate);

        return rabbitMap.get(topic);
    }


    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (correlationData == null || StrUtil.isBlank(correlationData.getId())) return;

        // 具体的消息应答
        List<String> strings = Arrays.stream(correlationData.getId().split("#")).collect(Collectors.toList());


        String messageId = strings.get(0);
        long sendTime = Long.parseLong(strings.get(1));
        String messageType = strings.get(2);

        if (ack) {
            // 当Broker 返回ACK成功时, 就是更新一下日志表里对应的消息发送状态为 SEND_OK

            log.info("send message is OK, confirm messageId: {}, sendTime: {}", messageId, sendTime);
        } else {
            log.error("send message is Fail, confirm messageId: {}, sendTime: {}", messageId, sendTime);
        }
    }
}
