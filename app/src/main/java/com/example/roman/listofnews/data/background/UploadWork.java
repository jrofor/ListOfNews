package com.example.roman.listofnews.data.background;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class UploadWork extends Worker {
    private Context ctx;
    private final String TAG = "myLogs";

    public UploadWork(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.ctx = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        Intent intent = new Intent(ctx, ServiceUpdate.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ctx.startForegroundService(intent);
        } else {
            ctx.startService(intent);
        }

        Log.d(TAG, "--- //w// doWork");
        return Result.success();
    }
}
