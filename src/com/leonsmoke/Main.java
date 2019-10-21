package com.leonsmoke;


import java.time.Instant;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        Set<Bean> setbean = new HashSet<>();
        List<Bean> beanList = new ArrayList<>();
        TestBean testBean1 = new TestBean();
        TestBean testBean2 = new TestBean();
        testBean1.setInstant(Instant.now());
        testBean1.setSecondInt(15);
        Encoder encoder = new Encoder();
        setbean.add(testBean1);
        beanList.add(testBean1);
        beanList.add(testBean1);
        testBean2.setBeansSet(setbean);
        testBean2.setBeansList(beanList);
        byte[] baits = encoder.serialize(testBean2);
        Object object = encoder.deserialize(baits);
        System.out.println();
    }

}
