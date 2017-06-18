package com.anatoliy.gyrospecks.gamewindow.controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;

import com.anatoliy.gyrospecks.base.controller.MainActivityController;
import com.anatoliy.gyrospecks.model.Cell;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Date: 27.05.2017
 * Time: 18:21
 *
 * @author Anatoliy
 */

public class GameWindowBroadcastReceiver extends BroadcastReceiver {
    private final static String SENSOR_VALUES_BROADCAST_RECEIVER
            = "com.anatoliy.accelspecks.controller.GameWindowBroadcastReceiver";

    private final static String SENSOR_VALUES_KEY = SENSOR_VALUES_BROADCAST_RECEIVER + ".valuesKey";

    private final static String RESTORE_STATE_KEY = SENSOR_VALUES_BROADCAST_RECEIVER + ".restoreKey";
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
        } else if (intent.getAction().equals(RESTORE_STATE_KEY)) {
            notifyOnRestoreState(intent);
        }
    }

    public static String getRestoreStateKey() {
        return RESTORE_STATE_KEY;
    }

    private void notifyOnRestoreState(final Intent intent) {
        final Serializable serializableExtra = intent.getSerializableExtra(RESTORE_STATE_KEY);
        final HashSet<Cell> board = new HashSet<>();
        if (serializableExtra instanceof HashSet) {
            for (final Object object: (HashSet)serializableExtra) {
                if (object instanceof Cell) {
                    board.add((Cell) object);
                }
            }
            for (final GameFragmentController observer : observers) {
                observer.updateOnRestoreState(board);
            }
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

    static void sendBroadcastRestore(@NonNull final String key, @NonNull final Serializable serilizable
            , @NonNull final Context context) {
        final Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(key);
        broadcastIntent.putExtra(key, serilizable);
        LocalBroadcastManager.getInstance(context).sendBroadcast(broadcastIntent);
    }

}
