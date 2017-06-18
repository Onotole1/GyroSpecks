package com.anatoliy.gyrospecks.gamewindow.controller;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.anatoliy.gyrospecks.model.Cell;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashSet;

/**
 * Date: 18.06.2017
 * Time: 23:59
 *
 * @author Anatoliy
 */

public class StateService extends IntentService {
    private final static String STATE_SERVICE
            = "com.anatoliy.gyrospecks.gamewindow.controller.StateService";
    private final static String RESTORE_ACTION = STATE_SERVICE + ".restoreAction";
    private final static String SAVE_ACTION = STATE_SERVICE + ".Action";
    private final static String STATE_FILE = "state";

    public StateService() {
        super(STATE_SERVICE);
    }

    @Override
    protected void onHandleIntent(@Nullable final Intent intent) {
        if (null != intent) {
            final String action = intent.getAction();

            switch (action) {
                case SAVE_ACTION:
                    saveState(intent);
                    break;
                case RESTORE_ACTION:
                    restoreState();
                    break;
            }
        }
    }

    public static void startSaveState(@NonNull final Context context, @NonNull final HashSet<Cell> board) {
        final Intent intent = new Intent(context, StateService.class);
        intent.setAction(SAVE_ACTION);
        intent.putExtra(SAVE_ACTION, board);
        context.startService(intent);
    }

    public static void startRestoreState(@NonNull final Context context) {
        final Intent intent = new Intent(context, StateService.class);
        intent.setAction(RESTORE_ACTION);
        context.startService(intent);
    }

    private void restoreState() {
        try {
            final ObjectInputStream in = new ObjectInputStream(openFileInput(STATE_FILE));
            final Object readObject = in.readObject();

            if (null != readObject) {
                final Serializable extra = (Serializable) readObject;
                in.close();

                GameWindowBroadcastReceiver.sendBroadcastRestore(GameWindowBroadcastReceiver
                        .getRestoreStateKey(), extra, this);
            }
        } catch (final IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void saveState(@NonNull final Intent intent) {
        try {
            final FileOutputStream fileOutputStream
                    = openFileOutput(STATE_FILE, Context.MODE_PRIVATE);
            final ObjectOutputStream out = new ObjectOutputStream(fileOutputStream);
            final Serializable extra = intent.getSerializableExtra(SAVE_ACTION);
            out.writeObject(extra);
            out.flush();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}
