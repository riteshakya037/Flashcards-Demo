package com.sorcery.flashcards.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TypefaceSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jpardogo.android.googleprogressbar.library.GoogleProgressBar;
import com.sorcery.flashcards.Adaptors.FragmentStatePagerAdapter;
import com.sorcery.flashcards.Adaptors.ZoomOutPageTransformer;
import com.sorcery.flashcards.CustomViews.MultiViewPager;
import com.sorcery.flashcards.Model.CardModel;
import com.sorcery.flashcards.R;

public class MainActivity extends AppCompatActivity implements FragmentStatePagerAdapter.EmptyInterface {
    private FirebaseApp app;
    private FirebaseDatabase database;
    GoogleProgressBar googleProgressBar;
    private DatabaseReference databaseRef;

    /**
     * The {@link MultiViewPager} that will host the section contents.
     */
    private MultiViewPager mViewPager;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        SpannableString title = new SpannableString(getResources().getString(R.string.app_name));

        // Add a span for the sans-serif-light font
        title.setSpan(
                new TypefaceSpan("sans-serif-light"),
                0,
                title.length(),
                Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        toolbar.setTitle(title);
        setSupportActionBar(toolbar);


        // Set up the ViewPager with the sections adapter.
        mViewPager = (MultiViewPager) findViewById(R.id.pager);


        // Get the Firebase app and all primitives we'll use
        app = FirebaseApp.getInstance();
        database = FirebaseDatabase.getInstance(app);

        final FragmentStatePagerAdapter adapter = new FragmentStatePagerAdapter(getSupportFragmentManager(), this);

        mViewPager.setAdapter(adapter);
        mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        // Get a reference to cards in the database
        databaseRef = database.getReference("cards");
        databaseRef.addChildEventListener(new ChildEventListener() {
            public void onChildAdded(DataSnapshot snapshot, String s) {
                // Get the card from the snapshot and add it to the UI
                CardModel card = snapshot.getValue(CardModel.class);
                adapter.addCard(card);
            }

            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            public void onChildRemoved(DataSnapshot dataSnapshot) {
                // Get the card from the snapshot and remove it to the UI
                CardModel card = dataSnapshot.getValue(CardModel.class);
                adapter.removeCard(card);
            }

            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            public void onCancelled(DatabaseError databaseError) {
            }
        });
        googleProgressBar = (GoogleProgressBar) findViewById(R.id.google_progress);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void isEmpty(boolean isEmpty) {
        googleProgressBar.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
    }
}
