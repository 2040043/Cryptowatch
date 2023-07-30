package com.cryptowatch.app.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Currency implements Parcelable {
    public static final String TABLE_NAME = "currency";
    public static final String FIELD_ID = "id";

    private String id;
    private String name;
    private String image;
    private Value value;
    private boolean isInPortfolio;

    public Currency() {

    }

    public Currency(String id, String name, String image, Value value, boolean isInPortfolio) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.value = value;
        this.isInPortfolio = isInPortfolio;
    }

    protected Currency(Parcel in) {
        id = in.readString();
        name = in.readString();
        image = in.readString();
        value = in.readParcelable(Value.class.getClassLoader());
        isInPortfolio = in.readByte() != 0;
    }

    public static final Creator<Currency> CREATOR = new Creator<Currency>() {
        @Override
        public Currency createFromParcel(Parcel in) {
            return new Currency(in);
        }

        @Override
        public Currency[] newArray(int size) {
            return new Currency[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Value getValue() {
        return value;
    }

    public void setValue(Value value) {
        this.value = value;
    }

    public boolean isInPortfolio() {
        return isInPortfolio;
    }

    public void setInPortfolio(boolean inPortfolio) {
        isInPortfolio = inPortfolio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Currency currency = (Currency) o;
        return id.equals(currency.id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(image);
        dest.writeParcelable(value, flags);
        dest.writeByte((byte) (isInPortfolio ? 1 : 0));
    }
}