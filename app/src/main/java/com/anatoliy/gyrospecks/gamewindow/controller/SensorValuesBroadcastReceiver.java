package com.anatoliy.gyrospecks.gamewindow.controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;

import com.anatoliy.gyrospecks.base.controller.MainActivityController;

import java.util.ArrayList;

/**
 * Date: 27.05.2017
 * Time: 18:21
 *
 * @author Anatoliy
 */

public class SensorValuesBroadcastReceiver extends BroadcastReceiver {
    private final static String SENSOR_VALUES_BROADCAST_RECEIVER
            = "com.anatoliy.accelspecks.controller.SensorValuesBroadcastReceiver";

    private final static String SENSOR_VALUES_KEY = SENSOR_VALUES_BROADCAST_RECEIVER + ".valuesKey";
    //From 10 to 0
    private final static int SENSITIVITY = 7;

    private final static int MAX_OBSERVERS = 1;

    private final ArrayList<GameFragmentController> observers = new ArrayList<>(MAX_OBSERVERS);


    @Override
    public void onReceive(final Context context, final Intent intent) {
        if (intent.getAction().equals(SENSOR_VALUES_KEY)) {
            final float[] values = intent.getFloatArrayExtra(SENSOR_VALUES_KEY);

            final float xValue = values[0];
            final float yValue = values[1];

            if (xValue > SENSITIVITY) {
                stepLeft();
            } else if (xValue < -SENSITIVITY) {
                stepRight();
            } else if (yValue > SENSITIVITY) {
                stepBottom();
            } else if (yValue < -SENSITIVITY) {
                stepUp();
            }

            secondPassed();
        }
    }

    void addObserver(final GameFragmentController game) {
        if (!observers.contains(game)) {
            observers.add(game);
        }
    }

    void removeObserver(final GameFragmentController game) {
        if (observers.contains(game)) {
            observers.remove(game);
        }
    }

    static String getSensorValuesKey() {
        return SENSOR_VALUES_KEY;
    }

    private void stepLeft() {
        for (int i = 0, observersSize = observers.size(); i < observersSize; i++) {
            final GameFragmentController game = observers.get(i);
            game.step(Steps.LEFT);
        }
    }

    private void stepRight() {
        for (int i = 0, observersSize = observers.size(); i < observersSize; i++) {
            final GameFragmentController game = observers.get(i);
            game.step(Steps.RIGHT);
        }
    }

    private void stepUp() {
        for (int i = 0, observersSize = observers.size(); i < observersSize; i++) {
            final GameFragmentController game = observers.get(i);
            game.step(Steps.UP);
        }
    }

    private void stepBottom() {
        for (int i = 0, observersSize = observers.size(); i < observersSize; i++) {
            final GameFragmentController game = observers.get(i);
            game.step(Steps.BOTTOM);
        }
    }

    private void secondPassed() {
        for (int i = 0, observersSize = observers.size(); i < observersSize; i++) {
            final GameFragmentController game = observers.get(i);
            game.secondPassed();
        }
    }

    static void sendBroadcast(@NonNull final String key, @NonNull final float[] values
            , @NonNull final Context context) {
        final Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(key);
        broadcastIntent.putExtra(key, values);
        LocalBroadcastManager.getInstance(context).sendBroadcast(broadcastIntent);
    }
}
