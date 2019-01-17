package com.mek.haberler.feednews;

import com.mek.haberler.feednews.NewsFeedModel.FeedNewsModel;

import java.util.List;

import androidx.recyclerview.widget.DiffUtil;

class FeedNewsDiffCallback extends DiffUtil.Callback {

    private final List<FeedNewsModel> oldList;
    private final List<FeedNewsModel> newList;

    FeedNewsDiffCallback(List<FeedNewsModel> oldList, List<FeedNewsModel> newList) {
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
        return oldList.get(oldItemPosition).id.equals(newList.get(newItemPosition).id);
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
    }
}
