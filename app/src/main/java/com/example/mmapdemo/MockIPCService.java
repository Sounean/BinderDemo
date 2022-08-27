package com.example.mmapdemo;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import androidx.annotation.Nullable;

import java.security.Provider;

public class MockIPCService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new IMockIPC.Stub() {    //1。重写IPC
            @Override
            public int add(int b, int c) throws RemoteException {
                return b+c;
            }
        };
    }
}
