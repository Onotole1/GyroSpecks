package com.anatoliy.gyrospecks.base.view;

import android.app.Fragment;
import android.os.Bundle;
import android.view.View;

import com.anatoliy.gyrospecks.base.controller.BaseFragmentController;

import java.util.ArrayList;

/**
 * Date: 29.04.17
 * Time: 13:36
 *
 * @author anatoliy
 */
public abstract class BaseFragment extends Fragment {
    private final static int NUMBER_OBSERVERS = 1;
    private final ArrayList<BaseFragmentController> observers = new ArrayList<>(NUMBER_OBSERVERS);

    @Override
    public void onResume() {
        super.onResume();
        notifyObserversOnResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        notifyObserversOnPause();
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        notifyObserversOnSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(final Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        notifyObserversOnViewStateRestored(savedInstanceState);
    }

    protected void addObserver(final BaseFragmentController observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    protected void removeObserver(final BaseFragmentController observer) {
        if (observers.contains(observer)) {
            observers.remove(observer);
        }
    }

    protected void notifyObserversOnCreateView(final View view) {
        for (int i = 0, size = observers.size(); i < size; i++) {
            final BaseFragmentController observer = observers.get(i);
            observer.updateOnCreateView(view);
        }
    }

    private void notifyObserversOnResume() {
        for (int i = 0, size = observers.size(); i < size; i++) {
            final BaseFragmentController observer = observers.get(i);
            observer.updateOnResume();
        }
    }

    private void notifyObserversOnPause() {
        for (int i = 0, size = observers.size(); i < size; i++) {
            final BaseFragmentController observer = observers.get(i);
            observer.updateOnPause();
        }
    }

    private void notifyObserversOnSaveInstanceState(final Bundle outState) {
        for (int i = 0, size = observers.size(); i < size; i++) {
            final BaseFragmentController observer = observers.get(i);
            observer.updateOnSaveInstanceState(outState);
        }
    }

    private void notifyObserversOnViewStateRestored(final Bundle savedInstanceState) {
        for (int i = 0, size = observers.size(); i < size; i++) {
            final BaseFragmentController observer = observers.get(i);
            observer.updateOnRestoreInstanceState(savedInstanceState);
        }
    }
}