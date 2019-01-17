package com.mek.haberler.readlaterpage;

import com.mek.haberler.roomdb.NewsDB;

import java.util.List;

import androidx.recyclerview.widget.DiffUtil;

class ReadLaterDiffCallback extends DiffUtil.Callback {

   private final List<NewsDB> oldList;
   private final List<NewsDB> newList;

    ReadLaterDiffCallback(List<NewsDB> oldList, List<NewsDB> newList) {
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
        return oldList.get(oldItemPosition).getNewsId().equals(newList.get(newItemPosition).getNewsId());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
    }
}
