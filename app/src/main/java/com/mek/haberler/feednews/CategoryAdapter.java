package com.mek.haberler.feednews;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mek.haberler.R;
import com.mek.haberler.feednews.categorymodel.CategoryModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryItemViewHolder>{


    private final List<CategoryModel> data = new ArrayList<>();

    CategoryAdapter(NewsFeedViewModel viewModel, LifecycleOwner owner) {

        viewModel.getCategories().observe(owner, newsList -> {
            if (newsList == null){
                data.clear();
                notifyDataSetChanged();
                return;
            }
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new CatDiffCallback(data,newsList));
            if (data.isEmpty()){
                data.clear();
                data.addAll(newsList);
            }
            
            diffResult.dispatchUpdatesTo(this);
        });
        setHasStableIds(true);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public CategoryItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category,parent,false);
        return new CategoryItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryItemViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static final class CategoryItemViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.txt_cat)
        TextView txtCat;
        @BindView(R.id.img_cat)
        ImageView imgCat;

        CategoryItemViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
        void bind(CategoryModel model){

            txtCat.setText(model.name);
            Glide.with(imgCat.getContext())
                    .load(model.image)
                    .apply(new RequestOptions().override(600,400))
                    .into(imgCat);

        }
    }

}
