package com.macardo.myfile;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.FileAlreadyExistsException;

public class OutterFileActivity extends AppCompatActivity {
    private EditText mEtNode;
    private Button mBtnSave,mBtnRead;
    private TextView mTvContent;
    private static final int REQ_CODE_FILE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outter_file);
        initViews();
        checkFilePermission();
    }

    private void initViews() {
        mEtNode = findViewById(R.id.etNote);
        mBtnSave = findViewById(R.id.btnSave);
        mBtnRead = findViewById(R.id.btnRead);
        mTvContent = findViewById(R.id.tvContent);
        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = mEtNode.getText().toString().trim();
                boolean success = saveToExternalFile("macardo", "mynote.txt", content);
                if (success){
                    Toast.makeText(OutterFileActivity.this,"保存到外部文件成功",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(OutterFileActivity.this,"保存到外部文件失败",Toast.LENGTH_SHORT).show();

                }
            }
        });
        mBtnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 String content = readFromExternalFile("macardo","mynote.txt");
                 mTvContent.setText(content);
            }
        });
    }

    private String readFromExternalFile(String dirName, String fileName) {
        if (!checkExternalState()){
            return null;
        }
        String externalPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        File dir = new File(externalPath + "/" + dirName + "/");
        if (!dir.exists()){
            return null;
        }
        FileInputStream fis =null;
        BufferedReader reader = null;
        try {
            fis = new FileInputStream(new File(dir,fileName));
            reader = new BufferedReader(new InputStreamReader(fis));
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
                if (fis !=null){
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

    //动态检查文件权限
    private void checkFilePermission() {
        //PERMISSION_GRANTED为获取到的权限
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            //未获取到文件的权限
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQ_CODE_FILE);//REQ_CODE_FILE = 1
        }
    }

    /**
     * 保存到外部文件中
     */
    private boolean saveToExternalFile(String dirName,String fileName,String content){
        if (!checkExternalState()){
            return false;
        }
        String externalPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        File dir = new File(externalPath +"/"+dirName+"/");
        if (!dir.exists()){
            dir.mkdir();
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(new File(dir,fileName));
            fos.write(content.getBytes());
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (fos !=null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    private boolean checkExternalState(){
        //MEDIA_MOUNTED：SD卡正常挂载，并且挂载点可读/写
        //Environment.getExternalStorageState：获取外部存储的状态
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQ_CODE_FILE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this,"成功获取文件权限",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this,"未获取文件权限",Toast.LENGTH_SHORT).show();
        }
    }
}
