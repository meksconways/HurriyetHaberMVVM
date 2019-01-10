package com.mek.haberler.feednews;

import android.arch.lifecycle.LifecycleOwner;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
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

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsFeedAdapter extends RecyclerView.Adapter<NewsFeedAdapter.NewsViewHolder> {


    private final List<FeedNewsModel> data = new ArrayList<>();

    NewsFeedAdapter(NewsFeedViewModel viewModel, LifecycleOwner lifecycleOwner){
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
        return new NewsViewHolder(view);
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

        NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(itemView);
        }

        void bind(FeedNewsModel newsModel){
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
