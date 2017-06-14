package com.anatoliy.gyrospecks.gamewindow.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anatoliy.gyrospecks.R;
import com.anatoliy.gyrospecks.base.view.BaseFragment;
import com.anatoliy.gyrospecks.gamewindow.controller.GameFragmentController;

/**
 * Date: 20.04.17
 * Time: 15:32
 *
 * @author anatoliy
 *
 * Фрагмент главного экрана. Содержит действия над подписчиками.
 */
public final class GameFragment extends BaseFragment {


    private final static String MAIN_FRAGMENT
            = "com.anatoliy.gyrospecks.gamewindow.view.MainFragment";

    GameFragmentController controller = new GameFragmentController(this);



    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container
            , final Bundle savedInstanceState) {
        addObserver(controller);
        final View view = inflater.inflate(R.layout.game_fragment, container, false);
        notifyObserversOnCreateView(view);
        return view;
    }

    @Override
    public void onAttach(final Context context) {
        addObserver(controller);
        super.onAttach(context);
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        addObserver(controller);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        addObserver(controller);
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        removeObserver(controller);
    }

    public static String getGameFragment() {
        return MAIN_FRAGMENT;
    }


}