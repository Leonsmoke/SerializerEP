package com.leonsmoke.modules.serialization;

import com.leonsmoke.services.ClassParser;

import java.lang.reflect.Field;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.leonsmoke.constants.Constants.*;

/**
 * Модуль сериализации объекта, представлении его в виде массива байтов
 */
public class SerializerModule {

    List<Object> innerList = new ArrayList<>();

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
        innerList.add(anyObject);
        StringBuilder sb = new StringBuilder();
        sb.append(CLASS_OPEN).append(CLASSNAME_OPEN);
        sb.append(anyObject.getClass().getName());
        sb.append(CLASSNAME_CLOSE);
        sb.append(serialize(anyObject));
        sb.append(CLASS_CLOSE);
        innerList.remove(anyObject);
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
        sb.append(CLASSFIELDS_OPEN);
        for (Field f: fields
        ) {
            sb.append(serializeField(f,object));
        }
        sb.append(CLASSFIELDS_CLOSE);
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
        sb.append(FIELD_OPEN);
        field.setAccessible(true);
        sb.append(NAME_OPEN).append(field.getName()).append(NAME_CLOSE);
        sb.append(TYPE_OPEN).append(field.getType().toString()).append(TYPE_CLOSE);
        sb.append(VALUE_OPEN);
        Object value = null;
        String type = ClassParser.checkClass(field.getType());
        value = field.get(mainObject);
        sb.append(getField(type,value));
        sb.append(VALUE_CLOSE);
        sb.append(FIELD_CLOSE);
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
                    if (innerList.contains(value)){
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
        stringBuilder.append(COLLECTION_OPEN);
        Collection<Object> newCol = (Collection)object;
        newCol.forEach((beans -> {
            stringBuilder.append(ELEMENT_OPEN);
            try {
                stringBuilder.append(start(beans));
            } catch (Exception e) {
                e.printStackTrace();
            }
            stringBuilder.append(ELEMENT_CLOSE);
        }));
        stringBuilder.append(COLLECTION_CLOSE);
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

        stringBuilder.append(COLLECTION_OPEN);
        object.forEach((key,value)->{
            stringBuilder.append(ELEMENT_OPEN);
            stringBuilder.append(KEY_OPEN);
            try {
                stringBuilder.append(TYPE_OPEN);
                stringBuilder.append(key.getClass());
                stringBuilder.append(TYPE_CLOSE);
                String type = ClassParser.checkClass(key.getClass());
                stringBuilder.append(VALUE_OPEN).append(getField(type,key)).append(VALUE_CLOSE);
            } catch (Exception e) {
                e.printStackTrace();
            }
            stringBuilder.append(KEY_CLOSE);
            stringBuilder.append(SECONDKEY_OPEN);
            try {
                stringBuilder.append(TYPE_OPEN);
                stringBuilder.append(value.getClass());
                stringBuilder.append(TYPE_CLOSE);
                String type = ClassParser.checkClass(value.getClass());
                stringBuilder.append(VALUE_OPEN);
                stringBuilder.append(getField(type,value));
                stringBuilder.append(VALUE_CLOSE);
            } catch (Exception e) {
                e.printStackTrace();
            }
            stringBuilder.append(SECONDKEY_CLOSE);
            stringBuilder.append(ELEMENT_CLOSE);
        });
        stringBuilder.append(COLLECTION_CLOSE);
        return stringBuilder.toString();
    }


}
