package com.sorcery.flashcards.Activities;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Activity class only created to initialize Firebase instance and set persistent data.
 * <p>
 * Created by Ritesh Shakya on 8/22/2016.
 */

public class FlashCardsApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}