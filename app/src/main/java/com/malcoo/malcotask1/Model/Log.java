package com.malcoo.malcotask1.Model;

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
}
