package com.sorcery.flashcards.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ritesh Shakya on 8/21/2016.
 */

public class CardModel implements Parcelable {

    /**
     * English word for individual card.
     */
    public String englishWord;
    /**
     * Greek word for individual card.
     */
    public String greekWord;
    /**
     * Greek Pronunciation for individual card (Text).
     */
    public String pronunciationGreek;
    /**
     * Greek Pronunciation for individual card (Voice File).
     */
    public String voiceMale;


    /**
     * Default Constructor.
     */
    @SuppressWarnings("unused")
    public CardModel() {
    }

    /**
     * Firebase uses this constructor for initializing class.
     *
     * @param englishWord        English word for individual card.
     * @param greekWord          Greek word for individual card.
     * @param pronunciationGreek Greek Pronunciation for individual card (Text).
     * @param voiceMale          Pronunciation for individual card (Voice File).
     */
    @SuppressWarnings("unused")
    public CardModel(String englishWord, String greekWord, String pronunciationGreek, String voiceMale) {
        this.englishWord = englishWord;
        this.greekWord = greekWord;
        this.pronunciationGreek = pronunciationGreek;
        this.voiceMale = voiceMale;
    }

    /**
     * @param in Parcelable object returned on state changes.
     */
    private CardModel(Parcel in) {
        englishWord = in.readString();
        greekWord = in.readString();
        pronunciationGreek = in.readString();
        voiceMale = in.readString();
    }

    /**
     * Create new object of state changes.
     */
    public static final Creator<CardModel> CREATOR = new Creator<CardModel>() {
        @Override
        public CardModel createFromParcel(Parcel in) {
            return new CardModel(in);
        }

        @Override
        public CardModel[] newArray(int size) {
            return new CardModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Parcelable object written on state changes.
     *
     * @param parcel Parcelable object in which state changes are written.
     * @param i      Flags used to storing Parcelable object
     */
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(englishWord);
        parcel.writeString(greekWord);
        parcel.writeString(pronunciationGreek);
        parcel.writeString(voiceMale);
    }

}
