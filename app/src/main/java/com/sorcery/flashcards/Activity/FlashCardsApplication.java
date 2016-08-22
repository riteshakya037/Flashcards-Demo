package com.sorcery.flashcards.Activity;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Ritesh Shakya on 8/22/2016.
 */

public class FlashCardsApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}