package com.leonsmoke.services;

import java.beans.Beans;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collections;
import java.util.Map;

public class ClassParser {

    public static String checkClass(Class clazz){
        switch (clazz.toString()){
            case "int":
            case "float":
            case "double":
            case "byte":
            case "boolean":
            case "short":
            case "long":
            case "char":
                return "primitive";
            case "interface java.util.Set":
            case "interface java.util.List":
                return "collection";
            case "interface java.util.Map":
                return "map";
            case "class java.time.Instant":
                return "instant";
            default:
                return getType(clazz);
        }
    }

    private static String getType(Class clazz){
        if (Collections.class.isAssignableFrom(clazz)){
            return "collection";
        }
        if (Number.class.isAssignableFrom(clazz)){
            return "number";
        }
        if (String.class.isAssignableFrom(clazz)){
            return "string";
        }
        if (Map.class.isAssignableFrom(clazz)){
            return "map";
        }
        return "bean";
    }

    private static String hasParseMethod(Class clazz){
        String parseMethodName = "none";
        for (Method m: clazz.getDeclaredMethods()
        ) {
            if(m.getName().toLowerCase().contains("parse") && m.getParameterCount()==1){
                parseMethodName = "parse";
                break;
            }
        }
        return parseMethodName;
    }



    private static String changeNull(String value){
        if (value.equals("null") || value==null){
            value = "0";
        }
        return value;
    }

    public static Object getValue(String type, String value){
        if (value.equals("null")) return null;
        switch (type){
            case "int":
            case "class java.lang.Integer":
                return new Integer(value);
            case "float":
            case "class java.lang.Float":
                return new Float(value);
            case "double":
            case "class java.lang.Double":
                return new Double(value);
            case "byte":
            case "class java.lang.Byte":
                return new Byte(value);
            case "boolean":
            case "class java.lang.Boolean":
                return new Boolean(value);
            case "short":
            case "class java.lang.Short":
                return new Short(value);
            case "long":
            case "class java.lang.Long":
                return new Long(value);
            case "char":
            case "class java.lang.Character":
                return new Character(value.charAt(0));
            case "class java.lang.String":
                return value;
            case "class java.math.BigDecimal":
                Integer integer = new Integer(value);
                return BigDecimal.valueOf(integer);
            case "class java.time.Instant":
                if (value==null) return null;
                return Instant.parse(value);
            default:
                return null;
        }
    }
}