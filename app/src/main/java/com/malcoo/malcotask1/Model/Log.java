package com.malcoo.malcotask1.Model;

public class Log {

    long enteringTime=-1;
    long leavingTime=-1;

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

    public long getLeavingTime() {
        return leavingTime;
    }

    public void setLeavingTime(long leavingTime) {
        this.leavingTime = leavingTime;
    }
}
