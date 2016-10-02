package com.sorcery.flashcards.Activities;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.sorcery.flashcards.CustomViews.FlipAnimation;
import com.sorcery.flashcards.Helper.DatabaseContract;
import com.sorcery.flashcards.Helper.Utils;
import com.sorcery.flashcards.Model.CardModel;
import com.sorcery.flashcards.Model.CurrentMode;
import com.sorcery.flashcards.R;

import java.io.File;
import java.io.IOException;

import static com.sorcery.flashcards.Activities.MainActivity.current_mode;

/**
 * Created by Ritesh Shakya on 8/21/2016.
 * A placeholder fragment containing a simple view.
 */
public class ScreenSlidePageFragment extends Fragment implements View.OnClickListener {

    /**
     * Text Displayed on the frontSide onf the card. Not necessary the visible side.
     */
    TextView frontText;
    /**
     * Text Displayed on the backSide onf the card.
     */
    TextView backText;
    /**
     * Layout used as a button for pronunciation also has ripple effect.
     */
    MaterialRippleLayout pronunciationBtn;
    /**
     * Stores the pronunciation of each card for playback.
     */
    MediaPlayer mp;

    /**
     * The fragment argument representing the key for storing {@link CardModel} in {@link Bundle}
     */
    private static final String ARG_CARD = "section_card";
    /**
     * Has all the necessary data about the card to be displayed.
     */
    private CardModel cardModel;

    /**
     * Default constructor to be used by Factory Method.
     */
    public ScreenSlidePageFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ScreenSlidePageFragment newInstance(CardModel cardModel) {
        ScreenSlidePageFragment fragment = new ScreenSlidePageFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_CARD, cardModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cardModel = getArguments().getParcelable(ARG_CARD);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        final Utils utils = new Utils(getActivity());

        View rootView = inflater.inflate(R.layout.cards_fragment, container, false);
        RelativeLayout relativeLayout = (RelativeLayout) rootView.findViewById(R.id.vg_cover);

        // Set the width of cards according to width of screen
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) relativeLayout.getLayoutParams();
        layoutParams.width = (int) (utils.getScreenWidth() * .8f);
        relativeLayout.setOnClickListener(this);

        frontText = (TextView) rootView.findViewById(R.id.frontText);
        backText = (TextView) rootView.findViewById(R.id.backText);
        pronunciationBtn = (MaterialRippleLayout) rootView.findViewById(R.id.pronunciationBtn);
        pronunciationBtn.setEnabled(false);

        // Instantiate the various views of the card.
        updateCheckedStatus(cardModel);

        pronunciationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp.start();
            }
        });

        return rootView;
    }


    /**
     * Default callback method for {@link View#setOnClickListener(View.OnClickListener)} .
     *
     * @param view View that was clicked.
     */
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.vg_cover)
            flipCard();
    }

    /**
     * Flips the card on each call.
     */
    private void flipCard() {
        View rootLayout = getView().findViewById(R.id.vg_cover);
        View cardFace = getView().findViewById(R.id.cardFront);
        View cardBack = getView().findViewById(R.id.cardBack);

        FlipAnimation flipAnimation = new FlipAnimation(cardFace, cardBack);

        if (cardFace.getVisibility() == View.GONE) {
            flipAnimation.reverse();
        }
        rootLayout.startAnimation(flipAnimation);
    }

    /**
     * Update the fragment to reflect the change in data. Forced update since {@link PagerAdapter#notifyDataSetChanged()} doesnt update active fragments.
     *
     * @param cardModel
     */
    public void updateCheckedStatus(CardModel cardModel) {
        this.cardModel = cardModel;
        frontText.setText(current_mode == CurrentMode.GREEK ? cardModel.greekWord : cardModel.englishWord);
        backText.setText(current_mode == CurrentMode.GREEK ? cardModel.englishWord : cardModel.greekWord);
        mp = new MediaPlayer();
        try {
            DatabaseContract.DbHelper dbHelper = new DatabaseContract.DbHelper(getActivity());
            if (dbHelper.checkExist(cardModel.voiceMale) && new File(dbHelper.onSelect(cardModel.voiceMale)).exists()) { // If record exists on local database load from cache.
                mp.setDataSource(dbHelper.onSelect(cardModel.voiceMale));
            } else {
                mp.setDataSource(cardModel.voiceMale); // Else load from web.
            }
            dbHelper.close();
            mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    pronunciationBtn.setEnabled(true); // Only enable if pronunciation has finished loading
                }
            });
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    pronunciationBtn.setRadius(0); // While playing display ripple effect. Dismiss on completion.
                }
            });
            mp.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return Current data of the fragment.
     */
    public CardModel getData() {
        return cardModel;
    }
}