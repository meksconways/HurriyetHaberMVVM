package com.mek.haberler.gallerynews;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mek.haberler.R;
import com.mek.haberler.base.BaseFragment;
import com.mek.haberler.base.MyApplication;
import com.mek.haberler.feednews.NewsSelectedListener;
import com.mek.haberler.gallerynewsdetail.GalleryHeaderPageFragment;
import com.mek.haberler.home.MainActivity;
import com.mek.haberler.util.Util;
import com.mek.haberler.viewmodel.ViewModelFactory;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class GalleryFragment extends BaseFragment implements NewsSelectedListener {

    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.txt_errorMsg)
    TextView txt_errorMsg;
    @BindView(R.id.rv_gallery)
    RecyclerView recyclerView;
    @BindView(R.id.lyt_refresh)
    SwipeRefreshLayout refreshLayout;
    private Unbinder unbinder;
    private Context context;

    @Inject
    ViewModelFactory viewModelFactory;
    private GalleryFrViewModel viewmodel;

    public static GalleryFragment newInstance(int instance) {
        Bundle args = new Bundle();
        args.putInt(ARGS_INSTANCE, instance);
        GalleryFragment fragment = new GalleryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        MyApplication.getAppComponent(context).inject(this);
    }
    int fragCount;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lay_gallery,container,false);
        unbinder = ButterKnife.bind(this,view);

        refreshLayout.setOnRefreshListener(() -> viewmodel.setRefresh(true));
        Bundle args = getArguments();
        if (args != null) {
            fragCount = args.getInt(ARGS_INSTANCE);
        }
        return view;
    }

    @Override
    public void onNewsSelected(String newsID) {
        if (mFragmentNavigation != null) {
            mFragmentNavigation.pushFragment(GalleryHeaderPageFragment.newInstance(fragCount + 1,newsID));
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        viewmodel = ViewModelProviders.of(getActivity(),viewModelFactory).get(GalleryFrViewModel.class);
        ((MainActivity)getActivity()).updateToolbarTitle("Fotoğraf Galerisi");


        recyclerView.setAdapter(new GalleryFragmentAdapter(viewmodel,this,this));
        recyclerView.setLayoutManager(new GridLayoutManager(context, 2));

        observeViewModel();

    }


    private void observeViewModel() {
        viewmodel.isLoading().observe(this, isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            if (isLoading){
                txt_errorMsg.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
            }
        });
        viewmodel.isError().observe(this, isError -> {
            if (isError){
                txt_errorMsg.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                txt_errorMsg.setText(R.string.api_error);
            }else{
                txt_errorMsg.setVisibility(View.GONE);
                txt_errorMsg.setText(null);
            }
        });
        viewmodel.getNews().observe(this, newsList -> {
            recyclerView.setVisibility(View.VISIBLE);

        });
        viewmodel.getScrollTop().observe(this, isScroll -> {
            if (recyclerView != null){
                //noinspection ConstantConditions
                if (!viewmodel.isLoading().getValue() && !viewmodel.isError().getValue()){
                    if (isScroll){
                        recyclerView.smoothScrollToPosition(0);
                        viewmodel.setScroll(false);
                    }
                }
            }
        });
        viewmodel.isRefreshing().observe(this, isRefresh -> refreshLayout.setRefreshing(isRefresh));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null){
            unbinder.unbind();
            unbinder = null;
        }
    }


}
