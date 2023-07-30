package com.cryptowatch.app.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cryptowatch.R;
import com.cryptowatch.app.interfaces.CurrencyClickListener;
import com.cryptowatch.app.models.Currency;
import com.cryptowatch.app.utilities.Constants;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

public class SearchRecyclerViewAdapter
        extends RecyclerView.Adapter<SearchRecyclerViewAdapter.SearchViewHolder> implements Filterable{

    private final LayoutInflater inflater;
    private final List<Currency> list;
    private final List<Currency> filteredList;
    private final CurrencyClickListener clickListener;

    private final Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Currency> newFilteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                newFilteredList.addAll(list);
            }
            else {
                String filter = constraint.toString().toLowerCase().trim();
                for (Currency currency : list) {
                    if (currency.getId().toLowerCase().contains(filter) || currency.getName().toLowerCase().contains(filter)) {
                        newFilteredList.add(currency);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = newFilteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredList.clear();
            filteredList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public SearchRecyclerViewAdapter(Context context, List<Currency> list, CurrencyClickListener clickListener) {
        this.inflater = LayoutInflater.from(context);
        this.list = list;
        this.filteredList = new ArrayList<>(list);
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.search_layout, parent, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        Currency currency = filteredList.get(position);

        Picasso.get().load(Constants.CURRENCY_LOGO_SOURCE + currency.getImage()).into(holder);
        holder.id.setText(currency.getId());
        holder.name.setText(currency.getName());
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    protected class SearchViewHolder extends RecyclerView.ViewHolder implements Target {
        private final ImageView logo;
        private final TextView id;
        private final TextView name;

        public SearchViewHolder(View view) {
            super(view);

            logo = view.findViewById(R.id.imageCurrency);
            id = view.findViewById(R.id.labelId);
            name = view.findViewById(R.id.labelName);

            setupViewClick(view);
        }

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            logo.setImageBitmap(bitmap);
        }

        @Override
        public void onBitmapFailed(Exception e, Drawable errorDrawable) {
            Log.d("getImage", e.getMessage());
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {}

        public void setupViewClick(View view) {
            view.setOnClickListener(v -> {
                Currency currency = filteredList.get(getAdapterPosition());
                clickListener.onCurrencyClick(currency);
            });
        }
    }
}