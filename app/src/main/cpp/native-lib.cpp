#include <jni.h>
#include <string>
#include <sys/mman.h>   // 1. 引入该文件因为该文件内有mmap方法

#include <jni.h>
#include <string>
#include <fcntl.h>
#include <unistd.h>
#include <sys/stat.h>
#include <android/log.h>    // 1.引入磁盘相关

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_mmapdemo_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}


/*
 * “写”方法
 * */
extern "C"
JNIEXPORT void JNICALL
Java_com_example_mmapdemo_MockSharedMemory_write(JNIEnv *env, jobject thiz) {
    std::string file = "/sdcard/binder";
    int m_fd = open(file.c_str(), O_RDWR | O_CREAT, S_IRWXU);
    ftruncate(m_fd, 4096);  // 内存与磁盘映射时必须要大小一致，所以下方若要开辟4096字节内存，则磁盘也得这么大

    int8_t *m_ptr = static_cast<int8_t *>(mmap(0, 4096,
                                               PROT_READ | PROT_WRITE, MAP_SHARED, m_fd,
                                               0)); // 2.mmap函数会返回一个虚拟地址给cpu（即应用层）
    //*m_ptr = 10;
    std::string data("把一串字符串传给m_ptr");
    memcpy(m_ptr, data.data(),
           data.size()); // c里没有字符串的概念，所以只能通过内存拷贝的方式实现把字符串放进去（要粘贴的地方的虚拟地址，字符串的起始地址，要传入的字符串的大小）

}


/*
 *读方法
 * */
extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_mmapdemo_MockSharedMemory_read(JNIEnv *env, jobject thiz) {
    std::string file = "/sdcard/binder";
    int m_fd = open(file.c_str(), O_RDWR | O_CREAT, S_IRWXU);
    ftruncate(m_fd, 4096);  // 内存与磁盘映射时必须要大小一致，所以下方若要开辟4096字节内存，则磁盘也得这么大

    int8_t *m_ptr = static_cast<int8_t *>(mmap(0, 4096,
                                               PROT_READ | PROT_WRITE, MAP_SHARED, m_fd,
                                               0)); // 2.mmap函数会返回一个虚拟地址给cpu（即应用层）
    char *buf = static_cast<char *>(malloc(100));
    memcpy(buf,m_ptr,100);
    std::string result(buf);    // 转换成字符串
    __android_log_print(ANDROID_LOG_ERROR, "binderDemo：", "读取数据:%s", result.c_str());
    munmap(m_ptr, 4096);    // 映射完了别忘取消映射
    close(m_fd);        //关闭文件
    return env->NewStringUTF(result.c_str());

}