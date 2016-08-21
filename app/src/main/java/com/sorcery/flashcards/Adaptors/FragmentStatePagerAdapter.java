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

    public FragmentStatePagerAdapter(FragmentManager fm) {
        super(fm);
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
        cards.add(card);
        notifyDataSetChanged();
    }

    public void removeCard(CardModel card) {
        cards.remove(card);
        notifyDataSetChanged();
    }
}