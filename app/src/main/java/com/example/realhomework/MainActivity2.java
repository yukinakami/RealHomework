package com.example.realhomework;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity2 extends AppCompatActivity implements Runnable {
    private static final String TAG = "Net";
    Handler handler;
    Button button1;
    TextView text1;
    TextView text2;

    Button button2;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        button1 = findViewById(R.id.btn1);
        button2 = findViewById(R.id.btn2);
        text1 = findViewById(R.id.text1);
        text2 = findViewById(R.id.text2);
        Intent intent = getIntent();
        String Bv = intent.getStringExtra("url");
        text1.setText(Bv);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity2.this,SearchActivity.class);
                startActivity(intent);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity2.this,MainActivity.class);
                String Bv = text1.getText().toString();
                intent.putExtra("url",Bv);
                startActivity(intent);
            }
        });


        handler = new Handler(Looper.myLooper()) {
            public void handleMessage(@NonNull Message msg1) {
                //处理返回
                if (msg1.what == 5) {
                    String str = (String) msg1.obj;
                    Log.i(TAG, "handleMessage: str=" + str);
                    text2.setText(str);
                }
                super.handleMessage(msg1);
            }
        };


        Log.i(TAG, "onCreat: start Thread");
        Thread t = new Thread(this);
        t.start();

    }

    @Override
    public void run() {
        Log.i(TAG, "run:线程已启动");

        //爬取视频标题
        String titlePattern = "\"title\":\"(.*?)\"";
        String title_hole = "视频标题：" + DATA(titlePattern);

        //爬取视频作者
        String namePattern = "\"name\":\"(.*?)\"";
        String name_real = "视频作者：" + DATA(namePattern);

        //爬取视频分区
        String tnamePattern = "\"tname\":\"(.*?)\"";
        String tname_real = "视频分区：" + DATA(tnamePattern);

        //爬取视频简介
        String textPattern = "\"raw_text\":(.*?),";
        String text_vedio = "视频简介：" + DATA(textPattern);

        //爬取点赞
        String likePattern = "\"like\":(.*?),";
        String like_vedio = "视频点赞：" + DATA(likePattern);
        int int_like_data = INTDATA(likePattern);

        //爬取投币
        String coinPattern = "\"coin\":(.*?)\\},";
        String coin_vedio = "视频投币：" + DATA(coinPattern);
        int int_coin_data = INTDATA(coinPattern);

        //爬取分享
        String sharePattern = "\"share\":(.*?),";
        String share_vedio = "视频分享：" + DATA(sharePattern);
        int int_share_data = INTDATA(sharePattern);

        //爬取收藏
        String favoritePattern = "\"favorite\":(.*?),";
        String favorite_vedio = "视频收藏：" + DATA(favoritePattern);
        int int_favorite_data = INTDATA(favoritePattern);

        //爬取播放量
        String viewPattern = "\"view\":(.*?),";
        String view_vedio = "视频播放：" + DATA(viewPattern);
        int int_view_data = INTDATA(viewPattern);

        //爬取评论数
        String coPattern = "\"reply\":(.*?),";
        String co_vedio = "视频评论：" + DATA(coPattern);
        int int_co_data = INTDATA(coPattern);

        //爬取弹幕数
        String danPattern = "\"danmaku\":(.*?),";
        String dan_vedio = "视频弹幕：" + DATA(danPattern);
        int int_dan_data = INTDATA(danPattern);

        //爬取点踩数,赋分时为负值
        String disPattern = "\"dislike\":(.*?),";
        String dis_vedio = "视频点踩：" + DATA(disPattern);
        int int_dis_data = INTDATA(disPattern);



        double[][] a = new double[][] { {1,1,0.5,0.3,3.1,1.7,0.7,1},
                {1.1,1,0.4,0.2,2.7,1.2,0.6,0.9},
                {3.2,3.8,1,0.7,3.9,2.1,0.6,0.9}, {3.4,3.7,1.8,1,4.8,3.2,2.9,3},
                {0.6,0.7,0.4,0.1,1,0.8,0.9,0.95},
                {0.9,1.1,0.8,0.4,2.1,1,1,1.7},
                {1.1,0.9,1.1,0.3,1.7,1.3,1,2.3},
                {0.9,1.2,0.6,0.5,1.5,1.2,0.8,1}
        };
        int N = a[0].length;
        double[] weight = new double[N];
        AHPComputeWeight instance = AHPComputeWeight.getInstance();
        instance.weight(a, weight, N);
        double total_double = 0;
        double[] weights = (double[]) weight;
        double weight_like = weights[0];
        double weight_coin = weights[1];
        double weight_share = weights[2];
        double weight_favorite = weights[3];
        double weight_view = weights[4];
        double weight_co = weights[5];
        double weight_dan = weights[6];
        double weight_dis = weights[7];
        total_double = weight_like * int_like_data + weight_coin * int_coin_data + weight_share * int_share_data
                + weight_favorite * int_favorite_data + weight_view * int_view_data + weight_co * int_co_data
                + weight_dan * int_dan_data - weight_dis * int_dis_data;
        String total = String.valueOf(total_double);
        String total_data = "视频综合得分" + total;
        String information_data = title_hole + "\n" + name_real + "\n" + tname_real + "\n" + text_vedio + "\n" + like_vedio + "\n" +
                coin_vedio + "\n" + share_vedio + "\n" + favorite_vedio + "\n" + view_vedio + "\n" + co_vedio + "\n" + dan_vedio + "\n" + dis_vedio + "\n" + total_data;

        //发送消息
        Message msg1 = handler.obtainMessage(5, information_data);
        handler.sendMessage(msg1);

    }

    public String getContent(String url, String referer) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36 Edg/114.0.1823.37")
                .addHeader("Referer", referer)
                .addHeader("Accept-Charset", "UTF-8")
                .build();

        String result = null;
        try {
            Call call = client.newCall(request);
            Response rep = call.execute();
            int code = rep.code();
            //状态码为200即为成功
            Log.i(TAG, "状态码为：" + code);
            result = rep.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String Cid(String content) {
        // 将JSON字符串解析为JSONObject对象
        JSONObject jsonObject = JSONObject.parseObject(content);
        // 获取"data"字段对应的JSONArray
        JSONArray dataArray = jsonObject.getJSONArray("data");
        // 获取第一个元素的JSONObject
        JSONObject dataObject = dataArray.getJSONObject(0);
        //JSONObject dataObject2 = dataArray.getJSONObject(12);
        // 提取"cid"字段的值
        //String Frame_int = dataObject2.getString("first_frame");
        String cid = dataObject.getString("cid");
        return cid;
    }

    public static String Information(String content_Bv_number) {
        // 将JSON字符串解析为JSONObject对象
        JSONObject jsonObject = JSONObject.parseObject(content_Bv_number);

        // 获取"data"字段对应的JSON对象
        JSONObject dataObject = jsonObject.getJSONObject("data");
        // 将"data"字段的值转换为字符串
        String data = dataObject.toString();
        return data;
    }

    public String data(String Bv){
        String Bv_number_url = "https://api.bilibili.com/x/web-interface/view?bvid=" + Bv;
        String Bv_number_referer = "https://www.bilibili.com/video/" + Bv;
        String content_Bv_number = getContent(Bv_number_url, Bv_number_referer);
        Log.i(TAG, "视频数据：" + content_Bv_number);
        String data = Information(content_Bv_number);
        return data;
    }

    public String DATA(String textPattern){
        Intent intent = getIntent();
        String Bv = intent.getStringExtra("url");
        String data = data(Bv);
        Pattern text_pattern = Pattern.compile(textPattern);
        Matcher text_matcher = text_pattern.matcher(data);
        String text = "";
        if (text_matcher.find()) {
            text = text_matcher.group(1);
        }
        String data_text = text;
        Log.i(TAG,"data_text：" + text);
        return text;
    }
    public int INTDATA(String textPattern){
        String text = DATA(textPattern);
        int int_text_data = Integer.valueOf(text);
        return int_text_data;
    }


}