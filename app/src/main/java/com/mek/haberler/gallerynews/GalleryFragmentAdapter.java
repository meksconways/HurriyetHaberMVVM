package com.mek.haberler.gallerynews;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mek.haberler.R;
import com.mek.haberler.feednews.NewsFeedModel.FeedNewsModel;
import com.mek.haberler.feednews.NewsFeedModel.Files;
import com.mek.haberler.feednews.NewsSelectedListener;
import com.mek.haberler.gallerynews.model.GalleryNewsModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class GalleryFragmentAdapter extends RecyclerView.Adapter<GalleryFragmentAdapter.GalleryFragmentViewHolder> {


    private final List<GalleryNewsModel> data = new ArrayList<>();
    private final NewsSelectedListener listener;

    public GalleryFragmentAdapter(GalleryFrViewModel viewModel, LifecycleOwner lifecycleOwner,NewsSelectedListener listener) {
        this.listener = listener;
        viewModel.getNews().observe(lifecycleOwner, newsList -> {
            data.clear();
            if (newsList != null){
                data.addAll(newsList);
            }
            notifyDataSetChanged();
        });
        setHasStableIds(true);

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public GalleryFragmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gallery,parent,false);
        return new GalleryFragmentViewHolder(view,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryFragmentViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static final class GalleryFragmentViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.img_gallery)
        ImageView newsPhoto;

        private GalleryNewsModel model;

        GalleryFragmentViewHolder(@NonNull View itemView,NewsSelectedListener listener) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(v -> {
                if (model != null) {
                    listener.onNewsSelected(model.id);
                }
            });


        }
        void bind(GalleryNewsModel newsModel){
            this.model = newsModel;
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
