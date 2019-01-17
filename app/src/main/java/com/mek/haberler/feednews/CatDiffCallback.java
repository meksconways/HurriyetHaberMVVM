package com.mek.haberler.feednews;

import com.mek.haberler.feednews.categorymodel.CategoryModel;

import java.util.List;

import androidx.recyclerview.widget.DiffUtil;

class CatDiffCallback extends DiffUtil.Callback {


    private final List<CategoryModel> oldList;
    private final List<CategoryModel> newList;

    CatDiffCallback(List<CategoryModel> oldList, List<CategoryModel> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).name.equals(newList.get(newItemPosition).name);
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
    }
}
