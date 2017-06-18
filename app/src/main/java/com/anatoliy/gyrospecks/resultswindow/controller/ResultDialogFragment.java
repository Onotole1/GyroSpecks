package com.anatoliy.gyrospecks.resultswindow.controller;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.anatoliy.gyrospecks.R;
import com.anatoliy.gyrospecks.base.view.MainActivity;
import com.anatoliy.gyrospecks.model.DbResponse;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Date: 18.06.2017
 * Time: 17:44
 *
 * @author Anatoliy
 */

public class ResultDialogFragment extends DialogFragment {
    private final static String RESULT_DIALOG_KEY
            = "com.anatoliy.gyrospecks.resultswindow.controller.ResultDialogFragment";
    private final static int BUNDLE_CAPACITY = 1;
    private final static String messageResult = "Ваш результат: %s";

    private String result;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Bundle arguments = getArguments();
        result = arguments.getString(RESULT_DIALOG_KEY);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container
            , final Bundle savedInstanceState) {
        getDialog().setCancelable(false);

        final View root = inflater.inflate(R.layout.result_dialog_fragment, container, false);
        final TextView message = (TextView) root.findViewById(R.id.result_dialog_fragment_message);

        message.setText(String.format(messageResult, result));

        final EditText editText = (EditText) root.findViewById(R.id.result_dialog_fragment_edittext);
        final Button buttonOk = (Button) root.findViewById(R.id.result_dialog_fragment_button_ok);

        final DialogFragment dialogFragment = this;
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final String name = editText.getText().toString();
                final Context context = v.getContext();

                if (!name.isEmpty()) {
                    final SimpleDateFormat simpleDateFormat
                            = new SimpleDateFormat("HH:mm:ss MM.dd.yyyy", Locale.getDefault());
                    final Date date = new Date();

                    final DbResponse dbResponse
                            = new DbResponse(name, simpleDateFormat.format(date)
                            , result);
                    ResultsFragmentIntentService.start(ResultsFragmentIntentService
                            .getWriteToHistoryKey(), context, dbResponse);
                    dialogFragment.dismiss();

                } else {
                    Toast.makeText(context, context.getString(R.string
                            .result_dialog_length_exception), Toast.LENGTH_SHORT).show();
                }
            }
        });

        return root;
    }

    public static void showResultDialog(@NonNull final String result, @NonNull final MainActivity activity) {
        final Bundle bundle = new Bundle(BUNDLE_CAPACITY);
        bundle.putString(RESULT_DIALOG_KEY, result);

        final ResultDialogFragment resultDialogFragment = new ResultDialogFragment();
        resultDialogFragment.setArguments(bundle);

        final FragmentManager fragmentManager = activity.getFragmentManager();
        resultDialogFragment.show(fragmentManager, RESULT_DIALOG_KEY);
    }
}