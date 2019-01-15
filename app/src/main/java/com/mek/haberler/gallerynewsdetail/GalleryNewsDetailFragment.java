package com.mek.haberler.gallerynewsdetail;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mek.haberler.R;
import com.mek.haberler.base.BaseFragment;
import com.mek.haberler.base.MyApplication;
import com.mek.haberler.gallerynews.GalleryFragmentAdapter;
import com.mek.haberler.gallerynewsdetail.model.Files;
import com.mek.haberler.home.MainActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class GalleryNewsDetailFragment extends BaseFragment {


    private Unbinder unbinder;

    @BindView(R.id.rv_gallery)
    RecyclerView recyclerView;
    private GalleryNewsDetailViewModel viewmodel;
    private Context context;

    public static GalleryNewsDetailFragment newInstance(int instance) {

        Bundle args = new Bundle();

        GalleryNewsDetailFragment fragment = new GalleryNewsDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        MyApplication.getAppComponent(context).inject(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
            unbinder = null;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lay_gallery_detail_item,container,false);

        unbinder = ButterKnife.bind(this,view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewmodel = ViewModelProviders.of(getActivity()).get(GalleryNewsDetailViewModel.class);

        SnapHelper startSnapHelper = new PagerSnapHelper();
        startSnapHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(new GalleryNewsDetailAdapter(viewmodel,this));
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        //noinspection ConstantConditions
        ((MainActivity)getActivity()).updateToolbarTitle("Galeri");
        observeViewModel();

    }

    private void observeViewModel() {

    }
}
