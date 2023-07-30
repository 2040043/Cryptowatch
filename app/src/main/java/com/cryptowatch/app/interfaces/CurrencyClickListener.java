package com.cryptowatch.app.interfaces;

import com.cryptowatch.app.models.Currency;

public interface CurrencyClickListener {
    void onCurrencyClick(Currency currency);
    void onPortfolioClick(Currency currency, boolean checked);
}
