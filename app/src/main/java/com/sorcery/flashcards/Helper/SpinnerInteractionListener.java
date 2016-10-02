package com.sorcery.flashcards.Helper;

/**
 * Created by Ritesh Shakya on 8/28/2016.
 */

import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;

public class SpinnerInteractionListener implements AdapterView.OnItemSelectedListener, View.OnTouchListener {

    boolean userSelect = false;
    SpinnerListener spinnerListener;

    public SpinnerInteractionListener(SpinnerListener spinnerListener) {
        this.spinnerListener = spinnerListener;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        userSelect = true;
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        if (userSelect) {
            spinnerListener.onItemSelected(parent, view, pos, id);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    public interface SpinnerListener {
        void onItemSelected(AdapterView<?> parent, View view, int pos, long id);
    }
}