package com.anatoliy.gyrospecks.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Date: 29.05.2017
 * Time: 20:56
 *
 * @author Anatoliy
 */

public class DbResponse implements Parcelable {
    private String name;
    private String date;
    private String spentTime;

    public DbResponse(final String name, final String date, final String spentTime) {
        this.name = name;
        this.date = date;
        this.spentTime = spentTime;
    }

    private DbResponse(final Parcel in) {
        name = in.readString();
        date = in.readString();
        spentTime = in.readString();
    }

    public static final Creator<DbResponse> CREATOR = new Creator<DbResponse>() {
        @Override
        public DbResponse createFromParcel(final Parcel in) {
            return new DbResponse(in);
        }

        @Override
        public DbResponse[] newArray(final int size) {
            return new DbResponse[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(final String date) {
        this.date = date;
    }

    public String getSpentTime() {
        return spentTime;
    }

    public void setSpentTime(final String spentTime) {
        this.spentTime = spentTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeString(name);
        dest.writeString(date);
        dest.writeString(spentTime);
    }
}
