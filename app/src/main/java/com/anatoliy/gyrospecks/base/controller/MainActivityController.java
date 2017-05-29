package com.anatoliy.gyrospecks.base.controller;

import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.anatoliy.gyrospecks.gamewindow.controller.SensorAlarmListenerService;
import com.anatoliy.gyrospecks.gamewindow.controller.SensorValuesBroadcastReceiver;
import com.anatoliy.gyrospecks.base.view.MainActivity;

/**
 * Date: 25.05.2017
 * Time: 21:52
 *
 * @author Anatoliy
 */

@SuppressWarnings("deprecation")
public class MainActivityController {


    private final MainActivity activity;

    private final SensorValuesBroadcastReceiver sensorValuesBroadcastReceiver
            = new SensorValuesBroadcastReceiver();

    public MainActivityController(@NonNull final MainActivity activity) {
        this.activity = activity;
    }

    public void updateOnCreate(@Nullable final Bundle savedInstanceState) {

    }

    public void updateOnResume() {

    }

    public void updateOnPause() {

    }
}
