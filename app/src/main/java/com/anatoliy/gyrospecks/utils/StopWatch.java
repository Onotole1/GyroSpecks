package com.anatoliy.gyrospecks.utils;

import java.util.ArrayList;

/**
 * Date: 15.06.2017
 * Time: 0:23
 *
 * @author Anatoliy
 */

public class StopWatch {
    private volatile boolean isRun;

    private long startTime;

    private final ArrayList<StopWatchListener> listeners = new ArrayList<>();


    public void start() {
        startTime = System.currentTimeMillis();

        isRun = true;
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while (isRun) {
                    try {
                        Thread.sleep(1000);
                    } catch (final InterruptedException e) {
                        e.printStackTrace();
                    }
                    final long seconds = (System.currentTimeMillis() - startTime) / 1000;
                    notifyOnSecondsIterate(seconds);
                }
            }
        };

        runnable.run();
    }

    public void resume() {
        isRun = true;
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while (isRun) {
                    try {
                        Thread.sleep(1000);
                    } catch (final InterruptedException e) {
                        e.printStackTrace();
                    }
                    final long seconds = (System.currentTimeMillis() - startTime) / 1000;
                    notifyOnSecondsIterate(seconds);
                }
            }
        };

        runnable.run();
    }

    public void stop() {
        isRun = false;
    }

    public void pause() {
        isRun = false;
    }

    public void addListener(final StopWatchListener stopWatchListener) {
        if (!listeners.contains(stopWatchListener)) {
            listeners.add(stopWatchListener);
        }
    }

    public void removeListener(final StopWatchListener stopWatchListener) {
        if (listeners.contains(stopWatchListener)) {
            listeners.remove(stopWatchListener);
        }
    }

    private void notifyOnSecondsIterate(final long seconds) {
        for (final StopWatchListener listener:listeners) {
            listener.OnSecondsIterate(seconds);
        }
    }
}
