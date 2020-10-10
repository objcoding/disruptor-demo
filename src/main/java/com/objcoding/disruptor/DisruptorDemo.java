package com.objcoding.disruptor;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

/**
 * @author zhangchenghui.dev@gmail.com
 * @since 0.0.1
 */
public class DisruptorDemo {

    private static final Disruptor<Event<Long>> disruptor;

    static {
        // 创建disruptor
        disruptor = new Disruptor<>(
            new MyEventFactory<>(),
            16,
            r -> {
                return new Thread(r, "disruptor-thread");
            },
            ProducerType.MULTI,
            new BlockingWaitStrategy()
        );
        // 设置EventHandler
        disruptor.handleEventsWith(new MyHandler<>(), new MyHandler2<>());
        // 启动disruptor的线程
        disruptor.start();
    }

    public static void main(String[] args) throws Exception {
        for (long i = 0; i <= 100L; i++) {
            final long j = i;
            new Thread(() -> disruptor.getRingBuffer().tryPublishEvent((event, sequence, v) -> event.set(v), j)).start();
            Thread.sleep(10);
        }
    }
}
