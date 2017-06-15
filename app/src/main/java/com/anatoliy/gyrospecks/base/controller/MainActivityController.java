package com.anatoliy.gyrospecks.base.controller;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.anatoliy.gyrospecks.R;
import com.anatoliy.gyrospecks.base.model.DrawerPair;
import com.anatoliy.gyrospecks.base.view.MainActivity;
import com.anatoliy.gyrospecks.gamewindow.controller.GameFragmentController;
import com.anatoliy.gyrospecks.gamewindow.view.GameFragment;
import com.anatoliy.gyrospecks.resultswindow.view.ResultsFragment;

/**
 * Date: 25.05.2017
 * Time: 21:52
 *
 * @author Anatoliy
 */

@SuppressWarnings("deprecation")
public class MainActivityController {


    private final MainActivity activity;

    // 0 - position header
    private final static int GAME_FRAGMENT_POSITION_IN_DRAWER = 1;
    private final static int RESULTS_FRAGMENT_POSITION_IN_DRAWER = 2;

    private DrawerLayout drawerLayout;

    private ListView drawerListView;

    public MainActivityController(@NonNull final MainActivity activity) {
        this.activity = activity;
    }

    public void updateOnCreate(@Nullable final Bundle savedInstanceState) {
        initDrawerToggle();

        if (null == savedInstanceState) {
            final GameFragment gameFragment = new GameFragment();
            final FragmentManager fragmentManager = activity.getFragmentManager();
            final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.activity_main_container, gameFragment
                    , GameFragment.getGameFragment());
            fragmentTransaction.commit();
        }
    }

    private DrawerLayout initDrawer() {
        final DrawerPair addElement = new DrawerPair(activity.getResources().getDrawable(R.drawable.ic_action_game)
                , activity.getString(R.string
                .drawer_layout_textView_description_game));
        final DrawerPair historyElement = new DrawerPair(activity.getResources().getDrawable(R.drawable.ic_action_results)
                , activity.getString(R.string
                .drawer_layout_textView_description_results));
        final DrawerPair[] drawerPairs = {addElement, historyElement};

        drawerLayout = (DrawerLayout) activity.findViewById(R.id.activity_main_drawer_layout);
        drawerListView = (ListView) activity
                .findViewById(R.id.activity_main_drawer_list_view);

        drawerListView.setAdapter(new DrawerListViewAdapter(activity, drawerPairs));
        drawerListView.setOnItemClickListener(new DrawerItemClickListener(this));
        final LayoutInflater inflater = activity.getLayoutInflater();
        final ConstraintLayout header = (ConstraintLayout)inflater.inflate(R.layout.drawer_header
                , drawerListView, false);
        drawerListView.addHeaderView(header, null, false);

        drawerListView.post(new Runnable() {
            @Override
            public void run() {
                if (isGameFragmentOnTheWindow()) {
                    selectGameFragment();
                } else if (isResultsFragmentOnTheWindow()) {
                    selectHistoryFragment();
                }
            }
        });

        return drawerLayout;
    }

    private android.support.v7.widget.Toolbar initToolbar() {

        final android.support.v7.widget.Toolbar toolbar
                = (android.support.v7.widget.Toolbar) activity
                .findViewById(R.id.activity_main_toolbar);

        activity.setSupportActionBar(toolbar);

        return toolbar;
    }

    private void initDrawerToggle() {
        final Toolbar toolbar = initToolbar();
        final DrawerLayout drawerLayout = initDrawer();

        final ActionBarDrawerToggle actionBarDrawerToggle
                = new ActionBarDrawerToggle(activity, drawerLayout, toolbar, R.string.open_drawer
                , (R.string.close_drawer));

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    private boolean isGameFragmentOnTheWindow() {
        final FragmentManager manager = activity.getFragmentManager();
        final Fragment fragmentByTag = manager.findFragmentByTag(GameFragment.getGameFragment());
        return null != fragmentByTag;
    }

    private boolean isResultsFragmentOnTheWindow() {
        final FragmentManager manager = activity.getFragmentManager();
        final Fragment fragmentByTag = manager.findFragmentByTag(ResultsFragment.getResultFragment());
        return null != fragmentByTag;
    }

    public void updateOnResume() {

    }

    public void updateOnPause() {

    }

    void closeDrawer() {
        drawerLayout.closeDrawers();
    }

    public void updateOnSetGameFragment() {
        final FragmentManager manager = activity.getFragmentManager();
        final Fragment fragmentByTag = manager.findFragmentByTag(GameFragment.getGameFragment());
        if (null == fragmentByTag) {
            replaceGameFragment(manager);
        } else {
            if (fragmentByTag.isVisible()) {
                drawerLayout.closeDrawers();
            } else {
                manager.popBackStackImmediate();
                replaceGameFragment(manager);
            }
        }

        selectGameFragment();
    }

    private GameFragmentController getGameFragmentController() {
        final FragmentManager manager = activity.getFragmentManager();
        final GameFragment fragmentByTag = (GameFragment) manager.findFragmentByTag(GameFragment.getGameFragment());
        if (null != fragmentByTag) {
            return fragmentByTag.getController();
        } else {
            return null;
        }
    }

    private void replaceGameFragment(final FragmentManager fragmentManager) {
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.activity_main_container, new GameFragment()
                , GameFragment.getGameFragment());
        fragmentTransaction.commit();
    }

    public void updateOnSetResultsFragment() {
        final FragmentManager manager = activity.getFragmentManager();
        final Fragment fragmentByTag = manager
                .findFragmentByTag(ResultsFragment.getResultFragment());
        if (null == fragmentByTag) {
            replaceHistoryFragment(manager);
        } else {
            if (fragmentByTag.isVisible()) {
                drawerLayout.closeDrawers();
            } else {
                manager.popBackStackImmediate();
                replaceHistoryFragment(manager);
            }
        }

        selectHistoryFragment();
    }

    private void replaceHistoryFragment(final FragmentManager fragmentManager) {
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.activity_main_container, new ResultsFragment()
                , ResultsFragment.getResultFragment());
        fragmentTransaction.addToBackStack(ResultsFragment.getResultFragment());
        fragmentTransaction.commit();
    }

    public void updateOnSupportNavigateUp() {
        final FragmentManager manager = activity.getFragmentManager();
        manager.popBackStackImmediate();

        if (isGameFragmentOnTheWindow()) {
            selectGameFragment();
        } else if (isResultsFragmentOnTheWindow()) {
            selectHistoryFragment();
        }
    }

    private void selectGameFragment() {
        final View resultFragmentItem = drawerListView.getChildAt(GAME_FRAGMENT_POSITION_IN_DRAWER);
        resultFragmentItem.setBackgroundColor(activity.getResources().getColor(R.color.selected));

        final TextView mainFragmentText
                = (TextView) resultFragmentItem.findViewById(R.id.drawer_element_textView_description);
        mainFragmentText.setTextColor(activity.getResources().getColor(R.color.colorPrimary));

        final ImageView mainFragmentIcon
                = (ImageView) resultFragmentItem.findViewById(R.id.drawer_element_imageView_icon);

        final Drawable mainFragmentIconDrawable = mainFragmentIcon.getDrawable();
        mainFragmentIconDrawable.setColorFilter(new PorterDuffColorFilter(activity.getResources()
                .getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP));
        mainFragmentIcon.setImageDrawable(mainFragmentIconDrawable);

        final View historyFragmentItem = drawerListView.getChildAt(RESULTS_FRAGMENT_POSITION_IN_DRAWER);
        historyFragmentItem.setBackgroundColor(Color.TRANSPARENT);

        final TextView historyFragmentText
                = (TextView) historyFragmentItem.findViewById(R.id.drawer_element_textView_description);
        historyFragmentText.setTextColor(Color.GRAY);

        final ImageView historyFragmentIcon
                = (ImageView) historyFragmentItem.findViewById(R.id.drawer_element_imageView_icon);

        final Drawable resultsFragmentIconDrawable = historyFragmentIcon.getDrawable();
        resultsFragmentIconDrawable.setColorFilter(new PorterDuffColorFilter(Color.GRAY
                , PorterDuff.Mode.SRC_ATOP));
        historyFragmentIcon.setImageDrawable(resultsFragmentIconDrawable);
    }

    private void selectHistoryFragment() {
        final View mainFragmentItem = drawerListView.getChildAt(GAME_FRAGMENT_POSITION_IN_DRAWER);
        mainFragmentItem.setBackgroundColor(Color.TRANSPARENT);

        final TextView mainFragmentText
                = (TextView) mainFragmentItem.findViewById(R.id.drawer_element_textView_description);
        mainFragmentText.setTextColor(Color.GRAY);

        final ImageView mainFragmentIcon
                = (ImageView) mainFragmentItem.findViewById(R.id.drawer_element_imageView_icon);

        final Drawable mainFragmentIconDrawable = mainFragmentIcon.getDrawable();
        mainFragmentIconDrawable.setColorFilter(new PorterDuffColorFilter(Color.GRAY
                , PorterDuff.Mode.SRC_ATOP));
        mainFragmentIcon.setImageDrawable(mainFragmentIconDrawable);

        final View historyFragmentItem = drawerListView.getChildAt(RESULTS_FRAGMENT_POSITION_IN_DRAWER);
        historyFragmentItem.setBackgroundColor(activity.getResources().getColor(R.color.selected));

        final TextView historyFragmentText
                = (TextView) historyFragmentItem.findViewById(R.id.drawer_element_textView_description);
        historyFragmentText.setTextColor(activity.getResources().getColor(R.color.colorPrimary));

        final ImageView historyFragmentIcon
                = (ImageView) historyFragmentItem.findViewById(R.id.drawer_element_imageView_icon);

        final Drawable historyFragmentIconDrawable = historyFragmentIcon.getDrawable();
        historyFragmentIconDrawable.setColorFilter(new PorterDuffColorFilter(activity.getResources()
                .getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP));
        historyFragmentIcon.setImageDrawable(historyFragmentIconDrawable);
    }

    public void updateOnOptionsItemSelected(final MenuItem item) {
        if (isGameFragmentOnTheWindow()) {
            final GameFragmentController gameFragmentController = getGameFragmentController();
            if (null != gameFragmentController) {
                if (gameFragmentController.isPaused()) {
                    item.setIcon(activity.getResources().getDrawable(R.drawable.ic_action_pause));
                    gameFragmentController.resumeGame();
                } else {
                    item.setIcon(activity.getResources().getDrawable(R.drawable.ic_action_resume));
                    gameFragmentController.pauseGame();
                }
            }
        }
    }
}
