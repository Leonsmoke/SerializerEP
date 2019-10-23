package com.leonsmoke;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.*;

public class Test {
    public static void main(String[] args) {
        Encoder encoder = new Encoder();
        TestBean innerBean = new TestBean();

        /**
         * Создание внутреннего бина
         */
        innerBean.setId(Long.MIN_VALUE);
        innerBean.setName("INNER BEAN");
        innerBean.setVals(Arrays.asList(999L,-100L,Long.MAX_VALUE));
        innerBean.setTest(false);

        Map<Integer, TestBean> map = new HashMap<>();
        map.put(1,innerBean);
        map.put(2,innerBean);
        map.put(3,innerBean);

        /**
         * Создание тестого бина
         */
        TestBean testBean = new TestBean();
        testBean.setId(Long.MAX_VALUE);
        testBean.setName("TEST");
        testBean.setVals(Arrays.asList(10L,1L,0L));
        testBean.setTest(true);
        testBean.setInnerBean(innerBean);
        testBean.setTestBeanMap(map);

        /**
         * Тестовый бин с различными полями
         */
        Set<TestBean> set = new HashSet<>();
        set.add(testBean);
        set.add(innerBean);

        FullTestBean fullTestBean = new FullTestBean();
        fullTestBean.setBigDecimal(BigDecimal.valueOf(150));
        fullTestBean.setBigInteger(BigInteger.valueOf(200));
        fullTestBean.setfBool(true);
        fullTestBean.setfDouble(Double.MAX_VALUE);
        fullTestBean.setfByte(Byte.MAX_VALUE);
        fullTestBean.setfFloat(Float.MAX_VALUE);
        fullTestBean.setfLong(Long.MAX_VALUE);
        fullTestBean.setfInt(Integer.MAX_VALUE);
        fullTestBean.setfShort(Short.MAX_VALUE);
        fullTestBean.setInstant(Instant.now());
        fullTestBean.setTestBeanSet(set);

        System.out.println(fullTestBean.toString());
        byte[] data = encoder.serialize(fullTestBean);
        System.out.println("*********ПОСЛЕ ДЕСЕРИАЛИЗАЦИИ***** !! Могут быть отличия в порядке вывода элементов коллекций в следствие особенностей их хранения внутри коллекции");
        FullTestBean testEnc = (FullTestBean)encoder.deserialize(data);
        System.out.println(testEnc.toString());
    }
}
