package com.sorcery.flashcards.Activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.sorcery.flashcards.Helper.Utils;
import com.sorcery.flashcards.Model.CardModel;
import com.sorcery.flashcards.R;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final Utils utils = new Utils(getActivity());

        View rootView = inflater.inflate(R.layout.cards_fragment, container, false);
        CardView relativeLayout = (CardView) rootView.findViewById(R.id.vg_cover);

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) relativeLayout.getLayoutParams();
        layoutParams.width = (int) (utils.getScreenWidth() * .8f);

        return rootView;
    }
}