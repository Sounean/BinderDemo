package com.example.mmapdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class SecondActivity extends AppCompatActivity {


    // 这里和MainAct中的mockBinder肯定就不是同一个了，这里是为了验证不同进程能否通过同一个虚拟地址访问到同一个物理地址，
    // 如果可以访问到，其实就是实现了不同进程间的通信
    private MockSharedMemory mockSharedMemory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        mockSharedMemory = new MockSharedMemory();
    }

    public void read(View view) {
        mockSharedMemory.read();
        Toast.makeText(this, "跨进程读取消息" + mockSharedMemory.read(), Toast.LENGTH_SHORT).show();
    }
}