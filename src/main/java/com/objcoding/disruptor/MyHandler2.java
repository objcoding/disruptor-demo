package com.objcoding.disruptor;


import com.lmax.disruptor.EventHandler;

/**
 * @author zhangchenghui.dev@gmail.com
 * @since 0.0.1
 */
public class MyHandler2<T> implements EventHandler<Event<T>> {

    @Override
    public void onEvent(Event<T> event, long sequence, boolean endOfBatch) throws Exception {
        System.out.println("Event2: " + event.get());
    }
}
