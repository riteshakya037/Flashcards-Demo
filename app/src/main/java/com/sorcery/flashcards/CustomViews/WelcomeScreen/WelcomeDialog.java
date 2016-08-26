package com.sorcery.flashcards.CustomViews.WelcomeScreen;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.TextView;

import com.sorcery.flashcards.Model.CurrentMode;
import com.sorcery.flashcards.R;

/**
 * Created by Ritesh Shakya on 8/24/2016.
 */

public class WelcomeDialog extends AlertDialog {
    private Context context;
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

    public interface ClickInterface {
        void onSelect(CurrentMode currentMode);
    }
}
