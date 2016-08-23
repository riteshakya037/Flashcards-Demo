package com.sorcery.flashcards.Adaptors;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.sorcery.flashcards.Activity.ScreenSlidePageFragment;
import com.sorcery.flashcards.Model.CardModel;

import java.util.ArrayList;

/**
 * Created by Ritesh Shakya on 8/21/2016.
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class FragmentStatePagerAdapter extends FragmentPagerAdapter {

    private ArrayList<CardModel> cards;
    private EmptyInterface anInterface;

    public FragmentStatePagerAdapter(FragmentManager fm, EmptyInterface anInterface) {
        super(fm);
        this.anInterface = anInterface;
        cards = new ArrayList<>();
    }

    @Override
    public Fragment getItem(int position) {
        return ScreenSlidePageFragment.newInstance(position, cards.get(position));
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
        notifyDataSetChanged();
    }

    public void removeCard(CardModel card) {
        cards.remove(card);
        if (cards.size()==0){
            anInterface.isEmpty(true);
        }
        notifyDataSetChanged();
    }

    public interface EmptyInterface {
        public void isEmpty(boolean isEmpty);
    }
}