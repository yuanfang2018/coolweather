package com.coolweather.android;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.coolweather.android.db.CountryOwned;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class ManageCitiesActivity extends AppCompatActivity {

    private List<CountryOwned> countryOwnedList = new ArrayList<>();

    private ListView ownedCityListView;
    private Toolbar toolbar;

    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_manage_city);

        ownedCityListView = findViewById(R.id.owned_city_list);
        toolbar = findViewById(R.id.weather_toolbar);

        toolbar.setTitle("管理城市");
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_black_36dp);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        countryOwnedList = DataSupport.findAll(CountryOwned.class);
        adapter = new MyAdapter(ManageCitiesActivity.this, R.layout.city_owned_item, countryOwnedList);
        ownedCityListView.setAdapter(adapter);
        ownedCityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //CountryOwned countryOwned = countryOwnedList.get(i);
                //Toast.makeText(ManageCitiesActivity.this, String.valueOf(i), LENGTH_SHORT).show();

            }
        });
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

    @Override
    protected void onResume() {
        countryOwnedList.clear();
        countryOwnedList.addAll(DataSupport.findAll(CountryOwned.class));
        adapter.notifyDataSetChanged();
        super.onResume();
    }
}
