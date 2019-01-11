package com.mek.haberler.newsdetail;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
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
import com.mek.haberler.home.MainActivity;
import com.mek.haberler.util.Util;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class NewsDetailFragment extends BaseFragment {


    int fragCount;
    private Context context;

    @BindView(R.id.lyt_detail_container)
    LinearLayout lyt_container;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.txt_errorMsg)
    TextView txt_errorMsg;
    @BindView(R.id.txt_news_title)
    TextView txt_newsTitle;
    @BindView(R.id.img_news_photo)
    ImageView img_newsPhoto;
    @BindView(R.id.txt_news_subtitle)
    TextView txt_newsSubtitle;
    @BindView(R.id.txt_news_owner)
    TextView txt_newsOwner;
    @BindView(R.id.txt_news_date)
    TextView txt_newsDate;
    @BindView(R.id.txt_news_desc)
    TextView txt_newsDesc;
    private Unbinder unbinder;
    private NewsDetailViewModel viewmodel;
    private String newsID;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public static NewsDetailFragment newInstance(int instance,String news_id) {
        Bundle args = new Bundle();
        args.putInt(ARGS_INSTANCE, instance);
        args.putString("news_id",news_id);
        NewsDetailFragment fragment = new NewsDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lay_news_detail,container,false);
        unbinder = ButterKnife.bind(this,view);
        Bundle args = getArguments();
        if (args != null) {
            fragCount = args.getInt(ARGS_INSTANCE);
            newsID = args.getString("news_id");
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewmodel = ViewModelProviders.of(this).get(NewsDetailViewModel.class);
        if (newsID != null){
            viewmodel.setSelectedNews(newsID);
        }
        observeViewModel();
        //noinspection ConstantConditions
        ((MainActivity)getActivity()).updateToolbarTitle("Haber Detay");
    }



    private void observeViewModel() {

        viewmodel.isLoading().observe(this, isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            if (isLoading){
                txt_errorMsg.setVisibility(View.GONE);
                lyt_container.setVisibility(View.GONE);
            }
        });
        viewmodel.isError().observe(this, isError -> {
            if (isError){
                txt_errorMsg.setVisibility(View.VISIBLE);
                lyt_container.setVisibility(View.GONE);
                txt_errorMsg.setText(R.string.api_error);
            }else{
                txt_errorMsg.setVisibility(View.GONE);
                txt_errorMsg.setText(null);
            }
        });
        viewmodel.getDetail().observe(this, detail -> {
            txt_newsDate.setText(Util.getDate(detail.createdDate));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                txt_newsDesc.setText(Html.fromHtml(detail.description, Html.FROM_HTML_MODE_COMPACT));
            } else {
                txt_newsDesc.setText(Html.fromHtml(detail.description));
            }
            txt_newsOwner.setText(detail.editor);
            txt_newsTitle.setText(detail.title);
            txt_newsSubtitle.setText(detail.subTitle);
            lyt_container.setVisibility(View.VISIBLE);
            if (detail.files.size() > 0){
                if (detail.files.get(0).fileUrl != null){
                    Glide.with(context)
                            .load(detail.files.get(0).fileUrl)
                            .apply(new RequestOptions().override(800, 450))
                            .into(img_newsPhoto);
                }
            }
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
