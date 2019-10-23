package com.leonsmoke;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.Map;
import java.util.Set;

public class FullTestBean {
    private Integer fInt;
    private Float fFloat;
    private Double fDouble;
    private Byte fByte;
    private Short fShort;
    private Long fLong;
    private Boolean fBool;
    private Instant instant;
    private BigDecimal bigDecimal;
    private BigInteger bigInteger;
    private Set<TestBean> testBeanSet;

    public Set<TestBean> getTestBeanSet() {
        return testBeanSet;
    }

    public void setTestBeanSet(Set<TestBean> testBeanSet) {
        this.testBeanSet = testBeanSet;
    }

    public Integer getfInt() {
        return fInt;
    }

    public void setfInt(Integer fInt) {
        this.fInt = fInt;
    }

    public Float getfFloat() {
        return fFloat;
    }

    public void setfFloat(Float fFloat) {
        this.fFloat = fFloat;
    }

    public Double getfDouble() {
        return fDouble;
    }

    public void setfDouble(Double fDouble) {
        this.fDouble = fDouble;
    }

    public Byte getfByte() {
        return fByte;
    }

    public void setfByte(Byte fByte) {
        this.fByte = fByte;
    }

    public Short getfShort() {
        return fShort;
    }

    public void setfShort(Short fShort) {
        this.fShort = fShort;
    }

    public Long getfLong() {
        return fLong;
    }

    public void setfLong(Long fLong) {
        this.fLong = fLong;
    }

    public Boolean getfBool() {
        return fBool;
    }

    public void setfBool(Boolean fBool) {
        this.fBool = fBool;
    }

    public Instant getInstant() {
        return instant;
    }

    public void setInstant(Instant instant) {
        this.instant = instant;
    }

    public BigDecimal getBigDecimal() {
        return bigDecimal;
    }

    public void setBigDecimal(BigDecimal bigDecimal) {
        this.bigDecimal = bigDecimal;
    }

    public BigInteger getBigInteger() {
        return bigInteger;
    }

    public void setBigInteger(BigInteger bigInteger) {
        this.bigInteger = bigInteger;
    }

    @Override
    public String toString() {
        return "FullTestBean{" +
                "fInt=" + fInt +
                ", fFloat=" + fFloat +
                ", fDouble=" + fDouble +
                ", fByte=" + fByte +
                ", fShort=" + fShort +
                ", fLong=" + fLong +
                ", fBool=" + fBool +
                ", instant=" + instant +
                ", bigDecimal=" + bigDecimal +
                ", bigInteger=" + bigInteger +
                ", testBeanSet=" + testBeanSet +
                '}';
    }
}
