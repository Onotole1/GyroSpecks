package com.anatoliy.gyrospecks.base.view;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

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

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.game_fragment_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        notifyObserversOnOptionsItemSelected(item);

        return true;
    }

    void notifyObserversOnOptionsItemSelected(final MenuItem item) {
        for (int i = 0, observersSize = observers.size(); i < observersSize; i++) {
            final MainActivityController observer = observers.get(i);
            observer.updateOnOptionsItemSelected(item);
        }
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
        for (int i = 0, observersSize = observers.size(); i < observersSize; i++) {
            final MainActivityController observer = observers.get(i);
            observer.updateOnCreate(savedInstanceState);
        }
    }

    private void notifyObserversOnResume() {
        for (int i = 0, observersSize = observers.size(); i < observersSize; i++) {
            final MainActivityController observer = observers.get(i);
            observer.updateOnResume();
        }
    }

    private void notifyObserversOnPause() {
        for (int i = 0, observersSize = observers.size(); i < observersSize; i++) {
            final MainActivityController observer = observers.get(i);
            observer.updateOnPause();
        }
    }

    public void setGameFragment() {
        notifyObserversOnSetGameFragment();
    }

    private void notifyObserversOnSetGameFragment() {
        for (int i = 0, observersSize = observers.size(); i < observersSize; i++) {
            final MainActivityController observer = observers.get(i);
            observer.updateOnSetGameFragment();
        }
    }

    public void setResultsFragment() {
        notifyObserversOnSetResultsFragment();
    }

    private void notifyObserversOnSetResultsFragment() {
        for (int i = 0, observersSize = observers.size(); i < observersSize; i++) {
            final MainActivityController observer = observers.get(i);
            observer.updateOnSetResultsFragment();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        notifyObserversOnSupportNavigateUp();
        return true;
    }

    private void notifyObserversOnSupportNavigateUp() {
        for (int i = 0, size = observers.size(); i < size; i++) {
            final MainActivityController observer = observers.get(i);
            observer.updateOnSupportNavigateUp();
        }
    }
}
