package com.cryptowatch.app.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cryptowatch.app.data.CurrencyRepository;
import com.cryptowatch.app.data.Database;
import com.cryptowatch.app.data.PortfolioRepository;
import com.cryptowatch.app.models.Currency;
import com.cryptowatch.app.models.Ohlc;

import java.util.List;

public class CurrencyViewModel extends ViewModel {
    private CurrencyRepository currencyRepository;
    private PortfolioRepository portfolioRepository;

    private MutableLiveData<Currency> currency;
    private MutableLiveData<List<Ohlc>> ohlc;
    private MutableLiveData<List<Currency>> portfolio;

    public CurrencyViewModel(Currency currency, Database database) {
        this.currencyRepository = new CurrencyRepository(currency);
        this.portfolioRepository = PortfolioRepository.getInstance(database);

        this.currency = currencyRepository.getCurrencyValue();
        this.ohlc = currencyRepository.getOhlc(currency.getId(), "hourly", 24);
        this.portfolio = portfolioRepository.getCurrenciesFromPortfolio();
    }

    public LiveData<Currency> getCurrency() {
        return currency;
    }

    public LiveData<List<Ohlc>> getOhlc() {
        return ohlc;
    }

    public void fetchOhlc(String type, int count) {
        currencyRepository.getOhlc(currency.getValue().getId(), type, count);
    }

    public void handlePortfolioChange(Currency currency, boolean inPortfolio) {
        if (inPortfolio) {
            portfolioRepository.insertCurrency(currency);
        }
        else {
            portfolioRepository.deleteCurrency(currency);
        }
    }

    public boolean isInPortfolio(Currency currency) {
        if (portfolio.getValue() == null) {
            return false;
        }

        return portfolio.getValue()
                .stream()
                .anyMatch(c -> c.getId().equals(currency.getId()));
    }
}
