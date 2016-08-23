package com.sorcery.flashcards.Adaptors;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.sorcery.flashcards.Activities.ScreenSlidePageFragment;
import com.sorcery.flashcards.Model.CardModel;

import java.util.ArrayList;

/**
 * Created by Ritesh Shakya on 8/21/2016.
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class FragmentStatePagerAdapter extends FragmentPagerAdapter {

    private final ArrayList<CardModel> cards;
    private final EmptyInterface anInterface;

    public FragmentStatePagerAdapter(FragmentManager fm, EmptyInterface anInterface) {
        super(fm);
        this.anInterface = anInterface;
        cards = new ArrayList<>();
    }

    @Override
    public Fragment getItem(int position) {
        return ScreenSlidePageFragment.newInstance(cards.get(position));
    }

    @Override
    public int getCount() {
        return cards.size();
    }

    public void addCard(CardModel card) {
        if (!cards.contains(card)) {
            cards.add(card);
        }
        if (cards.size()>0){
            anInterface.isEmpty(false);
        }
        this.notifyDataSetChanged();
    }

    public void removeCard(CardModel card) {
        cards.remove(card);
        if (cards.size()==0){
            anInterface.isEmpty(true);
        }
        this.notifyDataSetChanged();
    }

    public interface EmptyInterface {
        void isEmpty(boolean isEmpty);
    }
}