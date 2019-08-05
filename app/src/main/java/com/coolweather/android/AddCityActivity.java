package com.coolweather.android;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.coolweather.android.db.Country;
import com.coolweather.android.util.Utility;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import static org.litepal.LitePalApplication.getContext;

public class AddCityActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> dataList = new ArrayList<>();
    private EditText searchEditText;
    private Toolbar toolbar;

    private List<Country> countryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_add_city);

        listView = findViewById(R.id.city_listview);
        toolbar = findViewById(R.id.weather_toolbar);

        toolbar.setTitle("添加城市");
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_black_36dp);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, dataList);
        listView.setAdapter(adapter);

        searchEditText = findViewById(R.id.search_edittext);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Country country = countryList.get(i);
                if(!Utility.handleCountryOwnedResponse(country)){
                    Toast.makeText(AddCityActivity.this,"城市已存在", Toast.LENGTH_SHORT).show();
                }else{
                    finish();
                }
            }
        });

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if("".equals(editable.toString())){
                    listView.setVisibility(View.INVISIBLE);
                }else {
                    countryList = DataSupport.where("countryName like ?", String.valueOf("%"+editable+"%")).find(Country.class);
                    if(countryList.size() > 0){
                        listView.setVisibility(View.VISIBLE);
                        dataList.clear();
                        for(Country country : countryList){
                            dataList.add(country.getCountryName() + "   " + country.getCityName() + "    " + country.getProvinceName());
                        }
                        adapter.notifyDataSetChanged();
                        listView.setSelection(0);
                    }else{
                        listView.setVisibility(View.INVISIBLE);
                    }
                }
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

}
