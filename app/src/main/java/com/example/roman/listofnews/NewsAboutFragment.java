package com.example.roman.listofnews;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class NewsAboutFragment extends Fragment {

    @Nullable
    Button SendMessageBtn;
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
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
        clickSendMessage();
        clickIcon();
    }

    private void findView(View view) {
        aboutMainLayout = view.findViewById(R.id.about_main_ll);
        messageEditT = view.findViewById(R.id.etEmailMessage);
        SendMessageBtn = view.findViewById(R.id.btnSendMessage);
        telegramIcon = view.findViewById(R.id.icon_telegram);
        githubIcon = view.findViewById(R.id.icon_github);
        nyTimesLogo = view.findViewById(R.id.logo_nytimes);

    }

    private void clickSendMessage() {
        SendMessageBtn.setOnClickListener(v -> {
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


}