package com.objcoding.disruptor.falsesharing;

/**
 * @author zhangchenghui.dev@gmail.com
 * @since 0.0.1
 */
public class CacheLineEffect {

    //考虑一般缓存行大小是64字节，一个 long 类型占8字节
    static long[][] arr;

    public static void main(String[] args) {

        int size = 1024 * 1024;

        arr = new long[size][];
        for (int i = 0; i < size; i++) {
            arr[i] = new long[8];
            for (int j = 0; j < 8; j++) {
                arr[i][j] = 0L;
            }
        }
        long sum = 0L;
        long marked = System.currentTimeMillis();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < 8; j++) {
                sum = arr[i][j];
            }
        }
        System.out.println("[cache line]Loop times:" + (System.currentTimeMillis() - marked) + "ms");

        marked = System.currentTimeMillis();
        for (int i = 0; i < 8; i += 1) {
            for (int j = 0; j < size; j++) {
                sum = arr[j][i];
            }
        }
        System.out.println("[no cache line]Loop times:" + (System.currentTimeMillis() - marked) + "ms");
    }

}
