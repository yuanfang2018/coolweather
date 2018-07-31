package com.coolweather.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.coolweather.android.db.CountryOwned;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;

public class OwnedCitiesActivity extends AppCompatActivity {

    private List<CountryOwned> countryOwnedList = new ArrayList<>();

//    private FloatingActionButton floatingActionButton;
    private ListView ownedCityListView;
    private Toolbar toolbar;

    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_city_owned);

//        floatingActionButton = findViewById(R.id.floatingActionButton2);
        ownedCityListView = findViewById(R.id.owned_city_list);
        toolbar = findViewById(R.id.weather_toolbar);

        toolbar.setTitle("管理城市");
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_black_36dp);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        countryOwnedList = DataSupport.findAll(CountryOwned.class);
        adapter = new MyAdapter(OwnedCitiesActivity.this, R.layout.city_owned_item, countryOwnedList);
        ownedCityListView.setAdapter(adapter);
        ownedCityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //CountryOwned countryOwned = countryOwnedList.get(i);
                //Toast.makeText(OwnedCitiesActivity.this, String.valueOf(i), LENGTH_SHORT).show();

            }
        });
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(layoutManager);
//        adapter = new MyAdapter(countryOwnedList);
//        recyclerView.setAdapter(adapter);

//        floatingActionButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(OwnedCitiesActivity.this, AddCityActivity.class);
//                startActivity(intent);
//            }
//        });
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
