package com.anatoliy.gyrospecks.gamewindow.controller;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.anatoliy.gyrospecks.gamewindow.controller.SensorValuesBroadcastReceiver;

/**
 * Date: 17.05.17
 * Time: 11:26
 *
 * @author anatoliy
 */

class AccelerometerListener implements SensorEventListener {

    private final Context context;

    private final SensorManager sensorManager;

    AccelerometerListener(final Context context, final SensorManager sensorManager) {
        this.context = context;
        this.sensorManager = sensorManager;
    }

    @Override
    public void onSensorChanged(final SensorEvent event) {

        final float[] values = event.values;

        sensorManager.unregisterListener(this);

        SensorValuesBroadcastReceiver.sendBroadcast(SensorValuesBroadcastReceiver
                .getSensorValuesKey(), values, context);
    }

    @Override
    public void onAccuracyChanged(final Sensor sensor, final int accuracy) {
    }
}
