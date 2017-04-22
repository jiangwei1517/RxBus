package com.jiangwei.rxbus.api;

//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;

import com.jiangwei.rxbus.annotation.GlobalTag;
import com.jiangwei.rxbus.annotation.LocalTag;

import rx.Observable;
import rx.Observer;
import rx.Scheduler;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Created by juyanwen on 2016/5/18.
 */
public class RxBus {

    private final Subject<Object, Object> _bus = new SerializedSubject<>(PublishSubject.create());

    public <T> void send(T obj, @GlobalTag @LocalTag int tag) {
        _bus.onNext(new ObserverObject(obj, tag));
    }

    public <T> Subscription subscribe(/* @Nullable */ final Func1<ObserverObject<T>, Boolean> filter,
            /* @NonNull */ final Action1<ObserverObject<T>> onNext) {
        return subscribe(filter, onNext, null, null, null);
    }

    public <T> Subscription subscribe(/* @Nullable */ final Func1<ObserverObject<T>, Boolean> filter,
            /* @NonNull */ final Action1<ObserverObject<T>> onNext, /* @NonNull */ final Scheduler scheduler) {
        return subscribe(filter, onNext, null, null, scheduler);
    }

    public <T> Subscription subscribe(/* @Nullable */ final Func1<ObserverObject<T>, Boolean> filter,
            /* @NonNull */ final Action1<ObserverObject<T>> onNext, /* @Nullable */ final Action1<Throwable> onError,
            /* @Nullable */ final Action0 onComplete, /* @Nullable */ Scheduler scheduler) {
        return _bus.filter(new Func1<Object, Boolean>() {
            @Override
            public Boolean call(Object o) {
                if (!(o instanceof ObserverObject)) {
                    return false;
                }

                if (filter != null) {
                    ObserverObject<T> observerObj = (ObserverObject<T>) o;
                    return observerObj.canPropagate && filter.call(observerObj);
                }
                return true;
            }
        }).observeOn(scheduler == null ? Schedulers.trampoline() : scheduler).subscribe(new Observer<Object>() {

            @Override
            public void onCompleted() {
                if (onComplete != null) {
                    onComplete.call();
                }
            }

            @Override
            public void onError(Throwable e) {
                if (onError != null) {
                    onError.call(e);
                } else {
                    System.out.println("Unhandled error in RxBus:");
                    e.printStackTrace();
                }
            }

            @Override
            public void onNext(Object o) {
                onNext.call((ObserverObject<T>) o);
            }
        });
    }

    /**
     * 和send(Object)配合使用，如果发送的类型不是ObserverObject，由该方法注册的Action处理
     */
    public Subscription subscribeObject(/* @Nullable */ final Func1<Object, Boolean> filter,
            /* @Nonnull */ final Action1<Object> onNext, /* @Nullable */ final Action1<Throwable> onError,
            /* @Nullable */ final Action0 onComplete, /* @Nullable */ Scheduler scheduler) {
        return _bus.filter(new Func1<Object, Boolean>() {
            @Override
            public Boolean call(Object o) {
                if (o instanceof ObserverObject) {
                    return false;
                }

                if (filter != null) {
                    return filter.call(o);
                }
                return true;
            }
        }).observeOn(scheduler == null ? Schedulers.trampoline() : scheduler).subscribe(new Observer<Object>() {

            @Override
            public void onCompleted() {
                if (onComplete != null) {
                    onComplete.call();
                }
            }

            @Override
            public void onError(Throwable e) {
                if (onError != null) {
                    onError.call(e);
                } else {
                    System.out.println("Unhandled error in RxBus:");
                    e.printStackTrace();
                }
            }

            @Override
            public void onNext(Object o) {
                onNext.call(o);
            }
        });
    }

    public Subscription subscribeObject(/* @Nullable */ final Func1<Object, Boolean> filter,
            /* @NonNull */ final Action1<Object> onNext) {
        return subscribeObject(filter, onNext, null, null, null);
    }

    public void send(Object o) {
        _bus.onNext(o);
    }

    public Observable<Object> toObservable() {
        return _bus;
    }

    public boolean hasObservers() {
        return _bus.hasObservers();
    }

    public static final class ObserverObject<T> {
        private T actual;
        private boolean canPropagate = true;
        private int tag;

        public ObserverObject(T obj, @GlobalTag @LocalTag int tag) {
            this.actual = obj;
            this.tag = tag;
        }

        /**
         * 在filter中实现消息链上某个设置setCanPropagate(false)后，后续observer不再响应
         */
        public boolean canPropagate() {
            return canPropagate;
        }

        public void setCanPropagate(boolean canPropagate) {
            this.canPropagate = canPropagate;
        }

        public T getObject() {
            return actual;
        }

        public int getTag() {
            return tag;
        }
    }
}
