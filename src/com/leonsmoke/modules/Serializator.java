package com.leonsmoke.modules;

import com.leonsmoke.modules.serialization.DeserializerModule;
import com.leonsmoke.modules.serialization.SerializerModule;

public class Serializator {

    static SerializerModule beanModule = new SerializerModule();
    static DeserializerModule deserializer = new DeserializerModule();
    public Serializator() {
    }

    public String startSerialize(Object bean){
        String answer = "";
        try {
            answer=beanModule.start(bean);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return answer;
    }

    public Object startDeserialize(byte[] encode){
        Object bean = null;
        try {
            bean = deserializer.start(new String(encode));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bean;
    }


}
