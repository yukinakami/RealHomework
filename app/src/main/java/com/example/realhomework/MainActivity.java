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
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity implements Runnable {
    private static final String TAG = "Net";
    Handler handler;
    TextView text2;

    Button button1;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button1 = findViewById(R.id.btn1);
        text2 = findViewById(R.id.text2);
        Intent intent = getIntent();
        String Bv = intent.getStringExtra("url");
        Log.i(TAG, "Bv: " + Bv);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,MainActivity2.class);
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
                    if(str=="")
                    {
                        String warn = "诶呀，该视频没有弹幕呢~";
                        text2.setText(warn);
                    }
                    else {
                        text2.setText(str);
                    }
                }
                super.handleMessage(msg1);
            }
        };

        Log.i(TAG, "onCreat: start Thread");
        Thread t = new Thread(this);
        t.start();
    }

    public  void run(){
        Log.i(TAG, "run:线程已启动");
        Intent intent = getIntent();
        String Bv = intent.getStringExtra("url");
        String Bv_url = "https://api.bilibili.com/x/player/pagelist?bvid=" + Bv;
        String Bv_referer = "https://www.bilibili.com/video/" + Bv;
        String content_Bv = getContent(Bv_url,Bv_referer);
        String cid = Cid(content_Bv);
        String url_word = "https://comment.bilibili.com/" + cid + ".xml";
        Document doc = null;
        try {
            doc = Jsoup.connect(url_word).get();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String doc_real = doc.toString();
        Log.i(TAG,"视频弹幕：" +doc);

        String danPattern = "\">(.*?)</d>";
        Pattern dan_pattern = Pattern.compile(danPattern);
        Matcher dan_matcher = dan_pattern.matcher(doc_real);
        StringBuilder danBuilder = new StringBuilder();
        while (dan_matcher.find()) {
            String dan = dan_matcher.group(1);
            danBuilder.append(dan).append("\n");
        }

        String allDan = danBuilder.toString();
        Log.i(TAG, "所有的dan信息：" + allDan);


        Message msg1 = handler.obtainMessage(5,allDan);
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

    }