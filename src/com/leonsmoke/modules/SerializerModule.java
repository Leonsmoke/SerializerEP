package com.leonsmoke.modules;

import com.leonsmoke.Bean;
import com.leonsmoke.services.ClassParser;

import java.beans.Beans;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Модуль сериализации объекта, представлении его в виде массива байтов
 */
public class SerializerModule {

    List<Bean> innerList = new ArrayList<>();

    /**
     * Метод старта пройесса сериализации
     * @param bean - бин для сериализации
     * @return возвращает строкое представление сериализованного объекта
     * @throws Exception
     */
    public String start(Object bean) throws Exception{
        StringBuilder sb = new StringBuilder();
        sb.append(serializeBean(bean));
        return sb.toString();
    }

    /**
     *
     * @param anyObject бин, который необходимо сериализовать
     * @return возвращает полностью сериализованный бин
     * @throws Exception
     */
    private String serializeBean(Object anyObject) throws Exception{
        innerList.add((Bean)anyObject);
        StringBuilder sb = new StringBuilder();
        sb.append("<class>").append("<className>");
        sb.append(anyObject.getClass().getName());
        sb.append("</className>");
        sb.append(serialize(anyObject));
        sb.append("</class>");
        innerList.remove((Bean)anyObject);
        return sb.toString();
    }

    /**
     *
     * @param object
     * @return возвращает сериализованный объект
     * @throws Exception
     */
    private String serialize(Object object) throws Exception {
        StringBuilder sb = new StringBuilder();
        Field[] fields = object.getClass().getDeclaredFields();
        sb.append("<classfields>");
        for (Field f: fields
        ) {
            sb.append(serializeField(f,object));
        }
        sb.append("</classfields>");
        return sb.toString();
    }

    /**
     *
     * @param field // поле для сериализации
     * @param mainObject // объект, из которого достается это поле
     * @return возвращает сериализованное поле
     * @throws Exception
     */
    private String serializeField(Field field, Object mainObject) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("<field>");
        field.setAccessible(true);
        sb.append("<name>").append(field.getName()).append("</name>");
        sb.append("<type>").append(field.getType().toString()).append("</type>");
        sb.append("<value>");
        Object value = null;
        String type = ClassParser.checkClass(field.getType());
        value = field.get(mainObject);
        sb.append(getField(type,value));
        sb.append("</value>");
        sb.append("</field>");
        return sb.toString();
    }

    /**
     * Метод, в котором определяется тип поля, его значение, выполняется сериализация
     * @param type // тип получаемого поля
     * @param value // значение этого поля
     * @return возвращает сериализованное значение поля
     * @throws Exception возможно зацикливание, в данном случае будет вызвана исключительная ситуация
     */
    private Object getField(String type, Object value) throws Exception{
        switch (type){
            case "bean":
                if (value!=null){
                    if (innerList.contains((Bean)value)){
                        throw new Exception("Error. Looping!");
                    } else {
                        return serializeBean(value);
                    }
                }
                break;
            case "collection":
                if (value!=null){
                    return (collectionSerialize(value));
                }
                break;
            case "map":
                if (value!=null){
                    return(mapSerialize((Map)value));
                }
                break;
            case "parse":
            case "primitive":
            case "number":
            case "string":
            case "none":
                if (value!=null){
                    return(value.toString());
                }

        }
        return null;
    }

    /**
     * Сериализует коллекцию(list,set)
     * @param object сама коллекция
     * @return сериализованная строка
     * @throws Exception
     */
    public String collectionSerialize(Object object) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<collection>");
        Collection<Bean> newCol = (Collection)object;
        newCol.forEach((beans -> {
            stringBuilder.append("<element>");
            try {
                stringBuilder.append(start(beans));
            } catch (Exception e) {
                e.printStackTrace();
            }
            stringBuilder.append("</element>");
        }));
        stringBuilder.append("</collection>");
        return stringBuilder.toString();
    }

    /**
     * Сериализует мапу
     * @param object сама мапа
     * @return сериализованная строка
     * @throws Exception
     */
    public String mapSerialize(Map object){
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("<collection>");
        object.forEach((key,value)->{
            stringBuilder.append("<element>");
            stringBuilder.append("<key>");
            try {
                stringBuilder.append("<type>");
                stringBuilder.append(key.getClass());
                stringBuilder.append("</type>");
                String type = ClassParser.checkClass(key.getClass());
                stringBuilder.append("<value>").append(getField(type,key)).append("</value>");
            } catch (Exception e) {
                e.printStackTrace();
            }
            stringBuilder.append("</key>");
            stringBuilder.append("<secondkey>");
            try {
                stringBuilder.append("<type>");
                stringBuilder.append(value.getClass());
                stringBuilder.append("</type>");
                String type = ClassParser.checkClass(value.getClass());
                stringBuilder.append("<value>");
                stringBuilder.append(getField(type,value));
                stringBuilder.append("</value>");
            } catch (Exception e) {
                e.printStackTrace();
            }
            stringBuilder.append("</secondkey>");
            stringBuilder.append("</element>");
        });
        stringBuilder.append("</collection>");
        return stringBuilder.toString();
    }


}
