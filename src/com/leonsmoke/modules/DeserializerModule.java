package com.leonsmoke.modules;

import com.leonsmoke.Bean;
import com.leonsmoke.services.ClassParser;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Модуль, десериализовывающиймассив байтов в массив
 */
public class DeserializerModule {

    private int progress = 0; // прогресс-указатель на момент десериализации

    public Object start(String encoded) throws Exception {
        return deserializeBean(new StringBuilder(encoded));
    }

    /**
     * Метод, десериализовывающий бин
     * @param encoded Строка (стрингбилдер) с описанием бина
     * @return объект бина
     * @throws Exception
     */
    private Object deserializeBean(StringBuilder encoded) throws Exception{
        progress=0;
        String classname = encodeClassName(encoded);
        Object mainBean = Class.forName(classname).newInstance();
        Field[] fields = mainBean.getClass().getDeclaredFields();
        StringBuilder fieldsEncoded = new StringBuilder(findClassfields(encoded));
        for (Field field: fields
             ) {
            field.setAccessible(true);
            Object value = getFieldValue(field, fieldsEncoded);
            field.set(mainBean,value);
        }
        return mainBean;
    }

    /**
     * Метод, который ищет строковое представление полей класса
     * @param sb строковое представление класса
     * @return строковое представление полейф класса
     */
    private String findClassfields(StringBuilder sb){
        int startIndex = sb.indexOf("<classfields>");
        int endIndex = sb.indexOf("</classfields>");
        int seeker = startIndex;
        while(true){
            seeker=sb.indexOf("<classfields>",seeker+1);
            if (seeker==-1 || seeker>endIndex){
                break;
            }
            endIndex = sb.indexOf("</classfields>",endIndex+1);
        }
        return sb.substring(startIndex+13,endIndex);
    }

    /**
     * Метод, который ищет данные класса
     * @param sb строковое представление сериализованного класса
     * @param name имя класса
     * @return строковое представление данных класса
     */
    private String findBeanData(StringBuilder sb, String name){
        int startIndex = 0;
        do {
            if (name==null) break;
            startIndex = sb.indexOf("<name>"+name,startIndex+1);
        } while (progress>startIndex);
        startIndex = sb.indexOf("<value>",startIndex);
        int endIndex = sb.indexOf("</value>",startIndex);
        int seeker = startIndex;
        while(true){
            seeker=sb.indexOf("<value>",seeker+1);
            if (seeker==-1 || seeker>endIndex){
                break;
            }
            endIndex = sb.indexOf("</value>",endIndex+1);
        }
        progress=endIndex;
        return sb.substring(startIndex+7,endIndex);
    }

    /**
     * метод, для поиска строкового представления значения
     * @param sb строкове представление контекста
     * @param start начальная координата поиска
     * @return строка, со значениями
     */
    private String findValue(StringBuilder sb, int start){
        start = sb.indexOf("<value>",start);
        int endIndex = sb.indexOf("</value>",start);
        int seeker = start;
        while(true){
            seeker=sb.indexOf("<value>",seeker+1);
            if (seeker==-1 || seeker>endIndex){
                break;
            }
            endIndex = sb.indexOf("</value>",endIndex+1);
        }
        return sb.substring(start+7,endIndex);
    }

    /**
     * Метод, который ищет данные коллекции
     * @param sb
     * @param name
     * @return
     */
    private String findCollectionData(StringBuilder sb, String name){
        int startIndex = progress;
        do {
            startIndex = sb.indexOf("<name>"+name+"</name>",startIndex+1);
            if (startIndex==-1) return "null";
        } while (progress>startIndex);
        int tempProgress = progress;
        sb = new StringBuilder(findValue(sb,startIndex));
        startIndex = sb.indexOf("<collection>");
        if (startIndex==-1) return "null";
        int endIndex = sb.indexOf("</collection>",startIndex);
        progress=endIndex;
        int seeker = startIndex;
        while(true){
            seeker=sb.indexOf("<collection>",seeker+1);
            if (seeker==-1 || seeker>endIndex){
                break;
            }
            endIndex = sb.indexOf("</collection>",endIndex+1);
        }
        progress=tempProgress+endIndex;
        return sb.substring(startIndex+12,endIndex);
    }

    /**
     * метод, который получает значение поля класса
     * @param field поле класса, значение которого нужно найти
     * @param encoded строковое представление объекта
     * @return
     * @throws Exception исключения связанные с рефлексией
     */
    private Object getFieldValue(Field field, StringBuilder encoded) throws Exception{
        String name = field.getName();
        Object fieldWithValue = parseValue(encoded,name,field.getType());
        return fieldWithValue;
    }

    /**
     * Метод, который парсит значение по заданному строчному представлению
     * @param encoded строковое представление сериализованного объекта
     * @param name имя поля
     * @param type тип поля
     * @return объект - значение поля
     * @throws Exception
     */
    private Object parseValue(StringBuilder encoded, String name, Class type) throws Exception{
        Object fieldWithValue = null;
        if (Bean.class.isAssignableFrom(type)){
            StringBuilder beanData = new StringBuilder(findBeanData(encoded,name));
            int tempProgress = progress;
            if (beanData.toString().equals("null")){
                progress = tempProgress;
                return null;
            }
            fieldWithValue = deserializeBean(beanData);
            progress = tempProgress;
        } else if (isThatCollection(type)){
            StringBuilder collectionData = new StringBuilder(findCollectionData(encoded,name));
            int tempProgress = progress;
            if (collectionData.toString().equals("null")) return null;
            fieldWithValue = deserializeCollection(collectionData,type);
            progress = tempProgress;
        } else if (isThatMap(type)) {
            StringBuilder collectionData = new StringBuilder(findCollectionData(encoded,name));
            if (collectionData.toString().equals("null")) return null;
            fieldWithValue = deserializeMap(collectionData);
        } else {
            String value = parsePrimitiveValue(encoded,name);
            fieldWithValue = ClassParser.getValue(type.toString(),value);
        }
        return fieldWithValue;
    }

    /**
     * Метод десериализации коллекции
     * @param sb строковое представление сериализованного объекта
     * @param type тип коллекции
     * @return объект - коллекция
     * @throws Exception
     */
    private Object deserializeCollection(StringBuilder sb,Class type) throws Exception{
        //progress=0;
        Collection collection= null;
        if (type.toString().contains("Set")){
            collection = new HashSet<Bean>();
        }
        if (type.toString().contains("List")){
            collection = new ArrayList();
        }
        int indexStart = sb.indexOf("<class>");
        int indexEnd = sb.indexOf("</class>",indexStart);
        while (indexStart!=-1){
            Object element = deserializeBean(new StringBuilder(sb.substring(indexStart,indexEnd)));
            collection.add(element);
            indexStart = sb.indexOf("<class>",indexStart+1);
            indexEnd = sb.indexOf("</class>",indexStart+1);
        }
        return collection;
    }

    /**
     * Метод десериализации коллекции
     * @param sb строковое представление сериализованного объекта
     * @return объект - коллекция
     * @throws Exception
     */
    private Object deserializeMap(StringBuilder sb) throws Exception{
        //progress=0;
        Map mapa = new HashMap();
        int indexStart = sb.indexOf("<key>");

        /**
         * TODO не забыть подправить и изменить
         */
        while (indexStart!=-1){
            int tempIndex = sb.indexOf("<type>",indexStart)+6;
            String type = sb.substring(tempIndex,sb.indexOf("</type>",tempIndex));
            type = type.replace("class ","");
            tempIndex = sb.indexOf("<value>",tempIndex)+7;
            String value = sb.substring(tempIndex,sb.indexOf("</value>",tempIndex));
            Object key = ClassParser.getValue(type,value);
            if (key==null){
                value = "<value>"+sb.substring(tempIndex);
                StringBuilder beanData = new StringBuilder(findBeanData(new StringBuilder(value),null));
                key = deserializeBean(beanData);
            }
            tempIndex = sb.indexOf("<secondkey>",tempIndex)+11;

            tempIndex = sb.indexOf("<type>",tempIndex)+6;
            type = sb.substring(tempIndex,sb.indexOf("</type>",tempIndex));
            type = type.replace("class ","");
            tempIndex = sb.indexOf("<value>",tempIndex)+7;
            value = sb.substring(tempIndex,sb.indexOf("</value>",tempIndex)+8);
            Object secondKey = ClassParser.getValue(type,value);
            if (secondKey==null){
                value = "<value>"+sb.substring(tempIndex);
                StringBuilder beanData = new StringBuilder(findBeanData(new StringBuilder(value),null));
                secondKey = deserializeBean(beanData);
            }
            mapa.put(key,secondKey);
            indexStart = sb.indexOf("<key>",indexStart+1);
        }
        return mapa;
    }

    private boolean isThatCollection(Class type){
        return  (type.toString().contains("Set")||type.toString().contains("List"));
    }

    private boolean isThatMap(Class type){
        return  (type.toString().contains("Map"));
    }

    private String parsePrimitiveValue(StringBuilder encoded, String name){
        int indexFirst = 1;
        do {
            indexFirst = encoded.indexOf(name,indexFirst+1);
        } while (progress>indexFirst);
        int indexEnd = encoded.indexOf("</value>",indexFirst);
        indexFirst = encoded.indexOf("<value>",indexFirst)+7;
        progress=indexFirst;
        return encoded.substring(indexFirst,indexEnd);
    }

    private String encodeClassName(StringBuilder encoded){
        int indexStart = encoded.indexOf("<className>")+11;
        return encoded.substring(indexStart,encoded.indexOf("</className>"));
    }

}
