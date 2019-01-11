package com.mek.haberler.feednews;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mek.haberler.R;
import com.mek.haberler.feednews.NewsFeedModel.FeedNewsModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsFeedAdapter extends RecyclerView.Adapter<NewsFeedAdapter.NewsViewHolder> {


    private final List<FeedNewsModel> data = new ArrayList<>();
    private final NewsSelectedListener listener;

    NewsFeedAdapter(NewsFeedViewModel viewModel, LifecycleOwner lifecycleOwner, NewsSelectedListener listener){
        this.listener = listener;
        viewModel.getNews().observe(lifecycleOwner, newsList -> {
            data.clear();
            if (newsList != null){
                data.addAll(newsList);
            }
            notifyDataSetChanged(); // Todo: AutoValue Eklenince DiffUtil class kullanılacak
        });
        setHasStableIds(true);


    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(data.get(position).id);
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_default_news_card,parent,false);
        return new NewsViewHolder(view,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder newsViewHolder, int position) {
        newsViewHolder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static final class NewsViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.img_news_photo)
        ImageView newsPhoto;
        @BindView(R.id.txt_news_title)
        TextView newsTitle;

        private FeedNewsModel model;

        NewsViewHolder(@NonNull View itemView,NewsSelectedListener listener) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(v -> {
                if (model != null) {
                    listener.onNewsSelected(model.id);
                }
            });
        }

        void bind(FeedNewsModel newsModel){
            this.model = newsModel;
            newsTitle.setText(newsModel.title);
            if (newsModel.files.size() > 0){
                if (newsModel.files.get(0).fileUrl != null){
                    Glide.with(newsPhoto.getContext())
                            .load(newsModel.files.get(0).fileUrl)
                            .apply(new RequestOptions().override(800, 450))
                            .into(newsPhoto);
                }
            }
        }
    }

}
