package com.jnu.booklistmainactivity;

import static com.jnu.booklistmainactivity.BookListMainActivity.MENU_ID_ADD;
import static com.jnu.booklistmainactivity.BookListMainActivity.MENU_ID_DELETE;
import static com.jnu.booklistmainactivity.BookListMainActivity.MENU_ID_UPDATE;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
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

import com.jnu.booklistmainactivity.data.Book;
import com.jnu.booklistmainactivity.data.DataSaver;

import java.util.ArrayList;
import java.util.List;

public class BookListMainActivity extends AppCompatActivity {
    public static final int MENU_ID_ADD=1;
    public static final int MENU_ID_UPDATE=2;
    public static final int MENU_ID_DELETE=3;
    RecyclerView recyclerView;
    MyAdapater myAdapater;
    ArrayList<Book> books=new ArrayList<>();

    //增加
    private ActivityResultLauncher<Intent> addDataLauncher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(null!=result){
                    Intent intent=result.getData();
                    if(result.getResultCode()==EditBookActivity.RESULT_CODE_SUCCESS) {
                        Bundle bundle=intent.getExtras();
                        String title=bundle.getString("title");
                        int position=bundle.getInt("position");
                        books.add(position,new Book(title,R.drawable.book_no_name));
                        DataSaver dataSaver=new DataSaver();
                        dataSaver.Save(this,books);
                        myAdapater.notifyItemInserted(position);
                    }
                }
            });
    //修改
    private ActivityResultLauncher<Intent> updateDataLauncher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(null!=result){
                    Intent intent=result.getData();
                    if(result.getResultCode()==EditBookActivity.RESULT_CODE_SUCCESS) {
                        Bundle bundle=intent.getExtras();
                        String title=bundle.getString("title");
                        int position=bundle.getInt("position");
                        books.get(position).setTitle(title);
                        DataSaver dataSaver=new DataSaver();
                        dataSaver.Save(this,books);
                    }
                }
            });

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //recyclerView引入
        recyclerView=findViewById(R.id.recycleview_main);
        //布局
        LinearLayoutManager layoutManager = new LinearLayoutManager(BookListMainActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        //构造一些数据
        if(books.isEmpty()) {
            books.add(new Book("软件项目管理案例教程（第4版）", R.drawable.book_2));
            books.add(new Book("信息安全数学基础（第2版）", R.drawable.book_1));
            books.add(new Book("创新工程实践", R.drawable.book_no_name));
        }
        //适配器
        myAdapater=new MyAdapater(books);
        recyclerView.setAdapter(myAdapater);
    }

    //点击"增加"或"删除"的相应函数
    @SuppressLint("ShowToast")
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case MENU_ID_ADD://增加
                Intent intent=new Intent(this,EditBookActivity.class);
                intent.putExtra("position",item.getOrder());
                addDataLauncher.launch(intent);
                //Toast.makeText(this,"add",Toast.LENGTH_LONG).show();//跳出提示
                myAdapater.notifyDataSetChanged();
                break;
            case MENU_ID_UPDATE://修改
                Intent intentUpdate=new Intent(this,EditBookActivity.class);
                intentUpdate.putExtra("position",item.getOrder());
                intentUpdate.putExtra("title",books.get(item.getOrder()).getTitle());
                updateDataLauncher.launch(intentUpdate);
                //Toast.makeText(this,"update",Toast.LENGTH_LONG).show();//跳出提示
                myAdapater.notifyDataSetChanged();
                break;
            case MENU_ID_DELETE://删除
                books.remove(item.getOrder());
                DataSaver dataSaver=new DataSaver();
                dataSaver.Save(BookListMainActivity.this,books);
                myAdapater.notifyItemRemoved(item.getOrder());
                break;
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }
}





//适配器MyAdapater类
class MyAdapater extends RecyclerView.Adapter<MyAdapater.ViewHolder> {
    private List<Book> data;

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
    public MyAdapater(List<Book> dataSet){
        data= dataSet;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_main,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.getTextViewTitle().setText(data.get(position).getTitle());
        viewHolder.getImageViewCover().setImageResource(data.get(position).getCoverResourceId());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    //重载函数使得长按无效区无效,不弹出窗口
    @Override
    public void onViewRecycled(@NonNull ViewHolder viewHolder) {
        viewHolder.itemView.setOnLongClickListener(null);
        super.onViewRecycled(viewHolder);
    }
}
