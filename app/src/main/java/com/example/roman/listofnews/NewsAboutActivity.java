package com.example.roman.listofnews;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class NewsAboutActivity extends AppCompatActivity {

    Button SendMessageBtn;
    EditText messageEditT;
    ImageView telegramIcon;
    ImageView githubIcon;
    ImageView nyTimesLogo;
    private LinearLayout aboutMainLayout;
    final String TAG = "myLogs";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_activity);
        setupUi();
    }

    private void setupUi() {
        findView();
        clickSendMessage();
        clickIcon();
    }

    private void findView() {
        aboutMainLayout = findViewById(R.id.about_main_ll);
        messageEditT = findViewById(R.id.etEmailMessage);
        SendMessageBtn = findViewById(R.id.btnSendMessage);
        telegramIcon = findViewById(R.id.icon_telegram);
        githubIcon = findViewById(R.id.icon_github);
        nyTimesLogo = findViewById(R.id.logo_nytimes);

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
        if (intent.resolveActivity(getPackageManager()) == null) {
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
        if (urlIntent.resolveActivity(getPackageManager()) == null) {
            Snackbar.make(
                    aboutMainLayout,
                    R.string.about_open_icon_url_error,
                    Snackbar.LENGTH_SHORT).show();
            return;
        }
        startActivity(urlIntent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}