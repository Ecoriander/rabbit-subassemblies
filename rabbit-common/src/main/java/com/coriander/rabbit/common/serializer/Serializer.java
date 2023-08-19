package com.coriander.rabbit.common.serializer;

/**
 * $Serializer 序列化和反序列化的接口
 *
 * @author coriander
 */
public interface Serializer {

    byte[] serializeRaw(Object data);

    String serialize(Object data);

    <T> T deserialize(String content);

    <T> T deserialize(byte[] content);
}
