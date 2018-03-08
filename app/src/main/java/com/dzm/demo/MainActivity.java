package com.dzm.demo;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.dzm.banner.DzmBannerPage;
import com.dzm.banner.callback.OnItemClickCallback;
import com.dzm.banner.holder.DzmViewHolder;
import com.dzm.banner.holder.Holder;
import com.dzm.banner.transformer.DzmTransformer1;
import com.dzm.demo.http.TestIntercept;
import com.dzm.http.http.HttpCallback;
import com.dzm.http.http.HttpInterface;
import com.dzm.http.http.HttpSir;
import com.dzm.http.impl.HttpServerImpl;
import com.dzm.http.server.HttpServer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    List<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = new ArrayList<>();

        final DzmBannerPage<String> dzmBannerPage = findViewById(R.id.dzm_banner);
        dzmBannerPage.setBuild(new DzmBannerPage.Build<String>().setViewHolder(new DzmViewHolder<String>() {
            @Override
            public Holder<String> createHolder() {
                return new MyHolder();
            }

            @Override
            public List<String> getDatas() {
                return list;
            }
        }).setOnItemClickCallback(new OnItemClickCallback<String>() {
            @Override
            public void onItemClick(Context context, int position, String s) {
                Toast.makeText(context,s,Toast.LENGTH_SHORT).show();
            }
        }).setTransformer(new DzmTransformer1()).setNeadIndicator(false));


        MyLibServicesLoader.getService(HttpServer.class).addDefultIntercepte(new TestIntercept());

        findViewById(R.id.dzm_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MyLibServicesLoader.getService(HttpServer.class).http(v.getContext()).build(new HttpInterface.Build("test").addData("flag","1").setCallback(new HttpCallback() {
                    @Override
                    public void onStart(String tag, Map<String, Object> other) {

                    }

                    @Override
                    public void onNext(Object o, String tag, Map<String, Object> other) {
                        Log.d("http",o.toString());

                    }

                    @Override
                    public void onCompleted(String tag, Map<String, Object> other) {

                    }

                    @Override
                    public void onError(String tag, Map<String, Object> other, String msg) {
                        Log.d("http",msg);
                    }
                }));

//                list.clear();
//                list.add("aaaaaaaa");
//                list.add("bbbbbbbb");
//                list.add("cccccccc");
//                list.add("eeeeeeee");
//                dzmBannerPage.notifyDataSetChanged();
            }
        });
    }

    class MyHolder implements Holder<String> {

        ImageView imageView;

        @Override
        public View createView(Context context) {
            if(null == imageView){
                imageView = new ImageView(context);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            }
            return imageView;
        }

        @Override
        public void updateUi(Context context, int position, String s) {
            switch (position){
                case 0:
                    imageView.setImageResource(R.mipmap.a);
                    break;
                case 1:
                    imageView.setImageResource(R.mipmap.b);
                    break;
                case 2:
                    imageView.setImageResource(R.mipmap.c);
                    break;
                case 3:
                    imageView.setImageResource(R.mipmap.d);
                    break;
            }
            Log.d("MainActivity",s);
        }
    }
}
