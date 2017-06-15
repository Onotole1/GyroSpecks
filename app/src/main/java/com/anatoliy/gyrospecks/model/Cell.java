package com.anatoliy.gyrospecks.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Date: 25.05.2017
 * Time: 21:50
 *
 * @author Anatoliy
 */

public class Cell implements Parcelable {
    private int number;
    private Position position;

    public Cell(final int number, final Position position) {
        this.number = number;
        this.position = position;
    }

    private Cell(final Parcel in) {
        number = in.readInt();
    }

    public static final Creator<Cell> CREATOR = new Creator<Cell>() {
        @Override
        public Cell createFromParcel(final Parcel in) {
            return new Cell(in);
        }

        @Override
        public Cell[] newArray(final int size) {
            return new Cell[size];
        }
    };

    public int getNumber() {
        return number;
    }

    public void setNumber(final int number) {
        this.number = number;
    }

    public void setPosition(final Position position) {
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Cell cell = (Cell) o;

        return number == cell.number && position.equals(cell.position);

    }

    @Override
    public int hashCode() {
        int result = number;
        result = 31 * result + position.hashCode();
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeInt(number);
    }
}
