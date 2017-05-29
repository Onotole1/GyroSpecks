package com.anatoliy.gyrospecks.base.model;

import android.graphics.drawable.Drawable;

/**
 * Date: 30.05.2017
 * Time: 0:55
 *
 * @author Anatoliy
 */

public class DrawerPair {
    private final Drawable drawable;
    private final String description;

    public DrawerPair(final Drawable drawable, final String description) {
        this.drawable = drawable;
        this.description = description;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public String getDescription() {
        return description;
    }
}
