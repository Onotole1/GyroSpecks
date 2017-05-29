package com.anatoliy.gyrospecks.resultswindow.controller;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.anatoliy.gyrospecks.database.ResultsDataBaseHelper;

import java.util.ArrayList;

/**
 * Date: 21.04.17
 * Time: 8:39
 *
 * @author anatoliy
 *
 * Данный класс выполняет ресурсоемкие действия в фоне.
 * Система андроид задаёт особый статус таким фоновым задачам. Прописан в манифесте.
 */
public class ResultsFragmentIntentService extends IntentService {
    private final static String HISTORY_ACTIVITY_INTENT_SERVICE
            = "com.spitchenko.appsgeyser.historywindow.controller.ResultsFragmentIntentService";
    private final static String READ_HISTORY = HISTORY_ACTIVITY_INTENT_SERVICE + ".readHistory";

    public ResultsFragmentIntentService() {
        super(HISTORY_ACTIVITY_INTENT_SERVICE);
    }

    @Override
    protected void onHandleIntent(@Nullable final Intent intent) {
        if (null != intent) {
            if (intent.getAction().equals(READ_HISTORY)) {
                readHistory();
            }
        }
    }

    /**
     * Чтение всех сообщений с базы данных и отправка широковещательного сообщения
     */
    private void readHistory() {
        final ResultsDataBaseHelper responseWordsDataBaseHelper
                = new ResultsDataBaseHelper(this);
        final ArrayList<Parcelable> allFromDb = responseWordsDataBaseHelper.readAllResultssDb();
        ResultsFragmentBroadcastReceiver.sendToBroadcast(ResultsFragmentBroadcastReceiver
                .getReadActionKey(), getPackageName(), this, allFromDb);
    }

    public static String getReadHistoryKey() {
        return READ_HISTORY;
    }

    /**
     * Интерфейс для запуска сервиса
     * @param action - действие
     * @param context - контекст
     */
    public static void start(@NonNull final String action, @NonNull final Context context) {
        final Intent intent = new Intent(context, ResultsFragmentIntentService.class);
        intent.setAction(action);
        context.startService(intent);
    }
}
