package com.objcoding.disruptor;

/**
 * @author zhangchenghui.dev@gmail.com
 * @since 0.0.1
 */
public class Event<T> {

    private T value;

    public Event() {
    }

    public T get() {
        return value;
    }

    public void set(T value) {
        this.value = value;
    }

    public void clear() {
        this.value = null;
    }
}
