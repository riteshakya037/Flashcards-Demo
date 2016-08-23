package com.sorcery.flashcards.Model;

import android.os.Parcel;
import android.os.Parcelable;
import android.test.suitebuilder.annotation.Suppress;

/**
 * Created by Ritesh Shakya on 8/21/2016.
 */

public class CardModel implements Parcelable {

    public String englishWord;
    public String greekWord;
    public String pronunciationGreek;
    public String voiceMale;

    public CardModel() {
    }

    public CardModel(String englishWord, String greekWord, String pronunciationGreek, String voiceMale) {
        this.englishWord = englishWord;
        this.greekWord = greekWord;
        this.pronunciationGreek = pronunciationGreek;
        this.voiceMale = voiceMale;
    }

    protected CardModel(Parcel in) {
        englishWord = in.readString();
        greekWord = in.readString();
        pronunciationGreek = in.readString();
        voiceMale = in.readString();
    }

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

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(englishWord);
        parcel.writeString(greekWord);
        parcel.writeString(pronunciationGreek);
        parcel.writeString(voiceMale);
    }
}
