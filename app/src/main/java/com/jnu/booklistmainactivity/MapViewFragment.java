package com.jnu.booklistmainactivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.jnu.booklistmainactivity.data.ShopLoader;
import com.jnu.booklistmainactivity.data.SiteLocation;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapViewFragment extends Fragment {
    private MapView mapView;
    private Handler handler;//定义一个Handler成员对象
    private ShopLoader dataLoader;
    private String shopJsonData;
    private List<SiteLocation> siteLocationList;

    public MapViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MapViewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapViewFragment newInstance() {
        MapViewFragment fragment = new MapViewFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_map_view, container, false);
        mapView=rootView.findViewById(R.id.bmapView);
        //定义地图状态
//        //方法一
//        MapStatus.Builder builder = new MapStatus.Builder();//MapStatus.Builder对象
//        builder.zoom(18.0f);//比例尺
//        builder.target(new LatLng(22.333,113.444));
//        mapView.getMap().setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        //方法二
        LatLng cenpt = new LatLng(22.255925,113.541112);//中心经纬度:暨大珠区
        MapStatus mMapStatus = new MapStatus.Builder()
                .target(cenpt)//地图中心
                .zoom(18)//比例尺18
                .build();
        mapView.getMap().setMapStatus(MapStatusUpdateFactory.newMapStatus(mMapStatus));
        handler=new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:
                        //Toast.makeText(MapViewFragment.this.getContext(),msg.obj.toString()+msg.arg1,Toast.LENGTH_LONG).show();;
                        AddMarkerOnMap(siteLocationList);
                        break;
                    default:
                        break;
                }
            }
        };
        //新建一个线程加载网站数据(耗时大的事件不要放在主线程)
        new Thread(new Runnable() {
            @Override
            public void run() {
                dataLoader=new ShopLoader();
                shopJsonData= dataLoader.download("http://file.nidama.net/class/mobile_develop/data/bookstore2022.json");
                siteLocationList=dataLoader.parseJson(shopJsonData);
                //方法一:快速跳转到主线程更新界面
//                MapViewFragment.this.getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        AddMarkerOnMap(siteLocationList);
//                    }
//                });
                //方法二:子线程通过handler通知主线程
                Message message=new Message();
                message.what=1;
                message.arg1=666;
                message.obj="hello";
                handler.sendMessage(message);
            }
        }).start();
        //marker图标点击添加事件
        mapView.getMap().setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Toast.makeText(MapViewFragment.this.getContext(), "Marker clicked", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        return rootView;
    }

    private void AddMarkerOnMap(List<SiteLocation> siteLocationList) {
        BitmapDescriptor bitmap= BitmapDescriptorFactory.fromResource(R.drawable.school);
        for (SiteLocation shop: siteLocationList) {
            LatLng shopPoint = new LatLng(shop.getLatitude(),shop.getLongitude());
            OverlayOptions options = new MarkerOptions().position(shopPoint).icon(bitmap);
            //将maker添加到地图
            mapView.getMap().addOverlay(options);
            mapView.getMap().addOverlay(new TextOptions().bgColor(0xAAFFFF00)
                    .fontSize(32)
                    .fontColor(0xFFFF00FF).text(shop.getName()).position(shopPoint));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mapView.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mapView.onPause();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mapView.onDestroy();
    }
}