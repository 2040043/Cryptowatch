package com.cryptowatch.app.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.cryptowatch.app.models.Currency;

public class Database extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "db.sqlite";

    // to initiate onUpgrade method call you have to change version argument
    public Database(Context context) {
        super(context, DATABASE_NAME, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(String.format("CREATE TABLE IF NOT EXISTS %s (%s TEXT PRIMARY KEY);",
                Currency.TABLE_NAME, Currency.FIELD_ID));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(String.format("DROP TABLE IF EXISTS %s;", Currency.TABLE_NAME));
        onCreate(db);
    }
}
