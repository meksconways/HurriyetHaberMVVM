package com.mek.haberler.readlaterpage;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mek.haberler.R;
import com.mek.haberler.feednews.NewsSelectedListener;
import com.mek.haberler.roomdb.NewsDB;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ReadLaterAdapter extends RecyclerView.Adapter<ReadLaterAdapter.ReadLaterViewHolder>{


    private final List<NewsDB> data = new ArrayList<>();
    private final NewsSelectedListener listener;

    ReadLaterAdapter(ReadLaterFragmentViewModel viewModel, LifecycleOwner lifecycleOwner, NewsSelectedListener listener) {
        this.listener = listener;
        viewModel.getNews().observe(lifecycleOwner, newsDBS -> {

            if (newsDBS == null){
                data.clear();
                notifyDataSetChanged();
                return;
            }
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new ReadLaterDiffCallback(data,newsDBS));
            data.clear();
            data.addAll(newsDBS);
            diffResult.dispatchUpdatesTo(this);
        });
        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(data.get(position).getNewsId());
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public ReadLaterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_default_news_card,parent,false);
        return new ReadLaterViewHolder(view,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ReadLaterViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static final class ReadLaterViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.img_news_photo)
        ImageView newsPhoto;
        @BindView(R.id.txt_news_title)
        TextView newsTitle;

        private NewsDB db;

        ReadLaterViewHolder(@NonNull View itemView, NewsSelectedListener listener) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(v -> {
                if (db != null) {
                    listener.onNewsSelected(db.getNewsId());
                }
            });
        }

        void bind(NewsDB db){
            this.db = db;
            newsTitle.setText(db.getTitle());
            Log.d("bind: ",db.getPhoto());
            if (db.getPhoto() != null){
                Glide.with(newsPhoto.getContext())
                        .load(db.getPhoto())
                        .apply(new RequestOptions().override(800,450))
                        .into(newsPhoto);
            }

        }
    }


}
