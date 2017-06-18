package com.anatoliy.gyrospecks.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Date: 25.05.2017
 * Time: 22:08
 *
 * @author Anatoliy
 */

public class Position implements Serializable {
    private int x;
    private int y;

    public Position(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(final int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(final int y) {
        this.y = y;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Position position = (Position) o;

        return x == position.x && y == position.y;

    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }
}
