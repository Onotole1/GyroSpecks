package com.anatoliy.gyrospecks.base.controller;

import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.anatoliy.gyrospecks.R;
import com.anatoliy.gyrospecks.base.model.DrawerPair;
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

    private DrawerLayout drawerLayout;

    private ListView drawerListView;

    public MainActivityController(@NonNull final MainActivity activity) {
        this.activity = activity;
    }

    public void updateOnCreate(@Nullable final Bundle savedInstanceState) {

    }

    private void initDrawer() {
        final DrawerPair addElement = new DrawerPair(activity.getResources().getDrawable(R.drawable.ic_action_game)
                , activity.getString(R.string
                .drawer_layout_textView_description_game));
        final DrawerPair historyElement = new DrawerPair(activity.getResources().getDrawable(R.drawable.ic_action_results)
                , activity.getString(R.string
                .drawer_layout_textView_description_results));
        final DrawerPair[] drawerPairs = {addElement, historyElement};

        drawerLayout = (DrawerLayout) activity.findViewById(R.id.activity_main_drawer_layout);
        drawerListView = (ListView) activity
                .findViewById(R.id.activity_base_drawer_list_view);

        drawerListView.setAdapter(new DrawerListViewAdapter(activity, drawerPairs));
        drawerListView.setOnItemClickListener(new DrawerItemClickListener());
        final LayoutInflater inflater = activity.getLayoutInflater();
        final RelativeLayout header = (RelativeLayout)inflater.inflate(R.layout.drawer_header
                , drawerListView, false);
        drawerListView.addHeaderView(header, null, false);

        drawerListView.post(new Runnable() {
            @Override
            public void run() {
                if (isMainFragmentOnTheWindow()) {
                    selectMainFragment();
                } else if (isHistoryFragmentOnTheWindow()) {
                    selectHistoryFragment();
                }
            }
        });
    }

    public void updateOnResume() {

    }

    public void updateOnPause() {

    }

    public void closeDrawer() {
        drawerLayout.closeDrawers();
    }

    public void updateOnSetGameFragment() {

    }

    public void updateOnSetResultsFragment() {

    }

    public void updateOnSupportNavigateUp() {

    }
}
