package com.sorcery.flashcards.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ritesh Shakya on 8/21/2016.
 */

public class CardModel implements Parcelable {

    public String englishWord;
    public String greekWord;
    public String pronunciationGreek;
    public String voiceMale;

    private boolean visible = false;
    private int position;

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

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void currentPos(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }
}
