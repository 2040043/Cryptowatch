package com.cryptowatch.app.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cryptowatch.app.data.Database;
import com.cryptowatch.app.data.ListRepository;
import com.cryptowatch.app.data.PortfolioRepository;
import com.cryptowatch.app.models.Currency;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ListViewModel extends ViewModel {
    private ListRepository listRepository;
    private PortfolioRepository portfolioRepository;

    private MutableLiveData<List<Currency>> currencies;
    private MutableLiveData<List<Currency>> market;
    private MutableLiveData<List<Currency>> portfolio;
    private MutableLiveData<List<Currency>> search;

    public ListViewModel(Database database) {
        this.listRepository = new ListRepository();
        this.portfolioRepository = PortfolioRepository.getInstance(database);

        this.currencies = listRepository.getAllCurrencies();
        this.market = listRepository.getCurrenciesByTopList();
        this.portfolio = portfolioRepository.getCurrenciesFromPortfolio();
        this.search = new MutableLiveData<>(new ArrayList<>());
    }

    public LiveData<List<Currency>> getMarket() {
        return market;
    }

    public LiveData<List<Currency>> getPortfolio() {
        return portfolio;
    }

    public LiveData<List<Currency>> getSearch() {
        return search;
    }

    public void checkMarketInPortfolio() {
        if (market.getValue() == null || portfolio.getValue() == null) {
            return;
        }

        List<String> currenciesId = portfolio.getValue()
                .stream()
                .map(Currency::getId)
                .collect(Collectors.toList());

        market.getValue().forEach(
                currency -> currency.setInPortfolio(currenciesId.contains(currency.getId())));
    }

    public void handlePortfolioChange(Currency currency, boolean inPortfolio) {
        if (inPortfolio) {
            portfolioRepository.insertCurrency(currency);
        }
        else {
            portfolioRepository.deleteCurrency(currency);
        }
    }

    public void setSearch(String query) {
        if (currencies.getValue() == null) {
            return;
        }

        String q = query.trim().toLowerCase();
        List<Currency> list = currencies.getValue()
                .stream()
                .filter(c -> c.getId().toLowerCase().contains(q) || c.getName().toLowerCase().contains(q))
                .collect(Collectors.toList());

        search.setValue(list);
    }
}
