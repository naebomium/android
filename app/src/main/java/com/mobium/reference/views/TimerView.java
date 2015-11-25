package com.mobium.reference.views;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 *  on 08.05.14.
 */
public class TimerView extends TextView {

    private OnStopTimerListener onStopTimerListener;
    private long timerTime;
    private long startTime;
    private Handler timerHandler = new Handler();

    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            long millis = getRemainingTime();
            if (millis <= 0) {
                onStop();
                return;
            }

            TimerView.this.setText(convertToString(millis));

            timerHandler.postDelayed(this, 200);
        }

        private void onStop() {
            if (onStopTimerListener != null) {
                onStopTimerListener.onTimerStop();
            }
        }
    };

    private String convertToString(long millis) {
        if (millis <= 0) {
            return "0:00";
        }

        int seconds = (int) (millis / 1000);
        int minutes = seconds / 60;
        seconds = seconds % 60;

        return String.format("%d:%02d", minutes, seconds);
    }

    public TimerView(Context context) {
        super(context);
    }

    public TimerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TimerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setTimerTime(long timerTime) {
        this.timerTime = timerTime;
        setText(convertToString(timerTime));
        stop();
    }

    public void stop() {
        timerHandler.removeCallbacks(timerRunnable);
    }

    public void start(long timerTime) {
        setTimerTime(timerTime);
        start();
    }

    public void start() {
        startTime = System.currentTimeMillis();
        timerHandler.postDelayed(timerRunnable, 100);
    }

    public long getRemainingTime() {
        long millis = timerTime - (System.currentTimeMillis() - startTime);
        return millis > 0 ? millis : 0;
    }

    public void setOnStopTimerListener(OnStopTimerListener onStopTimerListener) {
        this.onStopTimerListener = onStopTimerListener;
    }

    public interface OnStopTimerListener {
        void onTimerStop();
    }
}
