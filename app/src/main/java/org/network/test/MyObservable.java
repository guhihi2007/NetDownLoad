package org.network.test;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/5/19.
 */

public class MyObservable implements IObservable {
    protected ArrayList<MyObserver> myObservers;
    public String msg;

    public MyObservable() {
        myObservers = new ArrayList<>();
    }

    @Override
    public void register(MyObserver myObserver) {
        myObservers.add(myObserver);
    }

    @Override
    public void unregister(MyObserver myObserver) {
        myObservers.remove(myObserver);
    }

    @Override
    public void notifychanged() {
        if (myObservers != null)
            for (MyObserver observers : myObservers) {
                if (setChanged())
                    observers.updata(getMsg());
            }
    }

    private boolean setChanged() {
        return true;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
        setChanged();
        notifychanged();
    }
}
