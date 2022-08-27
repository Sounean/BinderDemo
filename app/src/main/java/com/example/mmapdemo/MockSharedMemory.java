package com.example.mmapdemo;

/**
 * @Author : Sounean
 * @Time : On 2022-08-27 16:09
 * @Description : 仿写binder:功能读、写
 * @Warn :
 */
public class MockSharedMemory {

    static {
        System.loadLibrary("mmapdemo");   // 加载cpp文件夹下的native-lib.cpp文件（因为读写都是）
    }
    public native void write(); // 都是用的引入的native-lib.cpp的代码
    public native String read();
}
