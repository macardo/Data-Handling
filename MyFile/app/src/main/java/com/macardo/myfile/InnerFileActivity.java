package com.macardo.myfile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class InnerFileActivity extends AppCompatActivity {

    private EditText mEtNode;
    private Button mBtnSave,mBtnRead;
    private TextView mTvContent;
    private CheckBox mCheckBox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inner_file);
        initViews();
    }

    private void initViews() {
        mEtNode = findViewById(R.id.etNote);
        mBtnSave = findViewById(R.id.btnSave);
        mBtnRead = findViewById(R.id.btnRead);
        mTvContent = findViewById(R.id.tvContent);
        mCheckBox = findViewById(R.id.checkbox);

        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //保存etNote的内容到内部存储当中
                String content = mEtNode.getText().toString().trim();//trim()用于过滤前后空格
                boolean success = savetoInternalFile("mynote.txt",content,mCheckBox.isChecked());
                if (success){
                    Toast.makeText(InnerFileActivity.this,"保存到内部存储成功",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(InnerFileActivity.this,"保存到内部存储失败",Toast.LENGTH_SHORT).show();
                }
            }
        });
        mBtnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //读取内部存储的内容显示到tvContent中
                String content = readFromInternalFile("mynote.txt");
                mTvContent.setText(content);
            }
        });
    }

    private String readFromInternalFile(String fileName) {
        FileInputStream fis = null;
        BufferedReader reader = null;
        try {
            fis = openFileInput(fileName);
            reader =new BufferedReader(new InputStreamReader(fis));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null){
                builder.append(line);
            }
            return builder.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (fis != null){
                    fis.close();
                }
                if (reader != null){
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private boolean savetoInternalFile(String fileName, String content,boolean isAppend) {
        if (fileName == null||fileName.length() ==0 || content == null || content.length() ==0){
            return false;
        }

        FileOutputStream fos = null;
        try {
            if (isAppend) {
                fos = openFileOutput(fileName, MODE_APPEND);
            }else {
                fos = openFileOutput(fileName, MODE_PRIVATE);
            }
            fos.write(content.getBytes());
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (fos!=null){
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
