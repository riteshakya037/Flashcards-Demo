package com.sorcery.flashcards.Activities;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import java.io.IOException;

import static com.sorcery.flashcards.Activities.MainActivity.current_mode;

/**
 * Created by Ritesh Shakya on 8/21/2016.
 * A placeholder fragment containing a simple view.
 */
public class ScreenSlidePageFragment extends Fragment implements View.OnClickListener {

    TextView frontText;
    TextView backText;
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_CARD = "section_card";
    private CardModel cardModel;

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

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) relativeLayout.getLayoutParams();
        layoutParams.width = (int) (utils.getScreenWidth() * .8f);
        relativeLayout.setOnClickListener(this);

        frontText = (TextView) rootView.findViewById(R.id.frontText);
        backText = (TextView) rootView.findViewById(R.id.backText);
        final MaterialRippleLayout pronunciationBtn = (MaterialRippleLayout) rootView.findViewById(R.id.pronunciationBtn);
        pronunciationBtn.setEnabled(false);
        final MediaPlayer mp = new MediaPlayer();
        try {
            DatabaseContract.DbHelper dbHelper = new DatabaseContract.DbHelper(getActivity());
            if (dbHelper.checkExist(cardModel.voiceMale)) {
                mp.setDataSource(dbHelper.onSelect(cardModel.voiceMale));
            } else {
                mp.setDataSource(cardModel.voiceMale);
            }
            mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    pronunciationBtn.setEnabled(true);
                }
            });
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    pronunciationBtn.setRadius(0);
                }
            });
            mp.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }

        updateCheckedStatus();
        pronunciationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp.start();
            }
        });
        cardModel.setVisible(true);
        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        cardModel.setVisible(false);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.vg_cover)
            flipCard();
    }

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

    public void updateCheckedStatus() {
        frontText.setText(current_mode == CurrentMode.GREEK ? cardModel.greekWord : cardModel.englishWord);
        backText.setText(current_mode == CurrentMode.GREEK ? cardModel.englishWord : cardModel.greekWord);
    }

    public CardModel getData() {
        return cardModel;
    }
}