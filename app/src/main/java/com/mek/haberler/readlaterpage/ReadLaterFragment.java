package com.mek.haberler.readlaterpage;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mek.haberler.R;
import com.mek.haberler.base.BaseFragment;
import com.mek.haberler.base.MyApplication;
import com.mek.haberler.feednews.NewsSelectedListener;
import com.mek.haberler.home.MainActivity;
import com.mek.haberler.viewmodel.ViewModelFactory;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ReadLaterFragment extends BaseFragment implements NewsSelectedListener {


    private Unbinder unbinder;
    @Inject
    ViewModelFactory factory;
    private ReadLaterFragmentViewModel viewmodel;

    @BindView(R.id.rv_news)
    RecyclerView recyclerView;
    @BindView(R.id.lyt_empty)
    LinearLayout emptyView;
    private Context context;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        MyApplication.getAppComponent(context).inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lay_read_later,container,false);
        unbinder = ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewmodel = ViewModelProviders.of(this,factory).get(ReadLaterFragmentViewModel.class);
        //noinspection ConstantConditions
        ((MainActivity)getActivity()).updateToolbarTitle("Sonra Oku");
        recyclerView.setAdapter(new ReadLaterAdapter(viewmodel,this,this));
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        observeViewModel();
    }

    private void observeViewModel() {
        viewmodel.getNews().observe(this, newsDBS -> {
            if (newsDBS != null) {
                recyclerView.setVisibility(View.VISIBLE);
            }
        });
        viewmodel.getEmptyData().observe(this, isEmpty -> {
            if (isEmpty){
                emptyView.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }else{
                emptyView.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
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

    @Override
    public void onNewsSelected(String newsID) {

    }
}
