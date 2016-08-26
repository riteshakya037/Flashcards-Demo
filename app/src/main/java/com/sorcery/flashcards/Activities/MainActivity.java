package com.sorcery.flashcards.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jpardogo.android.googleprogressbar.library.GoogleProgressBar;
import com.sorcery.flashcards.Adaptors.FragmentStatePager;
import com.sorcery.flashcards.Adaptors.ZoomOutPageTransformer;
import com.sorcery.flashcards.CustomViews.MultiViewPager;
import com.sorcery.flashcards.CustomViews.WelcomeScreen.WelcomeDialog;
import com.sorcery.flashcards.Helper.DatabaseContract;
import com.sorcery.flashcards.Helper.DownloadMp3Async;
import com.sorcery.flashcards.Model.CardModel;
import com.sorcery.flashcards.Model.CurrentMode;
import com.sorcery.flashcards.R;

/**
 * The main activity of the application containing all the card holders.
 * <p>
 * Created by Ritesh Shakya on 8/22/2016.
 */
public class MainActivity extends AppCompatActivity implements FragmentStatePager.EmptyInterface, WelcomeDialog.ClickInterface, View.OnClickListener {
    /**
     * View used to display loading animation
     */
    private GoogleProgressBar googleProgressBar;
    /**
     * Local {@link DatabaseContract} which handles all database queries
     */
    private DatabaseContract.DbHelper dbHelper;
    /**
     * TAG for {@link MainActivity} class used in {@link android.util.Log}
     */
    private String TAG = "MainActivity";
    /**
     * Local {@link SharedPreferences} used to store various states.
     */
    SharedPreferences mPrefs;
    /**
     * Name used for app {@link SharedPreferences}
     */
    final String PREFS_NAME = "FlashPrefs";
    /**
     * Key used in {@link SharedPreferences} for storing active mode.
     */
    private static final String VAL_CURRENT_MODE = "current_mode";
    /**
     * Key used in {@link SharedPreferences} for storing first run of application.
     */
    private static final String VAL_FIRST_RUN = "first_run";

    /**
     * Enum {@link CurrentMode} which references the current mode of application.
     */
    public static CurrentMode current_mode;
    /**
     * Main adapter for the {@code mViewPager}
     */
    FragmentStatePager adapter;
    /**
     * Button used to change the mode of application.
     */
    private ImageButton switchMode;
    /**
     * TextView used to display the current mode to the user.
     */
    private TextView actionBarModeDisplay;
    /**
     * TextView used to display no. of cards. Also acts as a button for {@link FragmentStatePager#randomize()} .
     */
    private TextView countDisplay;
    /**
     * Stores the reference to Firebase database.
     */
    DatabaseReference databaseRef;
    /**
     * Holds all the cards in a page layout.
     */
    MultiViewPager mViewPager;

    /**
     * Default Constructor
     */
    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        // Instantiate necessary Views.
        actionBarModeDisplay = (TextView) findViewById(R.id.action_bar_mode);
        googleProgressBar = (GoogleProgressBar) findViewById(R.id.google_progress);
        switchMode = (ImageButton) findViewById(R.id.changeMode);
        switchMode.setOnClickListener(this);
        countDisplay = (TextView) findViewById(R.id.countDisplay);
        countDisplay.setOnClickListener(this);
        mViewPager = (MultiViewPager) findViewById(R.id.pager);

        // Instantiate SharedPreference to get stored state.
        mPrefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        if (mPrefs.getBoolean(VAL_FIRST_RUN, true)) { // Checks if app run for first time.
            WelcomeDialog dialog = new WelcomeDialog(this);
            dialog.show();
        } else {
            current_mode = CurrentMode.getMode(mPrefs.getString(VAL_CURRENT_MODE, CurrentMode.ENGLISH.getMode())); // Sets current mode of application
            actionBarModeDisplay.setText(getString(R.string.appbar_demo, current_mode.getDisplayText(), "Set 1")); // Displays the current mode and set to user.
        }

        // Get the Firebase app and all primitives we'll use
        FirebaseApp app = FirebaseApp.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance(app);

        // Instantiate local Database
        dbHelper = new DatabaseContract.DbHelper(this);

        // Instantiate Adaptor for ViewPager. Data added to Pager through Observer Pattern.
        adapter = new FragmentStatePager(getSupportFragmentManager(), this);
        mViewPager.setAdapter(adapter);
        mViewPager.setPageTransformer(true, new ZoomOutPageTransformer()); // Custom animation for page swipes

        // Get a reference to cards in the database
        databaseRef = database.getReference("cards/set1");

        // Add a listener that observers the change in real-time data-set to give callbacks.
        databaseRef.addChildEventListener(new ChildEventListener() {
            public void onChildAdded(DataSnapshot snapshot, String s) {
                // Get the card from the snapshot and add it to the UI
                CardModel card = snapshot.getValue(CardModel.class);
                adapter.addCard(card);
                if (!dbHelper.checkExist(card.voiceMale)) { // Check if voice file is locally available.
                    // If not download in cache and store reference in local database.
                    DownloadMp3Async mp3Async = new DownloadMp3Async(MainActivity.this);
                    mp3Async.execute(card.voiceMale);
                }
            }

            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            public void onChildRemoved(DataSnapshot dataSnapshot) {
                // Get the card from the snapshot and remove it to the UI
                CardModel card = dataSnapshot.getValue(CardModel.class);
                adapter.removeCard(card);
                if (dbHelper.checkExist(card.voiceMale)) {
                    dbHelper.onDelete(card.voiceMale);
                }
            }

            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    /**
     * Method called whe activity is no longer visible.
     */
    @Override
    protected void onStop() {
        super.onStop();
        dbHelper.close(); // Close database connection when activity is stopped to prevent leakage.
    }

    /**
     * Callback method of the current state of the data to be displayed. Checks if dataset in null.
     *
     * @param isEmpty If {@code true} dataset is empty. {@code else} otherwise.
     * @see FragmentStatePager.EmptyInterface
     */
    @Override
    public void isEmpty(boolean isEmpty) {
        googleProgressBar.setVisibility(isEmpty ? View.VISIBLE : View.GONE); //If true make loading visible  else make loading invisible.
    }

    /**
     * Callback method of the current state of the data displayed.
     *
     * @param count Current count of the dataset.
     */
    @Override
    public void updateCount(int count) {
        countDisplay.setText(getString(R.string.cards_count_format, count));
    }

    /**
     * Callback method form {@link WelcomeDialog}.
     *
     * @param currentMode Returns the mode selected by user.
     * @see WelcomeDialog.ClickInterface
     */
    @Override
    public void onSelect(CurrentMode currentMode) {
        mPrefs.edit().putBoolean(VAL_FIRST_RUN, false).putString(VAL_CURRENT_MODE, currentMode.getMode()).apply(); // Set firstRun flag as false in preference.
        current_mode = currentMode;
        actionBarModeDisplay.setText(getString(R.string.appbar_demo, currentMode.getDisplayText(), "Set 1")); // Display change to user.
        adapter.notifyChanged(); // Updates the dataset and also forces Fragments which have already been attached (even though not visible) to update view.
    }

    /**
     * Default callback method for {@link View#setOnClickListener(View.OnClickListener)} .
     *
     * @param view View that was clicked.
     */
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.changeMode) { // Alternates between the mode of application.
            if (current_mode == CurrentMode.ENGLISH) {
                onSelect(CurrentMode.GREEK);
            } else {
                onSelect(CurrentMode.ENGLISH);
            }
        } else if (view.getId() == R.id.countDisplay) { // Randomizes the card list to display.
            adapter.randomize();
            mViewPager.setCurrentItem(0, true);
        }
    }
}
