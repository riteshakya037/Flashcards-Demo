package com.sorcery.flashcards.Adaptors;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.sorcery.flashcards.Activities.ScreenSlidePageFragment;
import com.sorcery.flashcards.Model.CardModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.WeakHashMap;

/**
 * Created by Ritesh Shakya on 8/21/2016.
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class FragmentStatePager extends FragmentStatePagerAdapter {

    /**
     * Dataset to be displayed.
     */
    private final ArrayList<CardModel> cards;
    /**
     * Callback listener which observers certain change in dataset.
     */
    private final EmptyInterface anInterface;

    /**
     * Stores the list of Fragments added to adapter with their respective position as key.
     */
    private WeakHashMap<Integer, ScreenSlidePageFragment> mFragments;

    public FragmentStatePager(FragmentManager fm, EmptyInterface anInterface) {
        super(fm);
        this.anInterface = anInterface;
        cards = new ArrayList<>();
        mFragments = new WeakHashMap<>();
    }

    @Override
    public Fragment getItem(int position) {
        ScreenSlidePageFragment item = ScreenSlidePageFragment.newInstance(cards.get(position)); // Add Fragments with position as key.
        mFragments.put(position, item);
        return item;
    }

    @Override
    public int getCount() {
        return cards.size();
    }

    public void notifyChanged() {
        super.notifyDataSetChanged();
        for (Integer position : mFragments.keySet()) {
            //Make sure we only update fragments that are visible
            if (position != null && mFragments.get(position) != null && mFragments.get(position).isVisible()) {
                mFragments.get(position).updateCheckedStatus(cards.get(position)); // Forced update since {@link PagerAdapter#notifyDataSetChanged()} doesn't update active fragments.
            }
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        if (mFragments.containsKey(position)) {
            mFragments.remove(position);  // remove Fragments with are destroyed from adaptor.
        }
    }

    /**
     * Called whenever callback from firebase for addition of data from real-time database takes place
     *
     * @param card Card to be added.
     */
    public void addCard(CardModel card) {
        if (!cards.contains(card)) {
            cards.add(card);
        }
        anInterface.updateCount(cards.size()); // Broadcast change in card count.
        if (cards.size() > 0) {
            anInterface.isEmpty(false);  // Broadcast that dataset is not empty.
        }
        this.notifyDataSetChanged();
    }

    /**
     * Called whenever callback from firebase for deletion of data from real-time database takes place
     *
     * @param card Card to be removed.
     */
    public void removeCard(CardModel card) {
        cards.remove(card);
        anInterface.updateCount(cards.size()); // Broadcast change in card count.
        if (cards.size() == 0) {
            anInterface.isEmpty(true); // Broadcast that dataset is empty.
        }
        this.notifyDataSetChanged();
    }

    /**
     * Randomize the dataset and force view update.
     */
    public void randomize() {
        long seed = System.nanoTime();
        Collections.shuffle(cards, new Random(seed)); // Shuffle items in ArrayList.
        this.notifyChanged();
    }

    /**
     * Callback for the data state of the adapter.
     */
    public interface EmptyInterface {
        void isEmpty(boolean isEmpty);

        void updateCount(int count);
    }
}