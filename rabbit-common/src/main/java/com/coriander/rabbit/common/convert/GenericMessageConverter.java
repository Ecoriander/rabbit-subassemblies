package com.coriander.rabbit.common.convert;

import com.coriander.rabbit.common.serializer.Serializer;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;

/**
 * $GenericMessageConverter
 *
 * @author coriander
 */
public class GenericMessageConverter implements MessageConverter {
    private Serializer serializer;

    public GenericMessageConverter(Serializer serializer) {
        if (serializer == null) throw new NullPointerException();
        this.serializer = serializer;
    }

    @Override
    public Object fromMessage(org.springframework.amqp.core.Message message) throws MessageConversionException {
        return this.serializer.deserialize(message.getBody());
    }

    @Override
    public org.springframework.amqp.core.Message toMessage(Object object, MessageProperties messageProperties) throws MessageConversionException {
        return new org.springframework.amqp.core.Message(this.serializer.serializeRaw(object), messageProperties);
    }
}
