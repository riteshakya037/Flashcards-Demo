package com.sorcery.flashcards.CustomViews.WelcomeScreen;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

import com.sorcery.flashcards.R;

/**
 * Created by Ritesh Shakya on 8/24/2016.
 */

public class WelcomeDialog extends AlertDialog {
    private Context context;
    private static final String ENG_MODE = "english_mode";
    private static final String GREEK_MODE = "greek_mode";
    ClickInterface anInterface;

    public WelcomeDialog(Context context) {
        super(context);
        this.context = context;
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
                anInterface.onSelect(GREEK_MODE);
                dismiss();
            }
        });

        final CardView englishMode = (CardView) findViewById(R.id.englishMode);
        englishMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                anInterface.onSelect(ENG_MODE);
                dismiss();
            }
        });

    }

    public interface ClickInterface {
        void onSelect(String currentMode);
    }
}
