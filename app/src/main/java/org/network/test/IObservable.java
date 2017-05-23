package org.network.test;

/**
 * Created by Administrator on 2017/5/19.
 */

public interface IObservable {
    void register(MyObserver myObserver);
    void unregister(MyObserver myObserver);
    void notifychanged();
}
