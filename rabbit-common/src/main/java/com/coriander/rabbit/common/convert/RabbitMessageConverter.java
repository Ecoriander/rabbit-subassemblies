package com.coriander.rabbit.common.convert;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;

/**
 * $RabbitMessageConverter
 * 装饰者模式,真实的对象被代理的对象做了一层包装
 *
 * @author coriander
 */
public class RabbitMessageConverter implements MessageConverter {

    private GenericMessageConverter delegate;

    private final String defaultExpire = String.valueOf(24 * 60 * 60 * 1000);

    public RabbitMessageConverter(GenericMessageConverter genericMessageConverter) {
        if (genericMessageConverter == null) throw new NullPointerException();
        this.delegate = genericMessageConverter;
    }

    @Override
    public Message toMessage(Object object, MessageProperties messageProperties) throws MessageConversionException {
        // 可以在此处将自己的业务逻辑设置到messageProperties中

        // 比如说设置消息默认过期时间
        // messageProperties.setExpiration(defaultExpire);

        com.coriander.rabbit.api.Message message = (com.coriander.rabbit.api.Message) object;
        messageProperties.setDelay(message.getDelayMills());
        return this.delegate.toMessage(object, messageProperties);
    }

    @Override
    public Object fromMessage(Message message) throws MessageConversionException {
        com.coriander.rabbit.api.Message msg = (com.coriander.rabbit.api.Message) this.delegate.fromMessage(message);
        return msg;
    }
}
