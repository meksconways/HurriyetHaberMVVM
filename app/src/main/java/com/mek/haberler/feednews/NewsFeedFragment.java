package com.mek.haberler.feednews;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mek.haberler.R;
import com.mek.haberler.base.BaseFragment;
import com.mek.haberler.home.MainActivity;
import com.mek.haberler.newsdetail.NewsDetailFragment;
import com.mek.haberler.newsdetail.NewsDetailViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
    private Unbinder unbinder;
    private NewsFeedViewModel viewmodel;
    private Context context;

    int fragCount;


    public static NewsFeedFragment newInstance(int instance) {
        Bundle args = new Bundle();
        args.putInt(ARGS_INSTANCE, instance);
        NewsFeedFragment fragment = new NewsFeedFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /*
    * News id yi taşımak için detailVM class ını 'ACTIVITY SCOPE' içinde tanımladık
    * */
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
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        viewmodel = ViewModelProviders.of(this).get(NewsFeedViewModel.class);
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
