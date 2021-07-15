package com.malcoo.malcotask1.Utils;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;

import com.malcoo.malcotask1.Repo.LocationRepo;

public class Timer extends CountDownTimer {

    private static Timer timer;
    private static final String TAG = "Timer";
    private  Context context;
    OnTimerFinished onTimerFinished;

    public static Timer getInstance(long millisInFuture, long countDownInterv) {
        return timer==null?timer=new Timer(millisInFuture,countDownInterv):timer;
    }
    public static Timer getInstance(OnTimerFinished onTimerFinished,long millisInFuture, long countDownInterv) {

        return timer==null?timer=new Timer(onTimerFinished,millisInFuture,countDownInterv):timer;
    }

    /**
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receive
     *                          {@link #onTick(long)} callbacks.
     */
    private Timer(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        Log.d(TAG, "Timer: ");
    }
    private Timer(OnTimerFinished onTimerFinished,long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        this.onTimerFinished=onTimerFinished;
        Log.d(TAG, "Timer: ");
    }

    @Override
    public void onTick(long millisUntilFinished) {
        //Log.d(TAG, "onTick: "+millisUntilFinished);
    }

    @Override
    public void onFinish() {
        Log.d(TAG, "onFinish: ");
        LocationRepo.getInstance(context).trackLocation();
    }

     public interface OnTimerFinished{
        void onTimerFinished();
    }
}
