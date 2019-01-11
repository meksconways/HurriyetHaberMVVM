package com.mek.haberler.home;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mek.haberler.R;
import com.mek.haberler.base.BaseActivity;
import com.mek.haberler.base.BaseFragment;
import com.mek.haberler.feednews.NewsFeedFragment;
import com.mek.haberler.feednews.NewsFeedViewModel;
import com.mek.haberler.fragment.BookmarkFragment;
import com.mek.haberler.fragment.GalleryFragment;
import com.mek.haberler.fragment.VideoFragment;
import com.mek.haberler.fragment.WriterFragment;
import com.mek.haberler.util.FragNavController;
import com.mek.haberler.util.FragmentHistory;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements BaseFragment.FragmentNavigation,
        FragNavController.TransactionListener, FragNavController.RootFragmentListener {


    @BindView(R.id.screen_container)
    FrameLayout contentFrame;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindArray(R.array.tab_name)
    String[] TABS;

    @BindView(R.id.bottom_nav)
    BottomNavigationView bottomTabLayout;

    private FragNavController mNavController;
    private FragmentHistory fragmentHistory;
    MenuItem prevMenuItem;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {


            case android.R.id.home:


                onBackPressed();
                return true;
        }


        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        fragmentHistory = new FragmentHistory();

        setSupportActionBar(toolbar);



        mNavController = FragNavController.newBuilder(savedInstanceState,
                getSupportFragmentManager(),
                R.id.screen_container)
                .transactionListener(this)
                .rootFragmentListener(this, TABS.length)
                .build();
        switchTab(0);
        bottomTabLayout.setOnNavigationItemSelectedListener(item -> {

            switch (item.getItemId()){

                case R.id.act1:
                    switchTab(0);
                    fragmentHistory.push(0);

                    break;
                case R.id.act2:
                    switchTab(1);
                    fragmentHistory.push(1);
                    break;
                case R.id.act3:
                    switchTab(2);
                    fragmentHistory.push(2);
                    break;
                case R.id.act4:
                    switchTab(3);
                    fragmentHistory.push(3);
                    break;
                case R.id.act5:
                    switchTab(4);
                    fragmentHistory.push(4);
                    break;

            }

            return true;

        });

        bottomTabLayout.setOnNavigationItemReselectedListener(item -> {




            switch (item.getItemId()){

                case R.id.act1:
                    switchTab(0);
                    if (mNavController.isRootFragment()){
                        NewsFeedViewModel viewModel = ViewModelProviders.of(this).get(NewsFeedViewModel.class);
                        viewModel.setScroll(true);
                    }
                    mNavController.clearStack();
                    Log.d( "----onCreate: ","reselect");

                    break;
                case R.id.act2:
                    mNavController.clearStack();
                    switchTab(1);
                    break;
                case R.id.act3:
                    mNavController.clearStack();
                    switchTab(2);
                    break;
                case R.id.act4:
                    mNavController.clearStack();
                    switchTab(3);
                    break;
                case R.id.act5:
                    mNavController.clearStack();
                    switchTab(4);
                    break;

            }

        });


    }

    public void updateToolbarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }

    }

    private void switchTab(int position) {
        mNavController.switchTab(position);


    }

    @Override
    public Fragment getRootFragment(int index) {
        switch (index) {

            case FragNavController.TAB1:
                return new NewsFeedFragment();
            case FragNavController.TAB2:
                return new GalleryFragment();
            case FragNavController.TAB3:
                return new VideoFragment();
            case FragNavController.TAB4:
                return new WriterFragment();
            case FragNavController.TAB5:
                return new BookmarkFragment();


        }
        throw new IllegalStateException("Need to send an index that we know");
    }

    @Override
    public void onTabTransaction(Fragment fragment, int index) {
        // If we have a backstack, show the back button
        if (getSupportActionBar() != null && mNavController != null) {


            updateToolbar();

        }
    }

    private void updateToolbar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(!mNavController.isRootFragment());
            getSupportActionBar().setDisplayShowHomeEnabled(!mNavController.isRootFragment());
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        }

    }

    @Override
    public void onBackPressed() {

        if (!mNavController.isRootFragment()) {
            mNavController.popFragment();
        } else {

            if (fragmentHistory.isEmpty()) {
                super.onBackPressed();
            } else {


                if (fragmentHistory.getStackSize() > 1) {

                    int position = fragmentHistory.popPrevious();

                    switchTab(position);

                    updateTabSelection(position);

                } else {

                    switchTab(0);

                    updateTabSelection(0);

                    fragmentHistory.emptyStack();
                }
            }

        }
    }


    private void updateTabSelection(int currentTab){

        if (prevMenuItem != null) {
            prevMenuItem.setChecked(false);
        }
        else
        {
            bottomTabLayout.getMenu().getItem(0).setChecked(false);
        }

        bottomTabLayout.getMenu().getItem(currentTab).setChecked(true);
        prevMenuItem = bottomTabLayout.getMenu().getItem(currentTab);
    }

    @Override
    protected void onSaveInstanceState(@SuppressWarnings("NullableProblems") Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mNavController != null) {
            mNavController.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onFragmentTransaction(Fragment fragment, FragNavController.TransactionType transactionType) {
        //do fragmentty stuff. Maybe change title, I'm not going to tell you how to live your life
        // If we have a backstack, show the back button
        if (getSupportActionBar() != null && mNavController != null) {

            updateToolbar();

        }
    }


    @Override
    public void pushFragment(Fragment fragment) {
        if (mNavController != null) {
            mNavController.pushFragment(fragment);
        }
    }
}
