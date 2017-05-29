package com.anatoliy.gyrospecks.base.view;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.anatoliy.gyrospecks.R;
import com.anatoliy.gyrospecks.base.controller.MainActivityController;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private final static int NUMBER_OBSERVERS = 1;
    private final ArrayList<MainActivityController> observers = new ArrayList<>(NUMBER_OBSERVERS);
    private final MainActivityController observer = new MainActivityController(this);

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addObserver(observer);
        notifyObserversOnCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();

        addObserver(observer);
        notifyObserversOnResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        notifyObserversOnPause();
        removeObserver(observer);
    }

    void addObserver(final MainActivityController game) {
        if (!observers.contains(game)) {
            observers.add(game);
        }
    }

    void removeObserver(final MainActivityController game) {
        if (observers.contains(game)) {
            observers.remove(game);
        }
    }

    private void notifyObserversOnCreate(final Bundle savedInstanceState) {
        for (final MainActivityController observer:observers) {
            observer.updateOnCreate(savedInstanceState);
        }
    }

    private void notifyObserversOnResume() {
        for (final MainActivityController observer:observers) {
            observer.updateOnResume();
        }
    }

    private void notifyObserversOnPause() {
        for (final MainActivityController observer:observers) {
            observer.updateOnPause();
        }
    }
}
