package com.jiangwei.rxbus;

import com.jiangwei.rxbus.api.GlobalRxBus;
import com.jiangwei.rxbus.api.RxBus;
import com.rxbus.GlobalMessage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import rx.functions.Action1;
import rx.functions.Func1;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

public class LocalMessageActivity extends AppCompatActivity {
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = (Button) findViewById(R.id.btn);
        final RxBus bus = new RxBus();
        bus.subscribe(new Func1<RxBus.ObserverObject<String>, Boolean>() {
            @Override
            public Boolean call(RxBus.ObserverObject<String> observerObject) {
                return observerObject.getTag() == RxBusLocalTagsAnnotation.LM1;
            }
        }, new Action1<RxBus.ObserverObject<String>>() {
            @Override
            public void call(RxBus.ObserverObject<String> tObserverObject) {
                Toast.makeText(LocalMessageActivity.this, tObserverObject.getObject(), Toast.LENGTH_SHORT).show();
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bus.send("消息来了。。。", RxBusLocalTagsAnnotation.LM1);
            }
        });
    }
}
