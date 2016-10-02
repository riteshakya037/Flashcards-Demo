package com.sorcery.flashcards.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
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
import com.sorcery.flashcards.Helper.SpinnerInteractionListener;
import com.sorcery.flashcards.Model.CardModel;
import com.sorcery.flashcards.Model.CurrentMode;
import com.sorcery.flashcards.Model.SetModel;
import com.sorcery.flashcards.R;

import java.io.File;
import java.util.ArrayList;

/**
 * The main activity of the application containing all the card holders.
 * <p>
 * Created by Ritesh Shakya on 8/22/2016.
 */
public class MainActivity extends AppCompatActivity implements FragmentStatePager.EmptyInterface, WelcomeDialog.ClickInterface, View.OnClickListener, SpinnerInteractionListener.SpinnerListener {
    /**
     * Default set of cards to load on first run.
     */
    private static final String CONST_DEFAULT_SET = "cards/set1";
    /**
     * View used to display loading animation
     */
    private GoogleProgressBar googleProgressBar;
    /**
     * Reference to root level in database.
     */
    private FirebaseDatabase database;
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
    private SharedPreferences mPrefs;
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
     * Key used in {@link SharedPreferences} for storing active Set.
     */
    private static final String VAL_CURRENT_SET = "current_set";

    /**
     * Enum {@link CurrentMode} which references the current mode of application.
     */
    public static CurrentMode current_mode;
    /**
     * Main cardAdapter for the {@code mViewPager}
     */
    FragmentStatePager cardAdapter;
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
     * Stores the reference to Firebase database for cards.
     */
    private DatabaseReference databaseCardsRef;
    /**
     * Stores the reference to Firebase database for sets.
     */
    private DatabaseReference databaseSetsRef;
    /**
     * Holds all the cards in a page layout.
     */
    private MultiViewPager mViewPager;

    /**
     * Data for the spinnerSet display.
     */
    private ArrayAdapter<String> setAdapter;
    /**
     * Dataset for List of sets added dynamically.
     */
    private ArrayList<SetModel> sets = new ArrayList<>();
    /**
     *
     */
    private SetModel current_set;

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
            current_set = new SetModel("Set 1", "cards/set1");
        } else {
            current_mode = CurrentMode.getMode(mPrefs.getString(VAL_CURRENT_MODE, CurrentMode.ENGLISH.getMode())); // Sets current mode of application
            current_set = createSet(mPrefs.getString(VAL_CURRENT_SET, CONST_DEFAULT_SET));
            actionBarModeDisplay.setText(getString(R.string.appbar_demo, current_mode.getDisplayText(), current_set.getDisplayName())); // Displays the current mode and set to user.
        }

        // Instantiate local Database
        dbHelper = new DatabaseContract.DbHelper(this);

        // Get the Firebase app and all primitives we'll use
        FirebaseApp app = FirebaseApp.getInstance();
        database = FirebaseDatabase.getInstance(app);

        // Instantiate Adaptor for ViewPager. Data added to Pager through Observer Pattern.
        loadData(current_set.getSetLocation());

        // Loading spinner data from firebase database
        loadSpinnerData();

    }

    private SetModel createSet(String string) {
        return new SetModel("Set " + string.replaceAll("[^\\d]+", ""), string);
    }

    private void loadSpinnerData() {
        setAdapter = new ArrayAdapter<>(this, R.layout.spinner_item_selected, new ArrayList<String>());
        setAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(setAdapter);

        SpinnerInteractionListener listener = new SpinnerInteractionListener(this);
        spinner.setOnTouchListener(listener);
        spinner.setOnItemSelectedListener(listener);
        // Get a reference to cards in the database
        databaseSetsRef = database.getReference("sets");
        databaseSetsRef.addChildEventListener(new ChildEventListener() {
            public void onChildAdded(DataSnapshot snapshot, String s) {
                // Get the set from the snapshot and add it to the UI
                SetModel set = snapshot.getValue(SetModel.class);
                sets.add(set);
                if (set.getDisplayName().equals(current_set.getDisplayName())) {
                    spinner.setSelection(Integer.parseInt(current_set.getSetLocation().replaceAll("[^\\d]+", "")) - 1); // Force to change selection cause it will default to 0 otherwise
                }
                setAdapter.add(set.getDisplayName());
                setAdapter.notifyDataSetChanged();
            }

            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            public void onChildRemoved(DataSnapshot dataSnapshot) {
                // Get the set from the snapshot and remove it to the UI
                SetModel set = dataSnapshot.getValue(SetModel.class);
                sets.remove(set);
                setAdapter.remove(set.getDisplayName());
                setAdapter.notifyDataSetChanged();
            }

            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    /**
     * Loads new set of data by re-initialization and reference change;
     *
     * @param set Location of card set in reference to database.
     */
    private void loadData(String set) {
        cardAdapter = new FragmentStatePager(getSupportFragmentManager(), this);
        mViewPager.setAdapter(cardAdapter);
        mViewPager.setPageTransformer(true, new ZoomOutPageTransformer()); // Custom animation for page swipes
        isEmpty(true);
        // Get a reference to cards in the database
        databaseCardsRef = database.getReference(set);

        // Add a listener that observers the change in real-time data-set to give callbacks.
        databaseCardsRef.addChildEventListener(new ChildEventListener() {
            public void onChildAdded(DataSnapshot snapshot, String s) {
                // Get the card from the snapshot and add it to the UI
                CardModel card = snapshot.getValue(CardModel.class);
                cardAdapter.addCard(card);
                if (!dbHelper.checkExist(card.voiceMale) || !new File(dbHelper.onSelect(card.voiceMale)).exists()) { // Check if voice file is locally available.
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
                cardAdapter.removeCard(card);
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
        actionBarModeDisplay.setText(getString(R.string.appbar_demo, currentMode.getDisplayText(), current_set.getDisplayName())); // Display change to user.
        cardAdapter.notifyChanged(); // Updates the dataset and also forces Fragments which have already been attached (even though not visible) to update view.
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
            cardAdapter.randomize();
            mViewPager.setCurrentItem(0, true);
        }
    }

    /**
     * Default callback method for {@link android.widget.AdapterView.OnItemSelectedListener} .     *
     *
     * @param parent Parent View of the adapter.
     * @param view   View that has been selected.
     * @param pos    Position that has been selected.
     * @param id     Id of the view selected.
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        if (current_set != sets.get(pos)) { //Only change if selected set is different then current set.
            current_set = sets.get(pos);
            mPrefs.edit().putString(VAL_CURRENT_SET, current_set.getSetLocation()).apply(); // Set current set in preference.
            loadData(current_set.getSetLocation());
            actionBarModeDisplay.setText(getString(R.string.appbar_demo, current_mode.getDisplayText(), current_set.getDisplayName())); // Display change to user.
        }
    }

}
