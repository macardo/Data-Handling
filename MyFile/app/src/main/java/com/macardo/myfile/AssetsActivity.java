package com.macardo.myfile;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class AssetsActivity extends AppCompatActivity {

    private Button mBtnReadAssets;
    private TextView mTvContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
        mBtnReadAssets = findViewById(R.id.btnReadAssets);
        mTvContent = findViewById(R.id.tvAssetsContent);
        mBtnReadAssets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result = readAssetsContent("macardo.txt");
                mTvContent.setText(result);
            }
        });
    }

    /**
     * 读取Assets中的文件
     */
    @Nullable
    private String readAssetsContent(String fileName) {
        InputStream fis = null;
        BufferedReader reader = null;
        try {
            fis = this.getAssets().open(fileName);//this.getAssets()方法读取AssetsManager
            reader = new BufferedReader(new InputStreamReader(fis));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) !=null){
                builder.append(line);
            }
            return builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            //关闭操作
            try {
                if (fis!=null){
                    fis.close();
                }
                if (reader !=null){
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return null;
    }
}
