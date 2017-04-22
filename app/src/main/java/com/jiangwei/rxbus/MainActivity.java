package com.jiangwei.rxbus;

import com.rxbus.GlobalMessage;
import com.jiangwei.rxbus.api.GlobalRxBus;
import com.jiangwei.rxbus.api.RxBus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import rx.functions.Action1;
import rx.functions.Func1;

public class MainActivity extends AppCompatActivity {
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.tv);
        GlobalRxBus.instance().subscribe(new Func1<RxBus.ObserverObject<String>, Boolean>() {
            @Override
            public Boolean call(RxBus.ObserverObject<String> observerObject) {
                return observerObject.getTag() == GlobalMessage.GM1;
            }
        }, new Action1<RxBus.ObserverObject<String>>() {
            @Override
            public void call(RxBus.ObserverObject<String> tObserverObject) {
                System.out.println(tObserverObject.getObject());
            }
        });
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalRxBus.instance().sSend("1", GlobalMessage.GM1);
            }
        });
    }
}
