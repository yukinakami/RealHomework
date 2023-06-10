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

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity4 extends AppCompatActivity implements Runnable {

    private static final String TAG = "Net";

    TextView text1;

    TextView text2;

    Button btn1;

    Handler handler;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        text1 = findViewById(R.id.text1);
        text2 = findViewById(R.id.text2);
        btn1 = findViewById(R.id.btn1);
        Intent intent = getIntent();
        String up = intent.getStringExtra("up");
        Log.i(TAG, "up: " + up);
        text1.setText(up);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity4.this,MainActivity3.class);
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
        Intent intent = getIntent();
        String up = intent.getStringExtra("up");
        //个人信息
        String uid = up;
        String url_main = "https://api.bilibili.com/x/space/acc/info?mid=" + uid;
        String referer_main = "https://space.bilibili.com/" + uid;
        String content_main = getContent(url_main,referer_main);
        Log.i(TAG,"个人信息（关注数+观众数）：" + content_main);

        //up主关注数
        String followPattern = "\"following\":(.*?),";
        Pattern follow_pattern = Pattern.compile(followPattern);
        Matcher follow_matcher = follow_pattern.matcher(content_main);
        String follow = "";
        if (follow_matcher.find()) {
            follow = follow_matcher.group(1);
        }
        String follow_data = "关注数：" + follow;
        Log.i(TAG,"follow: "+follow);


        //个人信息（名称、性别、头像、描述、个人认证信息、大会员状态、直播间地址、预览图、标题、房间号、观看人数、直播间状态[开启/关闭]等）
        String url_number = "https://api.bilibili.com/x/space/acc/info?mid=" + uid;
        String referer_number = "https://space.bilibili.com/" + uid;
        String content_number = getContent(url_number,referer_number);
        Log.i(TAG,"个人信息（视频数+文章数+播放总量+获赞量）：" + content_number);

        //up主姓名
        String namePattern = "\"name\":\"(.*?)\"";
        Pattern name_pattern = Pattern.compile(namePattern);
        Matcher name_matcher = name_pattern.matcher(content_number);
        String name = "";
        if (name_matcher.find()) {
            name = name_matcher.group(1);
        }
        String name_data = "up主：" + name;
        Log.i(TAG,"name: "+name);

        //up主性别
        String sexPattern = "\"sex\":\"(.*?)\"";
        Pattern sex_pattern = Pattern.compile(sexPattern);
        Matcher sex_matcher = sex_pattern.matcher(content_number);
        String sex = "";
        if (sex_matcher.find()) {
            sex = sex_matcher.group(1);
        }
        String sex_data = "up主：" + sex;
        Log.i(TAG,"sex: "+sex);

        //up主头像网址
        String picPattern = "\"face\":\"(.*?)\"";
        Pattern pic_pattern = Pattern.compile(picPattern);
        Matcher pic_matcher = pic_pattern.matcher(content_number);
        String pic = "";
        if (pic_matcher.find()) {
            pic= pic_matcher.group(1);
        }
        String pic_data = "up主头像网址(可在浏览器中自行下载)：" + pic;
        Log.i(TAG,"pic: "+pic);

        //up主等级
        String levelPattern = "\"level\":(.*?),";
        Pattern level_pattern = Pattern.compile(levelPattern);
        Matcher level_matcher = level_pattern.matcher(content_number);
        String level = "";
        if (level_matcher.find()) {
            level= level_matcher.group(1);
        }
        String level_data = "up主等级" + level;
        Log.i(TAG,"level: "+level);

        //up主佩戴粉丝牌
            String medalPattern = "\"medal_name\":\"(.*?)\"";
            Pattern medal_pattern = Pattern.compile(medalPattern);
            Matcher medal_matcher = medal_pattern.matcher(content_number);
            String medal = "";
            if (medal_matcher.find()) {
                medal= medal_matcher.group(1);
            }
            String medal_name = "所佩戴粉丝牌名称：" + medal;
            Log.i(TAG,"medal: "+medal);

        //up主佩戴粉丝牌等级
        String medalPattern2 = "\"level\":(.*?),\"medal_name\"";
        Pattern medal_pattern2 = Pattern.compile(medalPattern2);
        Matcher medal_matcher2 = medal_pattern2.matcher(content_number);
        String medal2 = "";
        if (medal_matcher2.find()) {
            medal2= medal_matcher2.group(1);
        }
        String medal_name2 = "所佩戴粉丝牌等级：" + medal2;
        Log.i(TAG,"medal: "+medal2);


        //up主背景图
        String tpPattern = "\"top_photo\":\"(.*?)\"";
        Pattern tp_pattern = Pattern.compile(tpPattern);
        Matcher tp_matcher = tp_pattern.matcher(content_number);
        String tp = "";
        if (tp_matcher.find()) {
            tp = tp_matcher.group(1);
        }
        String tp_name = "背景图片网址(可自行下载)：" + tp;
        Log.i(TAG,"tp_name: "+tp);


        //up主生日
        String bdPattern = "\"birthday\":\"(.*?)\"";
        Pattern bd_pattern = Pattern.compile(bdPattern);
        Matcher bd_matcher = bd_pattern.matcher(content_number);
        String bd = "";
        if (bd_matcher.find()) {
            bd = bd_matcher.group(1);
        }
        String bd_name = "up主生日：" + bd;
        Log.i(TAG,"tp_name: "+bd);

        //up主学校
        String scPattern = "\"school\":\\{\"name\":\"(.*?)\"";
        Pattern sc_pattern = Pattern.compile(scPattern);
        Matcher sc_matcher = sc_pattern.matcher(content_number);
        String sc = "";
        if (sc_matcher.find()) {
            sc = sc_matcher.group(1);
        }
        String sc_name = "up主学校：" + sc;
        Log.i(TAG,"tp_name: "+sc);

        String up_data = name + "\n" + follow_data + "\n" + sex_data + "\n" + pic_data + "\n" + level_data + "\n" + medal_name + "\n"
                + medal_name2 + "\n" + tp_name + "\n" + bd_name + "\n" + sc_name;

        Message msg1 = handler.obtainMessage(5, up_data);
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

}