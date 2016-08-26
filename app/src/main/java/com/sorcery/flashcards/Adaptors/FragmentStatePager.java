package com.sorcery.flashcards.Adaptors;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.sorcery.flashcards.Activities.ScreenSlidePageFragment;
import com.sorcery.flashcards.Model.CardModel;

import java.util.ArrayList;
import java.util.WeakHashMap;

/**
 * Created by Ritesh Shakya on 8/21/2016.
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class FragmentStatePager extends FragmentStatePagerAdapter {

    private final ArrayList<CardModel> cards;
    private final EmptyInterface anInterface;

    private WeakHashMap<Integer, ScreenSlidePageFragment> mFragments;

    public FragmentStatePager(FragmentManager fm, EmptyInterface anInterface) {
        super(fm);
        this.anInterface = anInterface;
        cards = new ArrayList<>();
        mFragments = new WeakHashMap<>();
    }

    @Override
    public Fragment getItem(int position) {
        ScreenSlidePageFragment item = ScreenSlidePageFragment.newInstance(cards.get(position));
        mFragments.put(position, item);
        return item;
    }

    @Override
    public int getCount() {
        return cards.size();
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        for (Integer position : mFragments.keySet()) {
            //Make sure we only update fragments that should be seen
            if (position != null && mFragments.get(position) != null && mFragments.get(position).getData().isVisible()) {
                mFragments.get(position).updateCheckedStatus();
            }
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        Integer key = position;
        if (mFragments.containsKey(key)) {
            mFragments.remove(key);
        }
    }

    public void addCard(CardModel card) {
        if (!cards.contains(card)) {
            cards.add(card);
        }
        if (cards.size() > 0) {
            anInterface.isEmpty(false);
        }
        this.notifyDataSetChanged();
    }

    public void removeCard(CardModel card) {
        cards.remove(card);
        if (cards.size() == 0) {
            anInterface.isEmpty(true);
        }
        this.notifyDataSetChanged();
    }

    public interface EmptyInterface {
        void isEmpty(boolean isEmpty);
    }
}