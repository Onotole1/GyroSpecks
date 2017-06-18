package com.anatoliy.gyrospecks.resultswindow.controller;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.anatoliy.gyrospecks.R;
import com.anatoliy.gyrospecks.model.DbResponse;

import java.util.List;

/**
 * Date: 20.04.17
 * Time: 23:00
 *
 * @author anatoliy
 */
class ListViewAdapter extends ArrayAdapter<DbResponse> {

    ListViewAdapter(@NonNull final Context context
            , @NonNull final List<DbResponse> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable final View convertView
            , @NonNull final ViewGroup parent) {

        View workView = convertView;

        if (workView == null) {
            final LayoutInflater layoutInflater = ((AppCompatActivity) getContext())
                    .getLayoutInflater();
            workView = layoutInflater.inflate(R.layout.results_fragment_list_element, parent, false);
        }

        final DbResponse dbResponse = getItem(position);

       if (dbResponse != null) {
           final TextView nameTextView
                   = (TextView) workView.findViewById(R.id.activity_history_list_element_name);

           if (null != nameTextView) {
               nameTextView.setText(dbResponse.getName());
           }

            final TextView dateTextView
                    = (TextView) workView.findViewById(R.id.activity_history_list_element_date);

            if (null != dateTextView) {
                dateTextView.setText(dbResponse.getDate());
            }

            final TextView spentTimeTextView = (TextView) workView
                    .findViewById(R.id.activity_history_list_element_result);

            if (null != spentTimeTextView) {
                spentTimeTextView.setText(dbResponse.getSpentTime());
            }
        }
        return workView;
    }
}
