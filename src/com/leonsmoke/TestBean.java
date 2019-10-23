package com.leonsmoke;

import java.util.List;
import java.util.Map;

public class TestBean {
    private long id;
    private String name;
    private Boolean isTest;
    private TestBean innerBean;
    private List<Long> Vals;
    private Map<Integer, TestBean> testBeanMap;
    public TestBean getInnerBean() {
        return innerBean;
    }

    public Map<Integer, TestBean> getTestBeanMap() {
        return testBeanMap;
    }

    public void setTestBeanMap(Map<Integer, TestBean> testBeanMap) {
        this.testBeanMap = testBeanMap;
    }

    public void setInnerBean(TestBean innerBean) {
        this.innerBean = innerBean;
    }

    public Boolean getTest() {
        return isTest;
    }

    public void setTest(Boolean test) {
        isTest = test;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Long> getVals() {
        return Vals;
    }

    public void setVals(List<Long> vals) {
        Vals = vals;
    }

    @Override
    public String toString() {
        String inner = "null";
        if (innerBean!=null){
            inner = innerBean.toString();
        }
        return "TestBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", isTest=" + isTest +
                ", innerBean=" + inner +
                ", Vals=" + Vals +
                ", Map=" + testBeanMap+
                '}';
    }
}
