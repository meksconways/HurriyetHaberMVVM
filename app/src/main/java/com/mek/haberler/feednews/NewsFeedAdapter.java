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
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsFeedAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private final List<FeedNewsModel> data = new ArrayList<>();
    private final NewsSelectedListener listener;
    NewsFeedViewModel viewModel;
    int type_cat = 0;
    int type_news = 1;
    LifecycleOwner owner;

    NewsFeedAdapter(NewsFeedViewModel viewModel, LifecycleOwner lifecycleOwner, NewsSelectedListener listener){
        this.viewModel = viewModel;
        this.owner = lifecycleOwner;
        this.listener = listener;
        viewModel.getNews().observe(lifecycleOwner, newsList -> {

            if (newsList == null){
                data.clear();
                notifyDataSetChanged();
                return;
            }
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new FeedNewsDiffCallback(data,newsList));
            data.clear();
            data.addAll(newsList);
            diffResult.dispatchUpdatesTo(this);
        });

        setHasStableIds(true);


    }
    @Override
    public int getItemViewType(int position) {
        if (position == 0){
            return type_cat;
        }else{
            return type_news;
        }
    }

    @Override
    public long getItemId(int position) {
        if (position == 0){
            return 0;
        }else{
            return Long.parseLong(data.get(position - 1).id);
        }

    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == type_cat){

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cat_rv,parent,false);
            return new CategoryViewHolder(view);

        }else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_default_news_card,parent,false);
            return new NewsViewHolder(view,listener);
        }


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder.getItemViewType() == type_cat){

            CategoryViewHolder holder1 = (CategoryViewHolder)holder;
            holder1.bind();
            holder1.recyclerView.setAdapter(new CategoryAdapter(viewModel,owner));
            holder1.recyclerView.setLayoutManager(new LinearLayoutManager(holder1.recyclerView.getContext(),
                    LinearLayoutManager.HORIZONTAL, false));
            viewModel.setCatModel(false);





        }else{
            NewsViewHolder holder0 = (NewsViewHolder)holder;
            holder0.bind(data.get(position - 1));
        }

    }


    @Override
    public int getItemCount() {
       return data.size() + 1;
    }

    static final class CategoryViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.rv_cat)
        RecyclerView recyclerView;

        CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
        void bind(){
            Log.d( "observeViewModel: ", "girdi");
        }
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
