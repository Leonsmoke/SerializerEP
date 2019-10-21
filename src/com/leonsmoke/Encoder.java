package com.leonsmoke;

import java.io.Serializable;

public class Encoder implements SuperEncoder {

    /**
     * При вызове метода начнется сериализация объекта
     * @param anyBean объект для сериализации
     * @return
     */
    @Override
    public byte[] serialize(Object anyBean) {
        Serializator serializator = new Serializator();
        return serializator.startSerialize(anyBean).getBytes();
    }

    /**
     * При вызове метода начнется десериализация объекта
     * @param data сериализованный объект
     * @return
     */
    @Override
    public Object deserialize(byte[] data) {
        Serializator serializator = new Serializator();
        return serializator.startDeserialize(data);
    }
}
