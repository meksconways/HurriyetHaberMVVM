package com.mek.haberler.feednews;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mek.haberler.R;
import com.mek.haberler.base.BaseFragment;
import com.mek.haberler.base.MyApplication;
import com.mek.haberler.feednews.categorymodel.CategoryModel;
import com.mek.haberler.home.MainActivity;
import com.mek.haberler.newsdetail.NewsDetailFragment;
import com.mek.haberler.viewmodel.ViewModelFactory;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class NewsFeedFragment extends BaseFragment implements NewsSelectedListener {

    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.txt_errorMsg)
    TextView txt_errorMsg;
    @BindView(R.id.rv_news)
    RecyclerView recyclerView;
    @BindView(R.id.lyt_refresh)
    SwipeRefreshLayout refreshLayout;
    private Unbinder unbinder;
    private NewsFeedViewModel viewmodel;
    private Context context;

    int fragCount;



    @Inject
    ViewModelFactory viewModelFactory;

    public static NewsFeedFragment newInstance(int instance) {
        Bundle args = new Bundle();
        args.putInt(ARGS_INSTANCE, instance);
        NewsFeedFragment fragment = new NewsFeedFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onNewsSelected(String newsID) {
//        NewsDetailViewModel detailViewModel = ViewModelProviders.of(getActivity()).get(NewsDetailViewModel.class);
//        detailViewModel.setSelectedNews(newsID);
        if (mFragmentNavigation != null) {
            mFragmentNavigation.pushFragment(NewsDetailFragment.newInstance(fragCount + 1,newsID));
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lay_news_feed,container,false);
        unbinder = ButterKnife.bind(this,view);

        refreshLayout.setOnRefreshListener(() -> viewmodel.setRefresh(true));

        Bundle args = getArguments();
        if (args != null) {
            fragCount = args.getInt(ARGS_INSTANCE);
        }

        return view;

    }

    /*
    * Fragment içinde context bu fonksiyondan basit bir şekilde alınabilir
    * */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        MyApplication.getAppComponent(context).inject(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewmodel = ViewModelProviders.of(getActivity(),viewModelFactory).get(NewsFeedViewModel.class);
        //recyclerView.addItemDecoration(new DividerItemDecoration(context,DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(new NewsFeedAdapter(viewmodel,this,this));
        recyclerView.setLayoutManager(new LinearLayoutManager(context));



        //noinspection ConstantConditions
        ((MainActivity)getActivity()).updateToolbarTitle("Hürriyet Haber");

        observeViewModel();
    }

    /*
    * State lere göre görevler ne olacak bunu yazıyoruz
    * noinspection ConstantConditions yazmamızın sebebi bool veri tipinin asla null olmayacağıdır
    * */
    private void observeViewModel() {
        viewmodel.getFillCategories().observe(this, fillCat -> {

            if(fillCat){
                fillCategories();
                viewmodel.setCatModel(false);
            }
        });

        viewmodel.getError().observe(this, isError -> {
            //noinspection ConstantConditions
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
        viewmodel.loading().observe(this, isLoading -> {
            //noinspection ConstantConditions
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            if (isLoading){
                txt_errorMsg.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
            }
        });
        viewmodel.getScrollTop().observe(this, isScroll -> {
            if (recyclerView != null){
                //noinspection ConstantConditions
                if (!viewmodel.loading().getValue() && !viewmodel.getError().getValue()){
                    if (isScroll){
                        recyclerView.smoothScrollToPosition(0);
                        viewmodel.setScroll(false);
                    }
                }
            }


        });
        viewmodel.getRefresh().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isRefresh) {
                refreshLayout.setRefreshing(isRefresh);

            }
        });
    }

    private List<CategoryModel> categoryModel = new ArrayList<>();

    private void fillCategories() {
        int p1 = this.getResources().getIdentifier("cat_dunya", "drawable", context.getPackageName());
        int p2 = this.getResources().getIdentifier("cat_gundem", "drawable", context.getPackageName());
        int p3 = this.getResources().getIdentifier("cat_spor", "drawable", context.getPackageName());
        int p4 = this.getResources().getIdentifier("cat_ekonomi", "drawable", context.getPackageName());
        int p5 = this.getResources().getIdentifier("cat_avrupa", "drawable", context.getPackageName());
        int p6 = this.getResources().getIdentifier("cat_magazin", "drawable", context.getPackageName());
        int p7 = this.getResources().getIdentifier("cat_egitim", "drawable", context.getPackageName());

        categoryModel.add(new CategoryModel("Dünya",p1,"/dunya/"));
        categoryModel.add(new CategoryModel("Gündem",p2,"/gundem/"));
        categoryModel.add(new CategoryModel("Spor",p3,"/sporarena/"));
        categoryModel.add(new CategoryModel("Ekonomi",p4,"/ekonomi/"));
        categoryModel.add(new CategoryModel("Avrupa",p5,"/avrupa/"));
        categoryModel.add(new CategoryModel("Magazin",p6,"/magazin-haberleri/"));
        categoryModel.add(new CategoryModel("Eğitim",p7,"/egitim/"));

        viewmodel.setCat(categoryModel);

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
