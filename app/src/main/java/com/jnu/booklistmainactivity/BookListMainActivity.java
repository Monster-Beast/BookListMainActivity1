package com.jnu.booklistmainactivity;

import static com.jnu.booklistmainactivity.BookListMainActivity.MENU_ID_ADD;
import static com.jnu.booklistmainactivity.BookListMainActivity.MENU_ID_DELETE;
import static com.jnu.booklistmainactivity.BookListMainActivity.MENU_ID_UPDATE;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.jnu.booklistmainactivity.data.Book;
import com.jnu.booklistmainactivity.data.DataSaver;

import java.util.ArrayList;
import java.util.List;

public class BookListMainActivity extends AppCompatActivity {
    public static final int MENU_ID_ADD=1;
    public static final int MENU_ID_UPDATE=2;
    public static final int MENU_ID_DELETE=3;
    private ArrayList<Book> books;//书本列表

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
            }
            return BooklistFragment.newInstance();
        }

        @Override
        public int getItemCount() {
            return 3;
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
                }
            }
        });
        tabLayoutMediator.attach();
    }
}