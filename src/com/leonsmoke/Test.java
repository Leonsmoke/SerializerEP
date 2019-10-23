package com.leonsmoke;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Test {
    public static void main(String[] args) {
        Encoder encoder = new Encoder();
        TestBean innerBean = new TestBean();

        /**
         * Создание внутреннего бина
         */
        innerBean.setId(Long.MIN_VALUE);
        innerBean.setName("INNER BEAN");
        //innerBean.setVals(Arrays.asList(999L,-100L,Long.MAX_VALUE));
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

        System.out.println(testBean.toString());
        byte[] data = encoder.serialize(testBean);
        Object testEnc = encoder.deserialize(data);
        System.out.println(testEnc.toString());
    }
}
