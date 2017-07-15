package com.assistivetouch.widget;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public class ShortcutInfo {

    /**
     * The application name.
     */
    public CharSequence title;

    /**
     * The intent used to start the application.
     */
    public Intent intent;

    /**
     * The application icon.
     */
    Drawable icon;
    
    /**
     * Indicates the X position of the associated cell.
     */
    int cellX = -1;

    /**
     * Indicates the Y position of the associated cell.
     */
    int cellY = -1;

}
