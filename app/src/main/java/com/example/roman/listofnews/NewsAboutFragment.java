package com.example.roman.listofnews;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roman.listofnews.data.Storage;
import com.example.roman.listofnews.data.background.ServiceUpdate;
import com.example.roman.listofnews.data.background.UploadWork;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import androidx.work.Constraints;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

public class NewsAboutFragment extends Fragment {

    @Nullable
    Switch switchIntro;
    @Nullable
    Switch switchUpdate;
    @Nullable
    Button sendMessageBtn;
    @Nullable
    EditText messageEditT;
    @Nullable
    ImageView telegramIcon;
    @Nullable
    ImageView githubIcon;
    @Nullable
    ImageView nyTimesLogo;
    private LinearLayout aboutMainLayout;
    final String TAG = "myLogs";
    private static final Integer NOTIFY_ID = 0;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.about_activity, container, false);
        setupUi(view);
        return view;
    }

    private void setupUi(View view) {
        //////////getActivity().getActionBar().setTitle(R.string.about_label);
        findView(view);
        switchForIntro();
        switchForUpdate();
        clickSendMessage();
        clickIcon();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
    }


    private void findView(View view) {
        aboutMainLayout = view.findViewById(R.id.about_main_ll);
        switchIntro = view.findViewById(R.id.about_introSw);
        switchUpdate = view.findViewById(R.id.about_updateSw);
        messageEditT = view.findViewById(R.id.etEmailMessage);
        sendMessageBtn = view.findViewById(R.id.btnSendMessage);
        telegramIcon = view.findViewById(R.id.icon_telegram);
        githubIcon = view.findViewById(R.id.icon_github);
        nyTimesLogo = view.findViewById(R.id.logo_nytimes);

    }

    private void switchForIntro() {
        if (switchIntro != null) {
            switchIntro.setChecked(Storage.checkSwitchIntro(getActivity()));
        }

        switchIntro.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean on) {
                if (on) {
                    Storage.setSwitchIntroOn(getActivity());
                } else {
                    Storage.setSwitchIntroOff(getActivity());
                }

            }
        });
    }

    private void switchForUpdate() {
        if (switchUpdate !=null) {
            switchUpdate.setChecked(Storage.checkSwitchUpdate(getActivity()));
        }

        switchUpdate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean on) {
                Intent intent = new Intent(getContext(), ServiceUpdate.class);
                if (on) {
                    Storage.setSwitchUpdateOn(getActivity());
                    startPeriodicUploadService();
                } else {
                    Storage.setSwitchUpdateOff(getActivity());
                    stopPeriodicUploadService();
                    NotificationManager notificationManager = (NotificationManager) Objects.requireNonNull(getContext())
                            .getSystemService(Context.NOTIFICATION_SERVICE);
                    Objects.requireNonNull(notificationManager).cancel(NOTIFY_ID);
                }
            }
        });

    }

    private void startPeriodicUploadService() {
        Constraints workConstraints = new Constraints.Builder()
                .setRequiresCharging(true)
                .build();
        //set tag. Tag will safe if user reload devise
        Storage.setTagUploadWork(getActivity());
        //for Test
        /*OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(UploadWork.class)
                .setConstraints(workConstraints)
                .addTag(Storage.getTagUploadWork(getActivity()))
                .build();*/

        PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder(UploadWork.class, 120, TimeUnit.MINUTES, 115, TimeUnit.MINUTES)
                .setConstraints(workConstraints)
                .addTag(Storage.getTagUploadWork(getActivity()))
                .build();

        WorkManager.getInstance().enqueue(workRequest);
        Toast toast = Toast.makeText(getActivity(), getString(R.string.messagePeriodicUpload) , Toast.LENGTH_LONG);
        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        if( v != null) v.setGravity(Gravity.CENTER);
        toast.show();
    }

    private void stopPeriodicUploadService() {
        WorkManager.getInstance().cancelAllWorkByTag(Storage.getTagUploadWork(getActivity()));
    }

    private void clickSendMessage() {
        sendMessageBtn.setOnClickListener(v -> {
            String message = messageEditT.getText().toString();
            if (message.isEmpty()) {
                showErrorEmailMessage();
                return;
            }
            sendingMessage(message);
        });
    }

    private void showErrorEmailMessage() {
        Snackbar.make(
                aboutMainLayout,
                R.string.about_show_error_email_message,
                Snackbar.LENGTH_SHORT).show();
    }

    private void sendingMessage(String message) {
        composeEmail(new String[]{getString(R.string.about_email_addresses)},
                getString(R.string.about_subject),
                message);
    }

    public void composeEmail(String[] addresses, String subject, String message) {

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // "mailto:"  +""" " only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT,message);
        if (intent.resolveActivity(getActivity().getPackageManager()) == null) {
            Snackbar.make(
                    aboutMainLayout,
                    R.string.about_send_email_error,
                    Snackbar.LENGTH_SHORT).show();
            return;
        }
        startActivity(intent);
    }

    private void clickIcon() {
        telegramIcon.setOnClickListener(v ->
                openUrl(getString(R.string.telegram_icon_url)));
        githubIcon.setOnClickListener(v ->
                openUrl(getString(R.string.github_icon_url)));
        nyTimesLogo.setOnClickListener(v ->
                openUrl(getString(R.string.nytimes_logo_url)));

    }

    private void openUrl(String url) {
        Intent urlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        if (urlIntent.resolveActivity(getActivity().getPackageManager()) == null) {
            Snackbar.make(
                    aboutMainLayout,
                    R.string.about_open_icon_url_error,
                    Snackbar.LENGTH_SHORT).show();
            return;
        }
        startActivity(urlIntent);
    }

    @Override
    public void onDetach() {
        Log.d(TAG, "--- AboutFragment onDetach");
        super.onDetach();
    }
}