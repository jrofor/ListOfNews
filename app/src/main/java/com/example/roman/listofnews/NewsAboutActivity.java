package com.example.roman.listofnews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.util.TimeFormatException;
import android.util.TimeUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class NewsAboutActivity extends AppCompatActivity implements View.OnClickListener {

    EditText message;
    String LinkToTelegram;
    ImageView avaImageView;
    @NonNull
    private Thread MyUiRunnable;
    final String TAG = "myLogs";
    private boolean isRunning =true;
    private static final Object lock = new Object();
    private static boolean work = true;
    private Handler mUiHandler = new Handler();
    private MyWorkerThread mWorkerHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_activity);

        message = (EditText) findViewById(R.id.etEmailMassege);

        Button sendMassege = findViewById(R.id.btnSendMassege);
        sendMassege.setOnClickListener(this);
        ImageView telegram = findViewById(R.id.telegram);
        telegram.setOnClickListener(this);
        ImageView avatarImageView = findViewById(R.id.image_avatar);
        avatarImageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnSendMassege:
                composeEmail(new String[]{getString(R.string.addresses)},getString(R.string.subject), message.getText().toString());
                break;
            case R.id.telegram:
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.LinkToTelegram)));
                startActivity(intent);
                break;
            case R.id.image_avatar:
                onClickAvatarWithThread();
        }

    }

    public void composeEmail(String[] addresses, String subject, String message) {

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // "mailto:"  +""" " only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT,message);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
//------------------------------------------------------------------------------------------------

    public void onClickAvatarWithThread(){
        //mHandler.post(new MyUiRunnable("new text"));
        //new Thread(new MyUiRunnable()).start();

        //Thread myThread = new Thread(new Runnable() {
        mWorkerHandler = new MyWorkerThread("myWorkerThread");
        Runnable task = new Runnable() {
            @Override
            public void run() {
                /*for (int i=0; i<4; i++) {
                    try {
                        TimeUnit.MILLISECONDS.sleep( 500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (i==2) {
                        mUiHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(NewsAboutActivity.this,"Loading News 50%", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "Loading News 50%");
                            }
                        });
                    }
                }*/
                mUiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(NewsAboutActivity.this,"News", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "News");

                    }
                });
            }
         };
        //myThread.start();
        mWorkerHandler.start();
        mWorkerHandler.prepareHandler();
        mWorkerHandler.postTask(task);

    }

    @Override
    public void onStop (){
        if (mWorkerHandler != null) mWorkerHandler.quit();
        mWorkerHandler = null;
        super.onStop();
        Log.d(TAG, "---onStop---");
    }

    public class MyWorkerThread extends HandlerThread {

        private Handler mWorkerHandler;
        public MyWorkerThread(String name) {
            super(name);
        }

        public void postTask(Runnable task) {
            mWorkerHandler.post(task);
        }

        public void prepareHandler() {
            mWorkerHandler = new Handler(getLooper());
        }

    }

}