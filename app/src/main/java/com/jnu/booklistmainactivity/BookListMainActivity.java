package com.jnu.booklistmainactivity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class BookListMainActivity extends AppCompatActivity {
    //Fragement适配器,PageViewFragmentAdapater类
    public class PageViewFragmentAdapater extends FragmentStateAdapter{
        public PageViewFragmentAdapater(@NonNull FragmentManager fragmentManager,@NonNull Lifecycle lifecycle){
            super(fragmentManager,lifecycle);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position)
            {
                case 0:
                    return BooklistFragment.newInstance();
                case 1:
                    return WebViewFragment.newInstance();
                case 2:
                    return MapViewFragment.newInstance();
                case 3:
                    return GameFragment.newInstance();
            }
            return BooklistFragment.newInstance();
        }

        @Override
        public int getItemCount() {
            return 4;
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager2 viewPager2Main=findViewById(R.id.viewpage2_main);
        viewPager2Main.setAdapter(new PageViewFragmentAdapater(getSupportFragmentManager(),getLifecycle()));

        TabLayout tabLayout=findViewById(R.id.tablayout_header);
        TabLayoutMediator tabLayoutMediator=new TabLayoutMediator(tabLayout, viewPager2Main, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position){
                    case 0:
                        tab.setText("图书");
                        break;
                    case 1:
                        tab.setText("新闻");
                        break;
                    case 2:
                        tab.setText("地图");
                        break;
                    case 3:
                        tab.setText("游戏");
                        break;
                }
            }
        });
        tabLayoutMediator.attach();
    }
}