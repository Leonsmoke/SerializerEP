package com.leonsmoke;

import java.beans.Beans;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TestBean implements Bean {

    private int firstInt;
    private Integer secondInt;
    private BigDecimal bigDecimal;
    private Bean bean;
    private char aChar;
    private Instant instant;
    private boolean falseli;
    private List beansList;
    private Set beansSet;


    public List getBeansList() {
        return beansList;
    }

    public void setBeansList(List beansList) {
        this.beansList = beansList;
    }

    public Set getBeansSet() {
        return beansSet;
    }

    public void setBeansSet(Set beansSet) {
        this.beansSet = beansSet;
    }

    public boolean isFalseli() {
        return falseli;
    }

    public void setFalseli(boolean falseli) {
        this.falseli = falseli;
    }

    public char getaChar() {
        return aChar;
    }

    public void setaChar(char aChar) {
        this.aChar = aChar;
    }

    public Instant getInstant() {
        return instant;
    }

    public void setInstant(Instant instant) {
        this.instant = instant;
    }

    public Bean getBean() {
        return bean;
    }

    public void setBean(Bean bean) {
        this.bean = bean;
    }

    public int getFirstInt() {
        return firstInt;
    }

    public void setFirstInt(int firstInt) {
        this.firstInt = firstInt;
    }

    public Integer getSecondInt() {
        return secondInt;
    }

    public void setSecondInt(Integer secondInt) {
        this.secondInt = secondInt;
    }

    public BigDecimal getBigDecimal() {
        return bigDecimal;
    }

    public void setBigDecimal(BigDecimal bigDecimal) {
        this.bigDecimal = bigDecimal;
    }
}
