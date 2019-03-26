package com.example.roman.listofnews.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.roman.listofnews.R;
import com.example.roman.listofnews.data.NewsItem;

import java.util.List;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;


public class NewsRecyclerAdapter extends RecyclerView.Adapter<NewsRecyclerAdapter.ViewHolder>{
    @Nullable
    private final List<NewsItem> news;
    @Nullable
    private final Context context;
    @Nullable
    private final OnItemClickListener clickListener;
    private final LayoutInflater inflater;

    final String TAG = "myLogs";
    //private Handler mUiHandler = new Handler();
    //private  MyWorkerThread  mWorkerHandler;// = new NewsAboutActivity.MyWorkerThread("loadingImageThread");



    public NewsRecyclerAdapter(@Nullable Context context, @Nullable List<NewsItem> news, @Nullable OnItemClickListener clickListener) {
        this.news = news;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                inflater.inflate(R.layout.item_news, parent, false), clickListener
        );
    }

    @Override
    public void onBindViewHolder(@NonNull NewsRecyclerAdapter.ViewHolder holder, int position) {
        holder.bind(news.get(position));

    }

    public interface OnItemClickListener {
    //    void OnItemClick(NewsItem news);
     void OnItemClick(int position); }

    @Override
    public int getItemCount() {
        return news.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView titleView;
        public final TextView previewView;
        public final TextView categoryView;
        public final ImageView imageView;
        public final TextView textDateView;

        ViewHolder(@NonNull View itemView, OnItemClickListener clickListener) {
            super(itemView);
            itemView.setOnClickListener( view -> {
                int position = getAdapterPosition();
                if (clickListener != null && position != RecyclerView.NO_POSITION){
                //    clickListener.OnItemClick(news.get(position));
                    //clickListener.OnItemClickPosition( getAdapterPosition());
                    clickListener.OnItemClick(getAdapterPosition());
                }
            });

            titleView = itemView.findViewById(R.id.title);
            previewView = itemView.findViewById(R.id.preview);
            categoryView =  itemView.findViewById(R.id.CategoryName);
            imageView = itemView.findViewById(R.id.image);
            textDateView = itemView.findViewById(R.id.textDate);
        }

        public void bind(NewsItem news) {
            titleView.setText(news.getTitle());
            previewView.setText(news.getPreviewText());
            categoryView.setText(news.getCategory().getName());
            Glide.with(context).load(news.getImageUrl()).into(imageView);
            textDateView.setText(news.getPublishDate().toString());



        }




    }

    /*private void loadingImage(Context context, View itemView, NewsItem news, boolean received){
        //final boolean[] received = {false};
        mWorkerHandler = new MyWorkerThread("loadingImageThread");
        final boolean[] finalReceived = {received};
        Runnable task = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000 );
                    Log.d(TAG, "Sleep");

                    mUiHandler.post(new Runnable() { //постим в Looper (очередь) MainThread
                        @Override
                        public void run() {
                            synchronized (finalReceived[0]) {
                                if (!finalReceived[0]) {
                                    ImageView imageView = itemView.findViewById(R.id.image);
                                    Glide.with(context).load(news.getImageUrl()).into(imageView);
                                    Log.d(TAG, "Thread loadingImage");
                                    finalReceived[0] = true;
                                }
                            }
                        }

                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    mWorkerHandler.interrupt();    // сбрасываем состояние
                    Log.d(TAG, "Image loaded");
                }
            }
        };

        mWorkerHandler.start(); //запускаем Handler
        mWorkerHandler.prepareHandler(); //берём и прикрепляемя к Looper главного(текущего) потока
        mWorkerHandler.postTask(task);  //постим в Looper (в конец очереди) созданный Runnable
        //
        if (finalReceived[0] && mWorkerHandler != null){
            mWorkerHandler.quit();
            Log.d(TAG, "---Thread quit---");
            received = false;
        }


    }



    public class MyWorkerThread extends HandlerThread {
        private  Handler mWorkerHandler;
        public MyWorkerThread(String name) {
            super(name);
        }

        public void postTask (Runnable task) {
            mWorkerHandler.post(task);
        }

        public void prepareHandler() {
            mWorkerHandler = new Handler(getLooper());
        }

        @Override
        public void interrupt() {
            super.interrupt();
        }
    }
*/


}
