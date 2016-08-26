package com.sorcery.flashcards.Helper;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by Ritesh Shakya on 7/24/2016.
 */
public class Utils {

    private final Context _context;


    /**
     * Default Constructor
     *
     * @param context Activity context.
     */
    public Utils(Context context) {
        this._context = context;
    }

    /**
     * Gets screen width
     */
    public int getScreenWidth() {
        int columnWidth;
        WindowManager wm = (WindowManager) _context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        final Point point = new Point();
        try {
            display.getSize(point);
        } catch (NoSuchMethodError ignore) { // Older device
            point.x = display.getWidth();
            point.y = display.getHeight();
        }
        columnWidth = point.x;
        return columnWidth;
    }
}