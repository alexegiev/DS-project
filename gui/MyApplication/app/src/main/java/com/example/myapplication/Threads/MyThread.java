package com.example.myapplication.Threads;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class MyThread extends Thread{

    Handler myHandler;

    public MyThread(Handler handler){
        this.myHandler = handler;
    }

    @Override
    public void run() {
        try {
            sleep(2500);
            Log.d("MYAPPLICATION","Thread woke up");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Message msg = new Message();
        Bundle bundle = new Bundle();
        bundle.putString("MyValue","This is the result");
        msg.setData(bundle);
        myHandler.sendMessage(msg);
    }
}
