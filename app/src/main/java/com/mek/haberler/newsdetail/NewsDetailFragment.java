package com.mek.haberler.newsdetail;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mek.haberler.R;
import com.mek.haberler.base.BaseFragment;
import com.mek.haberler.base.MyApplication;
import com.mek.haberler.home.MainActivity;
import com.mek.haberler.readlaterpage.ReadLaterFragmentViewModel;
import com.mek.haberler.util.Util;
import com.mek.haberler.viewmodel.ViewModelFactory;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
    @BindView(R.id.scrollView)
    ScrollView scrollView;

    private Unbinder unbinder;
    private NewsDetailViewModel viewmodel;
    private String newsID;

    @Inject
    ViewModelFactory viewModelFactory;
    private ReadLaterFragmentViewModel _viewmodel;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        MyApplication.getAppComponent(context).inject(this);
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
        setHasOptionsMenu(true);

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
        viewmodel = ViewModelProviders.of(this,viewModelFactory).get(NewsDetailViewModel.class);
        _viewmodel = ViewModelProviders.of(getActivity(),viewModelFactory).get(ReadLaterFragmentViewModel.class);
        //noinspection ConstantConditions
        scrollView.setScrollY(viewmodel.getScrollY().getValue());
        if (newsID != null){
            viewmodel.setSelectedNews(newsID);
        }
        observeViewModel();

        //noinspection ConstantConditions
        ((MainActivity)getActivity()).updateToolbarTitle("Haber Detay");
    }


    private Menu menu;
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        this.menu = menu;
        inflater.inflate(R.menu.menu_news_detail, menu);
//        hideOption(R.id.removeNews);
//        hideOption(R.id.addNews);



        super.onCreateOptionsMenu(menu,inflater);


    }


    private void hideOption(int id)
    {
        try {
            MenuItem item = menu.findItem(id);
            if (item != null) {
                item.setVisible(false);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void showOption(int id)
    {
        try {
            MenuItem item = menu.findItem(id);
            if (item != null) {
                item.setVisible(true);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.removeNews:
                viewmodel.deleteFromRoom();
                hideOption(R.id.removeNews);
                showOption(R.id.addNews);

                return true;
            case R.id.addNews:
                viewmodel.saveToRoom();
                hideOption(R.id.addNews);
                showOption(R.id.removeNews);
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    private void observeViewModel() {
        viewmodel.getIsInDB().observe(this, isInDB -> {

            if (isInDB){
                showOption(R.id.removeNews);
                hideOption(R.id.addNews);
            }else {
                showOption(R.id.addNews);
                hideOption(R.id.removeNews);
            }

        });
        viewmodel.getNeedRefreshReadLater().observe(this, needRefresh -> {
            if (needRefresh){
                if (getActivity() != null){
                    ((MainActivity)getActivity()).showBadge();
                }
                _viewmodel.fetchData();
                viewmodel.setNeedRefreshReadLater(false);

            }
        });

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

           // webView.loadData(detail.description, "text/html; charset=UTF-8", null);
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
        viewmodel.setScrollYPos(scrollView.getScrollY());
        if (unbinder != null) {
            unbinder.unbind();
            unbinder = null;
        }
    }
}
