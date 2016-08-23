package com.sorcery.flashcards.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;

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
import com.sorcery.flashcards.Helper.DatabaseContract;
import com.sorcery.flashcards.Helper.DownloadMp3Async;
import com.sorcery.flashcards.Model.CardModel;
import com.sorcery.flashcards.R;

public class MainActivity extends AppCompatActivity implements FragmentStatePagerAdapter.EmptyInterface {
    private GoogleProgressBar googleProgressBar;
    private DatabaseContract.DbHelper dbHelper;
    private String TAG = "MainActivity";

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        // Set up the ViewPager with the sections adapter.
        MultiViewPager mViewPager = (MultiViewPager) findViewById(R.id.pager);


        // Get the Firebase app and all primitives we'll use
        FirebaseApp app = FirebaseApp.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance(app);

        // Instantiate local Database
        dbHelper = new DatabaseContract.DbHelper(this);


        final FragmentStatePagerAdapter adapter = new FragmentStatePagerAdapter(getSupportFragmentManager(), this);

        mViewPager.setAdapter(adapter);
        mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        // Get a reference to cards in the database
        DatabaseReference databaseRef = database.getReference("cards");
        databaseRef.addChildEventListener(new ChildEventListener() {
            public void onChildAdded(DataSnapshot snapshot, String s) {
                // Get the card from the snapshot and add it to the UI
                CardModel card = snapshot.getValue(CardModel.class);
                adapter.addCard(card);
                if (!dbHelper.checkExist(card.voiceMale)) {
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
        googleProgressBar = (GoogleProgressBar) findViewById(R.id.google_progress);
    }

//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public void isEmpty(boolean isEmpty) {
        googleProgressBar.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
    }
}
