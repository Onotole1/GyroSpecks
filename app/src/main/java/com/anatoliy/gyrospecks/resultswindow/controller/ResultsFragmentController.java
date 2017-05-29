package com.anatoliy.gyrospecks.resultswindow.controller;

import android.app.Activity;
import android.app.Fragment;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.anatoliy.gyrospecks.R;
import com.anatoliy.gyrospecks.base.controller.BaseFragmentController;
import com.anatoliy.gyrospecks.model.DbResponse;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Date: 20.04.17
 * Time: 22:46
 *
 * @author anatoliy
 *
 * Объект данного класса содержит логику по взаимодействию с фрагментом экрана истории.
 */
public class ResultsFragmentController extends BaseFragmentController {
    private final static String HISTORY_ACTIVITY_CONTROLLER
            = "com.spitchenko.appsgeyser.historywindow.controller.ResultsFragmentController";
    private final static String LIST_STATE = HISTORY_ACTIVITY_CONTROLLER + ".listState";

    private final ResultsFragmentBroadcastReceiver historyFragmentBroadcastReceiver
            = new ResultsFragmentBroadcastReceiver();
    private LocalBroadcastManager localBroadcastManager;
    private ListView listView;
    private final Fragment fragment;
    private Parcelable listState;

    public ResultsFragmentController(final Fragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public void updateOnCreateView(final View view) {
        listView = (ListView) view.findViewById(R.id.fragment_history_list_view);
        /*drawerLayout = (DrawerLayout) fragment.getActivity()
                .findViewById(R.id.activity_base_drawer_layout);*/
    }

    //set title

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
        intentFilter.addAction(ResultsFragmentBroadcastReceiver.getNoInternetExceptionKey());
        localBroadcastManager.registerReceiver(historyFragmentBroadcastReceiver, intentFilter);

        historyFragmentBroadcastReceiver.addObserver(this);

        ResultsFragmentIntentService.start(ResultsFragmentIntentService.getReadHistoryKey()
                , activity);
    }

    @Override
    public void updateOnPause() {
        if (null != localBroadcastManager) {
            localBroadcastManager.unregisterReceiver(historyFragmentBroadcastReceiver);
        }
        historyFragmentBroadcastReceiver.removeObserver(this);

        //close drawer
    }

    /**
     * В данном методе происходит заполнение списка элементов listView данными,
     * пришедшими из historyFragmentBroadcastReceiver
     * @param parcelables - список элементов для заполнения списка
     *                    (ArrayList<ResponseTrio implements Parcelable>)
     */
    void updateOnUpdate(final ArrayList<Parcelable> parcelables) {
        final ArrayList<DbResponse> responseTrios = new ArrayList<>();
        for (int i = 0, size = parcelables.size(); i < size; i++) {
            final Parcelable parcel = parcelables.get(i);
            if (parcel instanceof DbResponse) {
                responseTrios.add((DbResponse) parcel);
            }
        }

        Collections.reverse(responseTrios);
        final ListViewAdapter listViewAdapter
                = new ListViewAdapter(fragment.getActivity(), responseTrios);
        listView.setAdapter(listViewAdapter);
        if (null != listState) {
            listView.onRestoreInstanceState(listState);
        }
    }
}
