package com.jiangwei.rxbus.api;

//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;

import com.jiangwei.rxbus.annotation.LocalTag;

import rx.Scheduler;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * 重新继承以使全局RxBus与其他RxBus有明显区别
 *
 * Created by juyanwen on 2016/5/18.
 */
public class GlobalRxBus extends RxBus {

    private static final GlobalRxBus sRxBus = new GlobalRxBus();

    public static GlobalRxBus instance() {
        return sRxBus;
    }

    public static <T> void sSend(T obj, @LocalTag int tag) {
        sRxBus.send(obj, tag);
    }

    public static <T> Subscription sSubscribe(/* @Nullable */ Func1<ObserverObject<T>, Boolean> filter,
            /* @NonNull */ Action1<ObserverObject<T>> observer) {
        return sRxBus.subscribe(filter, observer);
    }

    public static <T> Subscription sSubscribe(/* @Nullable */ Func1<ObserverObject<T>, Boolean> filter,
            /* @NonNull */ Action1<ObserverObject<T>> observer, /* @NonNull */ Scheduler scheduler) {
        return sRxBus.subscribe(filter, observer, scheduler);
    }
}
