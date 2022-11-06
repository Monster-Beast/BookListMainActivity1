package com.jnu.booklistmainactivity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jnu.booklistmainactivity.data.Book;
import com.jnu.booklistmainactivity.data.DataSaver;

import java.util.ArrayList;

public class BookListMainActivity extends AppCompatActivity {
    public static final int MENU_ID_ADD=1;
    public static final int MENU_ID_UPDATE=2;
    public static final int MENU_ID_DELETE=3;
    private ArrayList<Book> books;//书本列表
    private MyAdapater myAdapater;
   //增加
    private final ActivityResultLauncher<Intent> addDataLauncher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(null!=result){
                    Intent intent=result.getData();
                    if(result.getResultCode()== EditBookActivity.RESULT_CODE_SUCCESS)
                    {
                        Bundle bundle=intent.getExtras();
                        String title=bundle.getString("title");
                        int position=bundle.getInt("position");
                        books.add(position,new Book(title,R.drawable.book_no_name));
                        new DataSaver().Save(this,books);
                        myAdapater.notifyItemInserted(position);
                    }
                }
            });
    //修改
    private final ActivityResultLauncher<Intent> updateDataLauncher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(null!=result){
                    Intent intent=result.getData();
                    if(result.getResultCode()==EditBookActivity.RESULT_CODE_SUCCESS) {
                        Bundle bundle=intent.getExtras();
                        String title=bundle.getString("title");
                        int position=bundle.getInt("position");
                        books.get(position).setTitle(title);
                        new DataSaver().Save(this,books);
                        myAdapater.notifyItemChanged(position);
                    }
                }
            });

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        //setContentView之前调用，否则报错
        setContentView(R.layout.activity_main);
        //工具栏
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        // 设置ToolBar标题
         toolbar.setTitle("ToolBar");
        // 设置ToolBar副标题
        toolbar.setSubtitle("this is toolbar");
        // 设置navigation button
        //toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.icon_lines,null));
        // 设置Logo图标
        //toolbar.setLogo(getResources().getDrawable(R.drawable.ic_launcher_background,null));
        // 设置溢出菜单的图标
        //toolbar.setOverflowIcon(getResources().getDrawable(R.drawable.icon_topright,null));
        // 设置Menu
        setSupportActionBar(toolbar);
        //侧滑抽屉
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_main);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, 0, 0);
        //drawer.setDrawerListener(toggle);
        toggle.syncState();
        //recyclerView
        RecyclerView recyclerView=findViewById(R.id.recyclerview_main);
        //布局
        LinearLayoutManager layoutManager = new LinearLayoutManager(BookListMainActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        //数据数组初始化
        books=new ArrayList<>();
        //数据载入
        DataSaver dataSaver=new DataSaver();
        books=dataSaver.Load(this);
        //构造一些数据
        if(books.size()==0) {
            books.add(new Book("软件项目管理案例教程（第4版）", R.drawable.book_2));
            books.add(new Book("信息安全数学基础（第2版）", R.drawable.book_1));
            books.add(new Book("创新工程实践", R.drawable.book_no_name));
        }
        myAdapater=new MyAdapater(books);
        recyclerView.setAdapter(myAdapater);
        //悬浮加法按钮
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentAdd=new Intent(BookListMainActivity.this,EditBookActivity.class);
                intentAdd.putExtra("position",books.size());
                addDataLauncher.launch(intentAdd);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.all_bar:
                //选择书架
                Toast.makeText(BookListMainActivity.this,"书架",Toast.LENGTH_SHORT).show();
                break;
            case R.id.unfolder_bar:
                Toast.makeText(BookListMainActivity.this,"展开",Toast.LENGTH_SHORT).show();
                break;
            case R.id.search_bar:
                Toast.makeText(BookListMainActivity.this,"搜索",Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(BookListMainActivity.this,"其他",Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_main);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //点击"增加"或"删除"的相应函数
    @SuppressLint("ShowToast")
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case MENU_ID_ADD://增加(在相同位置增加一本一样的书)
                books.add(item.getOrder(),new Book(books.get(item.getOrder()).getTitle(),books.get(item.getOrder()).getCoverResourceId()));
                new DataSaver().Save(BookListMainActivity.this,books);
                myAdapater.notifyItemInserted(item.getOrder());
                break;
            case MENU_ID_UPDATE://修改
                Intent intentUpdate=new Intent(this,EditBookActivity.class);
                intentUpdate.putExtra("position",item.getOrder());
                intentUpdate.putExtra("title",books.get(item.getOrder()).getTitle());
                updateDataLauncher.launch(intentUpdate);
                break;
            case MENU_ID_DELETE://删除
                AlertDialog alertDialog=new AlertDialog.Builder(this)
                        .setTitle(R.string.string_confirmation)
                        .setMessage(R.string.string_sure_to_delete)
                        .setNegativeButton(R.string.string_no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).setPositiveButton(R.string.string_yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                books.remove(item.getOrder());
                                new DataSaver().Save(BookListMainActivity.this,books);
                                myAdapater.notifyItemRemoved(item.getOrder());
                            }
                        }).create();
                alertDialog.show();
                break;
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }

    //适配器MyAdapater类
    public class MyAdapater extends RecyclerView.Adapter<MyAdapater.ViewHolder> {
        private ArrayList<Book> localDataset;

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{
            private final TextView textViewTitle;
            private final ImageView imageViewCover;

            public ViewHolder(View view){
                super(view);
                imageViewCover=view.findViewById(R.id.imageview_cover);
                textViewTitle=view.findViewById(R.id.textview_title);
                //长按事件监听者
                view.setOnCreateContextMenuListener(this);
            }

            public TextView getTextViewTitle(){return textViewTitle;}
            public ImageView getImageViewCover(){return imageViewCover;}

            //创建上下文菜单
            @Override
            public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                contextMenu.add(0,MENU_ID_ADD,getAdapterPosition(),"增加");
                contextMenu.add(0,MENU_ID_UPDATE,getAdapterPosition(),"修改");
                contextMenu.add(0,MENU_ID_DELETE,getAdapterPosition(),"删除");
            }
        }

        //构造函数
        public MyAdapater(ArrayList<Book> dataSet){
            localDataset=dataSet;
        }
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_main,viewGroup,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int position) {
            viewHolder.getTextViewTitle().setText(localDataset.get(position).getTitle());
            viewHolder.getImageViewCover().setImageResource(localDataset.get(position).getCoverResourceId());
        }

        @Override
        public int getItemCount() {
            return localDataset.size();
        }

        //重载函数使得长按无效区无效,不弹出窗口
        @Override
        public void onViewRecycled(@NonNull ViewHolder viewHolder) {
            viewHolder.itemView.setOnLongClickListener(null);
            super.onViewRecycled(viewHolder);
        }
    }
}
