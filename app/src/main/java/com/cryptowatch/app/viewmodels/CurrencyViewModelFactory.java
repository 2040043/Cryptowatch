package com.cryptowatch.app.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.cryptowatch.app.data.Database;
import com.cryptowatch.app.models.Currency;

public class CurrencyViewModelFactory implements ViewModelProvider.Factory {
    private Currency currency;
    private Database database;

    public CurrencyViewModelFactory(Currency currency, Database database) {
        this.currency = currency;
        this.database = database;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> aClass) {
        return (T) new CurrencyViewModel(currency, database);
    }
}
