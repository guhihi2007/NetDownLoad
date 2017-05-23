package org.network.test;

/**
 * Created by Administrator on 2017/5/19.
 */

public class TestMethod {
    public void mytest(){
        MyObservable myObservable = new MyObservable();
        MyObserver m1= new MyObserver();
        MyObserver m2= new MyObserver();
        MyObserver m3= new MyObserver();
        myObservable.register(m1);
        myObservable.register(m2);
        myObservable.register(m3);
        myObservable.notifychanged();
    }
}
