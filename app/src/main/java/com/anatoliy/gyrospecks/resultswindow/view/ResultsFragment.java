package com.anatoliy.gyrospecks.resultswindow.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anatoliy.gyrospecks.R;
import com.anatoliy.gyrospecks.base.view.BaseFragment;
import com.anatoliy.gyrospecks.resultswindow.controller.ResultsFragmentController;

/**
 * Date: 20.04.17
 * Time: 22:46
 *
 * @author anatoliy
 *
 * Фрагмент экрана истории. Содержит действия над подписчиками.
 */

public class ResultsFragment extends BaseFragment {
    private final static String HISTORY_FRAGMENT
            =  "com.spitchenko.appsgeyser.historywindow.userinterface.ResultsFragment";
    private final ResultsFragmentController historyFragmentController
            = new ResultsFragmentController(this);

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container
            , final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.results_fragment, container, false);
        notifyObserversOnCreateView(view);
        return view;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        addObserver(historyFragmentController);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        addObserver(historyFragmentController);
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        removeObserver(historyFragmentController);
    }

    public static String getHistoryFragment() {
        return HISTORY_FRAGMENT;
    }
}