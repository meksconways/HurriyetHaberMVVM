package com.mek.haberler.gallerynewsdetail;

import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mek.haberler.R;
import com.mek.haberler.gallerynewsdetail.model.Files;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class GalleryNewsDetailAdapter extends RecyclerView.Adapter<GalleryNewsDetailAdapter.GalleryNewsDetailViewHolder>{

    private final List<Files> data = new ArrayList<>();


    public GalleryNewsDetailAdapter(GalleryNewsDetailViewModel viewModel, LifecycleOwner lifecycleOwner) {

        viewModel.getFiles().observe(lifecycleOwner, files -> {
            data.clear();
            if (files != null){
                data.addAll(files);
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
    public GalleryNewsDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gallery_each,parent,false);
        return new GalleryNewsDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryNewsDetailViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class GalleryNewsDetailViewHolder extends RecyclerView.ViewHolder{


        @BindView(R.id.img_gallery)
        ImageView img;
        @BindView(R.id.txt_desc)
        TextView txt;


        GalleryNewsDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        void bind(Files files){

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                txt.setText(Html.fromHtml(files.metadata.description, Html.FROM_HTML_MODE_COMPACT));
            } else {
                txt.setText(Html.fromHtml(files.metadata.description));
            }
            Glide.with(txt.getContext())
                    .load(files.fileUrl)
                    .apply(new RequestOptions().override(800,450))
                    .into(img);

        }
    }


}
