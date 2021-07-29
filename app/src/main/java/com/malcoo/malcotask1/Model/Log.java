package com.malcoo.malcotask1.Model;

import android.annotation.SuppressLint;

import java.util.Objects;

public class Log {

    long enteringTime;
    public Long leavingTime=null;

    public Log() {
    }

    public Log(long enteringTime, long leavingTime) {
        this.enteringTime = enteringTime;
        this.leavingTime = leavingTime;
    }

    public long getEnteringTime() {
        return enteringTime;
    }

    public void setEnteringTime(long enteringTime) {
        this.enteringTime = enteringTime;
    }

    public Long getLeavingTime() {
        return leavingTime;
    }

    public void setLeavingTime(long leavingTime) {
        this.leavingTime = leavingTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Log log = (Log) o;
        return enteringTime == log.enteringTime;
    }

    @SuppressLint("NewApi")
    @Override
    public int hashCode() {
        return Objects.hash(enteringTime);
    }
}
