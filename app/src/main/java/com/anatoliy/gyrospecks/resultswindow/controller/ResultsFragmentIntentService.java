package com.anatoliy.gyrospecks.resultswindow.controller;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.anatoliy.gyrospecks.database.ResultsDataBaseHelper;
import com.anatoliy.gyrospecks.model.DbResponse;

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
    private final static String WRITE_TO_HISTORY = HISTORY_ACTIVITY_INTENT_SERVICE + ".writeToHistory";

    public ResultsFragmentIntentService() {
        super(HISTORY_ACTIVITY_INTENT_SERVICE);
    }

    @Override
    protected void onHandleIntent(@Nullable final Intent intent) {
        if (null != intent) {
            final String action = intent.getAction();

            switch (action) {
                case READ_HISTORY:
                    readHistory();
                    break;
                case WRITE_TO_HISTORY:
                    writeToHistory(intent);
                    break;
            }
        }
    }

    private void writeToHistory(@NonNull final Intent intent) {
        final Parcelable parcelableExtra = intent.getParcelableExtra(WRITE_TO_HISTORY);

        if (parcelableExtra instanceof DbResponse) {
            final ResultsDataBaseHelper resultsDataBaseHelper = new ResultsDataBaseHelper(this);

            final DbResponse dbResponse = (DbResponse) parcelableExtra;

            final String name = dbResponse.getName();
            final String date = dbResponse.getDate();
            final String spentTime = dbResponse.getSpentTime();

            resultsDataBaseHelper.writeResultToDb(name, date, spentTime);

            readHistory();
        }
    }

    /**
     * Чтение всех сообщений с базы данных и отправка широковещательного сообщения
     */
    private void readHistory() {
        final ResultsDataBaseHelper responseWordsDataBaseHelper
                = new ResultsDataBaseHelper(this);
        final ArrayList<Parcelable> allFromDb = responseWordsDataBaseHelper.readAllResultssDb();
        ResultsFragmentBroadcastReceiver.sendToBroadcastWithParcels(ResultsFragmentBroadcastReceiver
                .getReadActionKey(), this, allFromDb);
    }

    public static String getReadHistoryKey() {
        return READ_HISTORY;
    }

    public static String getWriteToHistoryKey() {
        return WRITE_TO_HISTORY;
    }

    /**
     * Интерфейс для запуска сервиса
     * @param action - действие
     * @param context - контекст
     */
    public static void start(@NonNull final String action, @NonNull final Context context, @Nullable final Parcelable content) {
        final Intent intent = new Intent(context, ResultsFragmentIntentService.class);
        intent.setAction(action);

        if (null != content) {
            intent.putExtra(action, content);
        }

        context.startService(intent);
    }
}
