package com.tied.android.tiedapp.customs;

import android.location.Address;
import android.os.AsyncTask;
import android.os.Build;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Emmanuel on 6/25/2016.
 */
public class MyListAsyncTask extends AsyncTask<Void, Void, List<Address>> {


    public void execute() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            int corePoolSize = 15;
            int maximumPoolSize = 20;
            int keepAliveTime = 40;

            BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(maximumPoolSize);
            Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);
            executeOnExecutor(threadPoolExecutor);

        } else {
            execute();
        }
    }

    @Override
    protected List<Address> doInBackground(Void... params) {
        return null;
    }
}
