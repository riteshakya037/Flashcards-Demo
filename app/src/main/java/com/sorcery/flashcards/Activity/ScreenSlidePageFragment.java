package com.sorcery.flashcards.Activity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.sorcery.flashcards.Helper.Utils;
import com.sorcery.flashcards.Model.CardModel;
import com.sorcery.flashcards.R;

import java.io.IOException;

/**
 * Created by Ritesh Shakya on 8/21/2016.
 * A placeholder fragment containing a simple view.
 */
public class ScreenSlidePageFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARG_CARD = "section_card";
    private CardModel cardModel;
    private int sectionNumber;

    public ScreenSlidePageFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ScreenSlidePageFragment newInstance(int sectionNumber, CardModel cardModel) {
        ScreenSlidePageFragment fragment = new ScreenSlidePageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        args.putParcelable(ARG_CARD, cardModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            sectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);
            cardModel = getArguments().getParcelable(ARG_CARD);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        final Utils utils = new Utils(getActivity());

        View rootView = inflater.inflate(R.layout.cards_fragment, container, false);
        CardView relativeLayout = (CardView) rootView.findViewById(R.id.vg_cover);

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) relativeLayout.getLayoutParams();
        layoutParams.width = (int) (utils.getScreenWidth() * .8f);
        TextView greekWord = (TextView) rootView.findViewById(R.id.greekWord);
        TextView englishWord = (TextView) rootView.findViewById(R.id.englishWord);
        final MaterialRippleLayout pronunciationBtn = (MaterialRippleLayout) rootView.findViewById(R.id.pronunciationBtn);
        pronunciationBtn.setEnabled(false);
        final MediaPlayer mp = new MediaPlayer();
        try {
            mp.setDataSource(cardModel.voiceMale);
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

        greekWord.setText(cardModel.greekWord);
        englishWord.setText(cardModel.englishWord);
        pronunciationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp.start();
            }
        });
        return rootView;
    }
}