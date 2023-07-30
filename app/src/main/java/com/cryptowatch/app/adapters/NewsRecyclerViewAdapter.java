package com.cryptowatch.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cryptowatch.R;
import com.cryptowatch.app.interfaces.NewsClickListener;
import com.cryptowatch.app.models.NewsArticle;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsRecyclerViewAdapter
        extends RecyclerView.Adapter<NewsRecyclerViewAdapter.NewsViewHolder> {

    private final LayoutInflater inflater;
    private final List<NewsArticle> data;
    private final NewsClickListener clickListener;

    public NewsRecyclerViewAdapter(Context context, List<NewsArticle> data, NewsClickListener clickListener) {
        this.inflater = LayoutInflater.from(context);
        this.data = data;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.news_article_layout, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        NewsArticle article = data.get(position);
        Picasso.get().load(article.getImage()).into(holder.image);
        holder.title.setText(article.getTitle());
        holder.source.setText(article.getSource());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    protected class NewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView image;
        private final TextView title;
        private final TextView source;

        public NewsViewHolder(View view) {
            super(view);
            image = view.findViewById(R.id.imageArticle);
            title = view.findViewById(R.id.labelTitle);
            source = view.findViewById(R.id.labelSource);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            NewsArticle article = data.get(getAdapterPosition());
            clickListener.onNewsClicked(article);
        }
    }
}
