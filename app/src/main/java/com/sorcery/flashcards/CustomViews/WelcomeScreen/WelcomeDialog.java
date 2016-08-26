package com.sorcery.flashcards.CustomViews.WelcomeScreen;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

import com.sorcery.flashcards.Model.CurrentMode;
import com.sorcery.flashcards.R;

/**
 * Welcome The user with a option to select the mode of usage.
 * <p>
 * Created by Ritesh Shakya on 8/24/2016.
 */

public class WelcomeDialog extends AlertDialog {
    /**
     * Callback listener which observers user selection.
     */
    ClickInterface anInterface;

    public WelcomeDialog(Context context) {
        super(context);
        this.anInterface = (ClickInterface) context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_dialog);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        CardView greekMode = (CardView) findViewById(R.id.greekMode);
        greekMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                anInterface.onSelect(CurrentMode.GREEK);
                dismiss();
            }
        });

        final CardView englishMode = (CardView) findViewById(R.id.englishMode);
        englishMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                anInterface.onSelect(CurrentMode.ENGLISH);
                dismiss();
            }
        });

    }

    /**
     * Callback for when user clicks on either of the two modes.
     */
    public interface ClickInterface {
        void onSelect(CurrentMode currentMode);
    }
}
