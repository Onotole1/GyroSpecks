package com.anatoliy.gyrospecks.database;

import android.provider.BaseColumns;

/**
 * Date: 20.04.17
 * Time: 16:20
 *
 * @author anatoliy
 *
 * Класс содержит название таблицы и колонок для работы с базой данных
 */
abstract class ResultsDataBase implements BaseColumns {
        static final String TABLE_NAME = "results";
        static final String DATE = "date";
        static final String SPENT_TIME = "spent_time";
}
