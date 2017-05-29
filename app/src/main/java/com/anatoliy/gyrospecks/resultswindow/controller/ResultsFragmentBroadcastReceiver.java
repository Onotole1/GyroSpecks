package com.anatoliy.gyrospecks.resultswindow.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.anatoliy.gyrospecks.base.controller.BaseBroadcastReceiver;

import java.util.ArrayList;

/**
 * Date: 21.04.17
 * Time: 8:48
 *
 * @author anatoliy
 *
 * Объект данного класса получает широковещательные сообщения и оповещает о них своих подписчиков
 */
public class ResultsFragmentBroadcastReceiver extends BaseBroadcastReceiver {
    private final static String HISTORY_ACTIVITY_BROADCAST_RECEIVER
            = "com.spitchenko.appsgeyser.historywindow.controller.ResultsFragmentBroadcastReceiver";
    private final static String READ_ACTION = HISTORY_ACTIVITY_BROADCAST_RECEIVER + ".readAction";
    private final static String NO_INTERNET_EXCEPTION = HISTORY_ACTIVITY_BROADCAST_RECEIVER
            + ".noInternetException";

    @Override
    public void onReceive(final Context context, final Intent intent) {
        final String action = intent.getAction();
        if (action.equals(READ_ACTION)) {
            notifyObserversUpdate(intent.getParcelableArrayListExtra(READ_ACTION));
        }
    }

    /**
     * Метод отправляет подписчикам данные, полученные из сервиса
     * @param parcelables - список элементов для отправки
     *                    (ArrayList<ResponseTrio implements Parcelable>)
     */
    private void notifyObserversUpdate(final ArrayList<Parcelable> parcelables) {
        for (int i = 0, size = observers.size(); i < size; i++) {
            final ResultsFragmentController observer = (ResultsFragmentController) observers.get(i);
            observer.updateOnUpdate(parcelables);
        }
    }

    public static String getReadActionKey() {
        return READ_ACTION;
    }

    public static String getNoInternetExceptionKey() {
        return NO_INTERNET_EXCEPTION;
    }

    /**
     * Интерфейс для отправки широковещательного сообщения
     * @param parcels - список элементов для отправки
     *                    (ArrayList<ResponseTrio implements Parcelable>)
     * @param action - действие
     * @param packageName - имя пакета
     * @param context - контекст
     */
    public static void sendToBroadcast(@NonNull final String action
            , @NonNull final String packageName, @NonNull final Context context
            , @Nullable final ArrayList<Parcelable> parcels) {
        final Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(action);
        broadcastIntent.setPackage(packageName);
        if (null != parcels) {
            broadcastIntent.putParcelableArrayListExtra(action, parcels);
        }
        LocalBroadcastManager.getInstance(context).sendBroadcast(broadcastIntent);
    }
}
