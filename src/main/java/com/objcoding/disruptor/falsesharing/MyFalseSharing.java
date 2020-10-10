package com.objcoding.disruptor.falsesharing;

/**
 * @author zhangchenghui.dev@gmail.com
 * @since 1.0.0
 */
public class MyFalseSharing {

    public static void main(String[] args) throws InterruptedException {
        for (int i = 1; i < 10; i++) {
            System.gc();
            final long start = System.currentTimeMillis();
            runTest(Type.PADDING, i);
            System.out.println("[PADDING]Thread num " + i + " duration = " + (System.currentTimeMillis() - start));
        }
        for (int i = 1; i < 10; i++) {
            System.gc();
            final long start = System.currentTimeMillis();
            runTest(Type.NO_PADDING, i);
            System.out.println("[NO_PADDING] Thread num " + i + " duration = " + (System.currentTimeMillis() - start));
        }
    }

    private static void runTest(Type type, int NUM_THREADS) throws InterruptedException {
        Thread[] threads = new Thread[NUM_THREADS];

        switch (type) {
            case PADDING:
                DataPadding.longs = new ValuePadding[NUM_THREADS];
                for (int i = 0; i < DataPadding.longs.length; i++) {
                    DataPadding.longs[i] = new ValuePadding();
                }
                break;
            case NO_PADDING:
                Data.longs = new ValueNoPadding[NUM_THREADS];
                for (int i = 0; i < Data.longs.length; i++) {
                    Data.longs[i] = new ValueNoPadding();
                }
                break;
        }

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(new FalseSharing(type, i));
        }
        for (Thread t : threads) {
            t.start();
        }
        for (Thread t : threads) {
            t.join();
        }
    }

    // 线程执行单元
    static class FalseSharing implements Runnable {
        public final static long ITERATIONS = 500L * 1000L * 100L;
        private int arrayIndex;
        private Type type;

        public FalseSharing(Type type, final int arrayIndex) {
            this.arrayIndex = arrayIndex;
            this.type = type;
        }

        public void run() {
            long i = ITERATIONS + 1;
            // 读取共享变量中指定的下标对象，并对其value变量不断修改
            // 由于每次读取数据都会写入缓存行，如果线程间有共享的缓存行数据，就会导致伪共享问题发生
            // 如果对象已填充，那么线程每次读取到缓存行中的对象就不会产生伪共享问题
            switch (type) {
                case NO_PADDING:
                    while (0 != --i) {
                        Data.longs[arrayIndex].value = 0L;
                    }
                    break;
                case PADDING:
                    while (0 != --i) {
                        DataPadding.longs[arrayIndex].value = 0L;
                    }
                    break;
            }
        }
    }

    // 线程间贡献的数据
    public final static class Data {
        public static ValueNoPadding[] longs;
    }

    public final static class DataPadding {
        public static ValuePadding[] longs;
    }

    // 使用填充对象
    public final static class ValuePadding {
        // 前置填充对象
        protected long p1, p2, p3, p4, p5, p6;
        // value 值
        protected volatile long value = 0L;
        // 后置填充对象
        protected long p9, p10, p11, p12, p13, p14, p15;
    }

    // 不填充对象
//    @sun.misc.Contended
    public final static class ValueNoPadding {
        protected volatile long value = 0L;
    }

    enum Type {
        NO_PADDING,
        PADDING
    }
}
