package com.mek.haberler.home;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mek.haberler.R;
import com.mek.haberler.feednews.NewsFeedFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.screen_container,new NewsFeedFragment())
                    .commit();
        }
    }
}
