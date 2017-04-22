# RXBUS

![logo](https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1492844570081&di=a3253baefe4b0d0d418c5c36eff5a9db&imgtype=0&src=http%3A%2F%2Fimg.tupianzj.com%2Fuploads%2Fallimg%2F150802%2F9-150P2105405.jpg)

## 解决问题
* 消息的全局分发
* 消息的定向分发
* 消息的拦截分发
* 与Rxjava、Retrofit完整结合
* 解决了内存泄露问题

## 基本思想
* 基于Rxjava实现
* 定向发送消息的id唯一，避免重复id的产生
* annotation通过apt动态生成编译代码

## 使用方法

### 监听器

#### 全局监听

 	GlobalRxBus.instance().subscribe(new Func1<RxBus.ObserverObject<String>, Boolean>() {
            @Override
            public Boolean call(RxBus.ObserverObject<String> observerObject) {
                return observerObject.getTag() == GlobalMessage.GM1;
            }
        }, new Action1<RxBus.ObserverObject<String>>() {
            @Override
            public void call(RxBus.ObserverObject<String> tObserverObject) {
                Toast.makeText(MainActivity.this, tObserverObject.getObject(), Toast.LENGTH_SHORT).show();
            }
        });
        
#### 本地监听
	RxBus bus = new RxBus();
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
        
### 发送器

#### 全局发送

    GlobalRxBus.instance().sSend("消息来了。。。", GlobalMessage.GM1);
    
#### 本地发送

 	bus.send("消息来了。。。", RxBusLocalTagsAnnotation.LM1);
 	
### Annotation使用 
* packageName：annotation编译时tag包名，默认是GlobalMessageDef的包名
* calssName：annotation编译时tag类名，默认是RxBus+ GlobalMessageDef的类名
	
		@GlobalTagsAnnotation(
        packageName = "com.rxbus",
        className = "GlobalMessage",
        tags = {
                "GM1",
                "GM2"
        }
		)
		public @interface GlobalMessageDef {
		}
		
### Dependencies
 	javapoet    : 'com.squareup:javapoet:1.8.0',
    auto_service: 'com.google.auto.service:auto-service:1.0-rc2',
    rxjava      : 'io.reactivex:rxjava:1.1.5',
    appcompat   : 'com.android.support:appcompat-v7:25.3.1'
        
    dependencies {
        classpath 'com.android.tools.build:gradle:2.2.0'
        classpath 'com.neenbedankt.gradle.plugins:android-apt:1.4'
        e individual module build.gradle files
    }
















