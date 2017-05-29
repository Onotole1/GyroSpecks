package com.anatoliy.gyrospecks.base.controller;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.anatoliy.gyrospecks.R;
import com.anatoliy.gyrospecks.base.model.DrawerPair;
import com.anatoliy.gyrospecks.base.view.MainActivity;

/**
 * Date: 21.04.17
 * Time: 11:03
 *
 * @author anatoliy
 */
public class DrawerItemClickListener implements ListView.OnItemClickListener {
    @Override
    public void onItemClick(final AdapterView<?> parent, final View view, final int position
            , final long id) {
        final DrawerPair pair = (DrawerPair) ((ListView) parent).getAdapter().getItem(position);
        final String pairDescription = pair.getDescription();
        final Context context = view.getContext();
        if (pairDescription.equals(context
                .getString(R.string.drawer_layout_textView_description_game))) {
            ((MainActivity)view.getContext()).setGameFragment();
        } else if (pairDescription.equals(context
                .getString(R.string.drawer_layout_textView_description_results))) {
            ((MainActivity)view.getContext()).setResultsFragment();
        }

    }
}