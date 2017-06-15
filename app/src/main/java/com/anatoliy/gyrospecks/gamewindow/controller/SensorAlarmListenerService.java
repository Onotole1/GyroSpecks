package com.anatoliy.gyrospecks.gamewindow.controller;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

/**
 * Date: 27.05.2017
 * Time: 18:12
 *
 * @author Anatoliy
 */

public class SensorAlarmListenerService extends IntentService {
    private final static String SENSOR_ALARM_LISTENER_SERVICE
            = "com.anatoliy.accelspecks.controller.SensorAlarmListenerService";
    private final static String ACTION_START = SENSOR_ALARM_LISTENER_SERVICE + ".start";
    private final static String ACTION_STOP = SENSOR_ALARM_LISTENER_SERVICE + ".stop";

    private final static int PERIOD = 1000;

    private int startSeconds;

    private AccelerometerListener accelerometerListener;

    private volatile boolean isStop;

    public SensorAlarmListenerService() {
        super(SENSOR_ALARM_LISTENER_SERVICE);
    }

    @Override
    protected void onHandleIntent(@Nullable final Intent intent) {
        if (null != intent) {
            final String action = intent.getAction();

            switch (action) {
                case ACTION_START:
                    startAlarm();
                    break;
                case ACTION_STOP:
                    stopAlarm();
                    break;
            }
        }

    }

    private void startAlarm() {

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while (!isStop) {
                    readSensor();

                    try {
                        Thread.sleep(PERIOD);
                    } catch (final InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                isStop = false;
            }
        };

        runnable.run();

    }

    private void stopAlarm() {
        isStop = true;
    }

    private void readSensor() {

        final SensorManager sensorManager
                = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        final List<Sensor> listSensor = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);

        final Sensor sensorAccelerometer = listSensor.get(0);

        if (null == accelerometerListener) {
            accelerometerListener = new AccelerometerListener(this, sensorManager);
        }

        sensorManager.registerListener(accelerometerListener, sensorAccelerometer
                , SensorManager.SENSOR_DELAY_FASTEST);
    }

    static void start(@NonNull final Context context, @NonNull final String action) {
        final Intent intent = new Intent(context, SensorAlarmListenerService.class);
        intent.setAction(action);
        context.startService(intent);
    }

    static String getActionStart() {
        return ACTION_START;
    }

    static String getActionStop() {
        return ACTION_STOP;
    }
}
