package com.cryptowatch.app.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.cryptowatch.app.data.Database;

public class ListViewModelFactory implements ViewModelProvider.Factory {
    private Database database;

    public ListViewModelFactory(Database database) {
        this.database = database;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> aClass) {
        return (T) new ListViewModel(database);
    }
}
