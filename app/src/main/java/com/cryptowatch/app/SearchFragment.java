package com.cryptowatch.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cryptowatch.R;
import com.cryptowatch.app.adapters.SearchRecyclerViewAdapter;
import com.cryptowatch.app.data.Database;
import com.cryptowatch.app.interfaces.CurrencyClickListener;
import com.cryptowatch.app.models.Currency;
import com.cryptowatch.app.viewmodels.ListViewModel;
import com.cryptowatch.app.viewmodels.ListViewModelFactory;

public class SearchFragment extends Fragment implements CurrencyClickListener {
    private ListViewModel viewModel;
    private SearchRecyclerViewAdapter adapter;

    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        viewModel = new ViewModelProvider(requireActivity(),
                new ListViewModelFactory(new Database(requireActivity()))).get(ListViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.cryptocurrencyRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        viewModel.getSearch().observe(getViewLifecycleOwner(), data -> {
            adapter = new SearchRecyclerViewAdapter(getContext(), data, this);
            recyclerView.setAdapter(adapter);
        });
        viewModel.setSearch("");
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                viewModel.setSearch(newText);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onCurrencyClick(Currency currency) {
        Intent intent = new Intent(getActivity(), CurrencyActivity.class);
        intent.putExtra(CurrencyActivity.CURRENCY_INTENT_KEY, currency);
        startActivity(intent);
    }

    @Override
    public void onPortfolioClick(Currency currency, boolean checked) {
        viewModel.handlePortfolioChange(currency, checked);
    }
}