package com.coriander.rabbit.common.serializer.impl;

import com.coriander.rabbit.api.Message;
import com.coriander.rabbit.common.serializer.Serializer;
import com.coriander.rabbit.common.serializer.SerializerFactory;

/**
 * $JacksonSerializerFactory
 *
 * @author coriander
 */
public class JacksonSerializerFactory implements SerializerFactory {

    /**
     * 饥饿加载模式,创建单例对象
     */
    public static final SerializerFactory INSTANCE = new JacksonSerializerFactory();

    @Override
    public Serializer create() {
        return JacksonSerializer.createParametricType(Message.class);
    }
}
