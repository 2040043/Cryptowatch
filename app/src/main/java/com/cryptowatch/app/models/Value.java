package com.cryptowatch.app.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Value implements Parcelable {
    private double rawPrice;
    private String price;
    private String marketCap;
    private String supply;
    private String change1H;
    private String change24H;
    private String volume24H;
    private String high24H;
    private String low24H;

    public Value() {

    }

    public Value(double rawPrice, String price, String marketCap, String supply, String change1H, String change24H, String volume24H, String high24H, String low24H) {
        this.rawPrice = rawPrice;
        this.price = price;
        this.marketCap = marketCap;
        this.supply = supply;
        this.change1H = change1H;
        this.change24H = change24H;
        this.volume24H = volume24H;
        this.high24H = high24H;
        this.low24H = low24H;
    }

    protected Value(Parcel in) {
        rawPrice = in.readDouble();
        price = in.readString();
        marketCap = in.readString();
        supply = in.readString();
        change1H = in.readString();
        change24H = in.readString();
        volume24H = in.readString();
        high24H = in.readString();
        low24H = in.readString();
    }

    public static final Creator<Value> CREATOR = new Creator<Value>() {
        @Override
        public Value createFromParcel(Parcel in) {
            return new Value(in);
        }

        @Override
        public Value[] newArray(int size) {
            return new Value[size];
        }
    };

    public double getRawPrice() {
        return rawPrice;
    }

    public void setRawPrice(double rawPrice) {
        this.rawPrice = rawPrice;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getMarketCap() {
        return marketCap;
    }

    public void setMarketCap(String marketCap) {
        this.marketCap = marketCap;
    }

    public String getSupply() {
        return supply;
    }

    public void setSupply(String supply) {
        this.supply = supply;
    }

    public String getChange1H() {
        return change1H;
    }

    public void setChange1H(String change1H) {
        this.change1H = change1H;
    }

    public String getChange24H() {
        return change24H;
    }

    public void setChange24H(String change24H) {
        this.change24H = change24H;
    }

    public String getVolume24H() {
        return volume24H;
    }

    public void setVolume24H(String volume24H) {
        this.volume24H = volume24H;
    }

    public String getHigh24H() {
        return high24H;
    }

    public void setHigh24H(String high24H) {
        this.high24H = high24H;
    }

    public String getLow24H() {
        return low24H;
    }

    public void setLow24H(String low24H) {
        this.low24H = low24H;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(rawPrice);
        dest.writeString(price);
        dest.writeString(marketCap);
        dest.writeString(supply);
        dest.writeString(change1H);
        dest.writeString(change24H);
        dest.writeString(volume24H);
        dest.writeString(high24H);
        dest.writeString(low24H);
    }
}
