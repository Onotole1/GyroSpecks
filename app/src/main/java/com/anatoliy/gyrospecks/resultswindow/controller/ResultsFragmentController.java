package com.anatoliy.gyrospecks.resultswindow.controller;

import android.app.Activity;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.ListView;

import com.anatoliy.gyrospecks.R;
import com.anatoliy.gyrospecks.base.controller.BaseFragmentController;
import com.anatoliy.gyrospecks.base.view.MainActivity;
import com.anatoliy.gyrospecks.model.DbResponse;
import com.anatoliy.gyrospecks.resultswindow.view.ResultsFragment;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Date: 20.04.17
 * Time: 22:46
 *
 * @author anatoliy
 */
public class ResultsFragmentController extends BaseFragmentController {
    private final static String HISTORY_ACTIVITY_CONTROLLER
            = "com.spitchenko.appsgeyser.historywindow.controller.ResultsFragmentController";
    private final static String LIST_STATE = HISTORY_ACTIVITY_CONTROLLER + ".listState";

    private final ResultsFragmentBroadcastReceiver historyFragmentBroadcastReceiver
            = new ResultsFragmentBroadcastReceiver();
    private LocalBroadcastManager localBroadcastManager;
    private ListView listView;
    private final ResultsFragment fragment;
    private Parcelable listState;

    public ResultsFragmentController(final ResultsFragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public void updateOnCreateView(final View view) {
        listView = (ListView) view.findViewById(R.id.fragment_history_list_view);

        final Bundle arguments = fragment.getArguments();

        if (null != arguments) {
            final String string = arguments.getString(ResultsFragment.getShowResultDialog());
            if (null != string) {
                ResultDialogFragment.showResultDialog(string, (MainActivity) fragment.getActivity());
            }
        }
    }

    @Override
    public void updateOnSaveInstanceState(@NonNull final Bundle outState) {
        outState.putParcelable(LIST_STATE, listView.onSaveInstanceState());
    }

    @Override
    public void updateOnRestoreInstanceState(@Nullable final Bundle savedInstanceState) {
        if (null != savedInstanceState) {
            listState = savedInstanceState.getParcelable(LIST_STATE);
        }
    }

    @Override
    public void updateOnResume() {
        final Activity activity = fragment.getActivity();

        localBroadcastManager = LocalBroadcastManager.getInstance(activity);
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ResultsFragmentBroadcastReceiver.getReadActionKey());
        localBroadcastManager.registerReceiver(historyFragmentBroadcastReceiver, intentFilter);

        historyFragmentBroadcastReceiver.addObserver(this);

        ResultsFragmentIntentService.start(ResultsFragmentIntentService.getReadHistoryKey()
                , activity, null);
    }

    @Override
    public void updateOnPause() {
        if (null != localBroadcastManager) {
            localBroadcastManager.unregisterReceiver(historyFragmentBroadcastReceiver);
        }
        historyFragmentBroadcastReceiver.removeObserver(this);

        //close drawer
    }

    void updateOnUpdate(final ArrayList<Parcelable> parcelables) {
        final ArrayList<DbResponse> responseTrios = new ArrayList<>();
        for (int i = 0, size = parcelables.size(); i < size; i++) {
            final Parcelable parcel = parcelables.get(i);
            if (parcel instanceof DbResponse) {
                responseTrios.add((DbResponse) parcel);
            }
        }

        final ListViewAdapter listViewAdapter
                = new ListViewAdapter(fragment.getActivity(), responseTrios);
        listView.setAdapter(listViewAdapter);
        if (null != listState) {
            listView.onRestoreInstanceState(listState);
        }
    }
}
