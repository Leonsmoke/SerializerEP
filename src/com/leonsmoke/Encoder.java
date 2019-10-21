package com.leonsmoke;

import com.leonsmoke.modules.Serializator;

/**
 * Author: Melnikov Dmitrii
 *
 * Сериализатор Java Beans, на вход получает объект класса, в полях которого могут быть:
 * Все примитивные типы и ссылочные оболочки над ними, BigDecimal, Instant, другие объекты Bean, коллекции с этими объектами
 */
public class Encoder implements SuperEncoder {

    /**
     * При вызове метода начнется сериализация объекта
     * @param anyBean объект для сериализации
     * @return сериализованный объект, в виде массива байтов
     */
    @Override
    public byte[] serialize(Object anyBean) {
        Serializator serializator = new Serializator();
        return serializator.startSerialize(anyBean).getBytes();
    }

    /**
     * При вызове метода начнется десериализация объекта
     * @param data сериализованный объект
     * @return десериализованный объект
     */
    @Override
    public Object deserialize(byte[] data) {
        Serializator serializator = new Serializator();
        return serializator.startDeserialize(data);
    }
}
