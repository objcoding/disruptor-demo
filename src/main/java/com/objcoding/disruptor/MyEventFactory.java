package com.objcoding.disruptor;

import com.lmax.disruptor.EventFactory;

/**
 * @author zhangchenghui.dev@gmail.com
 * @since 0.0.1
 */
public class MyEventFactory<T> implements EventFactory<Event<T>> {

    @Override
    public Event<T> newInstance() {
        return new Event<>();
    }
}
