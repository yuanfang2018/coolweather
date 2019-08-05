package com.coolweather.android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;

public class AboutActivity extends AppCompatActivity {
    private TextView notitTitleText;
    private ImageView notiIconImage;
    private TextView notiDescText;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_notification);

        toolbar = findViewById(R.id.weather_toolbar);
        toolbar.setTitle("关于");
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_black_36dp);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        notitTitleText = findViewById(R.id.noti_title_tv);
        notiIconImage = findViewById(R.id.icon_iv);
        notiDescText = findViewById(R.id.noti_desc_tv);

        notitTitleText.setText("版本信息");
        //Glide.with(this).load(getIntent().getStringExtra("noti_icon")).into(notiIconImage);
        notiDescText.setText("0.1");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
