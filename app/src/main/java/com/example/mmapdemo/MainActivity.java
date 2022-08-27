package com.example.mmapdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.Toast;

import com.example.mmapdemo.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {



    private ActivityMainBinding binding;
    private MockSharedMemory mockSharedMemory;

    public native String stringFromJNI();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mockSharedMemory = new MockSharedMemory();
        checkPermission();
    }

    public boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 1);
        }
        return false;
    }

    public void write(View view) {
        mockSharedMemory.write();
    }

    public void jump(View view) {
        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
        startActivity(intent);
    }


    /*
    * 数据发送方连接到服务端
    * */
    public void jumpService(View view) {
        Intent intent = new Intent(MainActivity.this, MockIPCService.class);
        bindService(intent, new ServiceConnection() { // 1.一般通过这种方式来绑定Service
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                IMockIPC mockIPC = IMockIPC.Stub.asInterface(service);  // 获取IPC对象后，就可以对服务端进程发送消息了
                try {
                    Toast.makeText(MainActivity.this, "跨进程读取消息" + mockIPC.add(1,2), Toast.LENGTH_SHORT).show();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        }, Context.BIND_AUTO_CREATE);
    }
}