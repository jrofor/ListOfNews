package com.example.roman.listofnews;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
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

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.example.roman.listofnews.data.Storage;
import com.example.roman.listofnews.mvp.NewsAboutPresenter;
import com.example.roman.listofnews.mvp.NewsAboutView;

import java.util.Objects;

public class NewsAboutFragment extends MvpAppCompatFragment implements NewsAboutView{

    @InjectPresenter
    NewsAboutPresenter newsAboutPresenter;

    private static final int LAYOUT = R.layout.fragment_news_about;

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
        View view = inflater.inflate(LAYOUT, container, false);
        setupUi(view);
        Log.d(TAG, "--- AboutFragment onCreateView");
        return view;
    }

    private void setupUi(View view) {
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

    @Override
    public void onStart() {
        super.onStart();
        onChangeSwitchIntro();
        onChangeSwitchUpdate();
    }

    @Override
    public void onResume(){
        super.onResume();
        // Set title bar
        ((MainActivity) Objects.requireNonNull(getActivity())).setupActionBar(getString(R.string.about_label), true);
    }


//=======================================================================================switchIntro

    private void switchForIntro() {
        if (switchIntro != null) {
            newsAboutPresenter.setCheckedValueSwIntro();
        }
    }

    @Override
    public void setValueSwIntro() {
        switchIntro.setChecked(Storage.checkSwitchIntro(getActivity()));
    }

    private void onChangeSwitchIntro() {
        switchIntro.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean on) {
                newsAboutPresenter.onCheckedChangedSwIntro(on);
            }
        });
    }

    @Override
    public void saveValueSwIntro(@NonNull Boolean on) {
        if (on) {
            Storage.setSwitchIntroOn(getActivity());
        } else {
            Storage.setSwitchIntroOff(getActivity());
        }
    }

//======================================================================================switchUpdate

    private void switchForUpdate() {
        if (switchUpdate !=null) {
            newsAboutPresenter.setCheckedValueSwUpdate();
        }
    }

    @Override
    public void setValueSwUpdate() {
        switchUpdate.setChecked(Storage.checkSwitchUpdate(getActivity()));
    }

    private void onChangeSwitchUpdate() {
        switchUpdate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean on) {
                newsAboutPresenter.onCheckedChangedSwUpdate(on);
            }
        });
    }

    @Override
    public void saveValueAndWorkSwUpdate(@NonNull Boolean on) {
        if (on) {
            Storage.setSwitchUpdateOn(getActivity());
            //setting tag for stop PeriodicWorkRequest. Tag will save if user reload devices
            newsAboutPresenter.startPeriodicUpdateService(Storage.getTagUpdateWork(getActivity()));
        } else {
            Storage.setSwitchUpdateOff(getActivity());
            newsAboutPresenter.stopPeriodicUpdateService(Storage.getTagUpdateWork(getActivity()));
            NotificationManager notificationManager = (NotificationManager) Objects.requireNonNull(getContext())
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            Objects.requireNonNull(notificationManager).cancel(NOTIFY_ID);
        }
    }

    @Override
    public void showWorkPeriodicUpdateService() {
        Toast toast = Toast.makeText(getActivity(), getString(R.string.messagePeriodicUpdate) , Toast.LENGTH_LONG);
        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        if( v != null) v.setGravity(Gravity.CENTER);
        toast.show();
    }

//==================================================================================clickSendMessage

    private void clickSendMessage() {
        sendMessageBtn.setOnClickListener(v -> {
            String message = messageEditT.getText().toString();
            if (message.isEmpty()) {
                newsAboutPresenter.ErrorEmailMessage();
                return;
            }
            newsAboutPresenter.onClickSendMessage(message);
        });
    }

    @Override
    public void showErrorEmailMessage() {
        Snackbar.make(
                aboutMainLayout,
                R.string.about_show_error_email_message,
                Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void sendingMessage(@NonNull String message) {
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
            newsAboutPresenter.ErrorEmailClient();
            return;
        }
        startActivity(intent);
    }

    @Override
    public void showErrorEmailClient() {
        Snackbar.make(
                aboutMainLayout,
                R.string.about_send_email_error_client,
                Snackbar.LENGTH_SHORT).show();
    }

//=========================================================================================clickIcon

    private void clickIcon() {
        telegramIcon.setOnClickListener(v ->
                newsAboutPresenter.onClickTelegramIcon());
        githubIcon.setOnClickListener(v ->
                newsAboutPresenter.onClickGithubIcon());
        nyTimesLogo.setOnClickListener(v ->
                newsAboutPresenter.onClickNyTimesLogo());
    }

    @Override
    public void openUrlTelegramIcon() {
        openUrl(getString(R.string.telegram_icon_url));
    }

    @Override
    public void openUrlGithubIcon() {
        openUrl(getString(R.string.github_icon_url));
    }

    @Override
    public void openUrlNyTimesLogo() {
        openUrl(getString(R.string.nytimes_logo_url));
    }

    private void openUrl(String url) {
        Intent urlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        if (urlIntent.resolveActivity(getActivity().getPackageManager()) == null) {
            newsAboutPresenter.ErrorWebClient();
            return;
        }
        startActivity(urlIntent);
    }

    @Override
    public void showErrorWebClient() {
        Snackbar.make(
                aboutMainLayout,
                R.string.about_open_icon_url_error,
                Snackbar.LENGTH_SHORT).show();
    }

//==================================================================================================

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

    @Override
    public void onDetach() {
        Log.d(TAG, "--- AboutFragment onDetach");
        super.onDetach();
    }
}