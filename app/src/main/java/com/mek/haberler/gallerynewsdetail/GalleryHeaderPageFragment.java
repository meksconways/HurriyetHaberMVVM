package com.mek.haberler.gallerynewsdetail;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mek.haberler.R;
import com.mek.haberler.base.BaseFragment;
import com.mek.haberler.base.MyApplication;
import com.mek.haberler.gallerynewsdetail.model.Files;
import com.mek.haberler.home.MainActivity;
import com.mek.haberler.viewmodel.ViewModelFactory;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class GalleryHeaderPageFragment extends BaseFragment implements GalleryClickListener {


    private Unbinder unbinder;

    @BindView(R.id.lyt_container)
    LinearLayout lyt_container;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.txt_errorMsg)
    TextView txt_error;
    @BindView(R.id.img_first)
    ImageView img_first;
    @BindView(R.id.img_second)
    ImageView img_second;
    @BindView(R.id.img_third)
    ImageView img_third;
    @BindView(R.id.txt_news_title)
    TextView txt_newsTitle;
    @BindView(R.id.txt_news_owner)
    TextView txt_owner;
    @BindView(R.id.txt_desc)
    TextView txt_desc;
    private Context context;
    @BindView(R.id.lyt_imgContainer)
    LinearLayout linearLayout;



    @Inject
    ViewModelFactory viewModelFactory;
    private GalleryNewsDetailViewModel _viewmodel;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        MyApplication.getAppComponent(context).inject(this);
    }

    int fragCount;
    private GalleryHeaderPageViewModel viewmodel;
    private String newsID;

    public static GalleryHeaderPageFragment newInstance(int instance,String news_id) {

        Bundle args = new Bundle();
        args.putInt(ARGS_INSTANCE, instance);
        args.putString("news_id",news_id);
        GalleryHeaderPageFragment fragment = new GalleryHeaderPageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.lay_gallery_detail_header,container,false);
        unbinder = ButterKnife.bind(this,v);
        Bundle args = getArguments();
        if (args != null) {
            fragCount = args.getInt(ARGS_INSTANCE);
            newsID = args.getString("news_id");
        }

        linearLayout.setOnClickListener(v1 -> {
            if (viewmodel.getNews().getValue() != null){
                onGalleryClick(viewmodel.getNews().getValue().files);
            }

        });

        return v;
    }

    @Override
    public void onGalleryClick(List<Files> files) {
        _viewmodel.setFiles(files);
        if (mFragmentNavigation != null) {
            mFragmentNavigation.pushFragment(GalleryNewsDetailFragment.newInstance(fragCount + 1));
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewmodel = ViewModelProviders.of(this,viewModelFactory).get(GalleryHeaderPageViewModel.class);
        _viewmodel = ViewModelProviders.of(getActivity(),viewModelFactory).get(GalleryNewsDetailViewModel.class);
        try {
            Log.d( "------onViewCreated: ",newsID);
        }catch (Exception e){
            e.printStackTrace();
        }

        if (newsID != null){
            viewmodel.setSelectedNews(newsID);
        }
        observeViewModel();
        //noinspection ConstantConditions
        ((MainActivity)getActivity()).updateToolbarTitle("Galeri Haber Detay");
    }

    private void observeViewModel() {
        viewmodel.getError().observe(this, isError -> {
            if (isError){
                txt_error.setVisibility(View.VISIBLE);
                lyt_container.setVisibility(View.GONE);
                txt_error.setText(R.string.api_error);
            }else{
                txt_error.setVisibility(View.GONE);
                txt_error.setText(null);
            }
        });
        viewmodel.getLoading().observe(this, isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            lyt_container.setVisibility(isLoading ? View.GONE : View.VISIBLE);
            if (isLoading){
                txt_error.setVisibility(View.GONE);
                lyt_container.setVisibility(View.GONE);
            }
        });
        viewmodel.getNews().observe(this, news -> {



                txt_newsTitle.setText(news.title);
                txt_owner.setText(news.editor);
                txt_desc.setText(news.description);

                Glide.with(context)
                        .load(news.files.get(0).fileUrl)
                        .apply(new RequestOptions().override(800, 450))
                        .into(img_first);
                Glide.with(context)
                        .load(news.files.get(1).fileUrl)
                        .apply(new RequestOptions().override(800, 450))
                        .into(img_second);
                Glide.with(context)
                        .load(news.files.get(2).fileUrl)
                        .apply(new RequestOptions().override(800, 450))
                        .into(img_third);


        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
            unbinder = null;
        }
    }


}
