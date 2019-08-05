package com.coolweather.android;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.coolweather.android.db.CountryOwned;

import java.util.List;

public class MyAdapter extends ArrayAdapter<CountryOwned> {

    private int resourceId;
    private Context context;

    public MyAdapter(Context context, int textViewResourceId, List<CountryOwned> objects){
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable final View convertView, @NonNull ViewGroup parent) {
        final CountryOwned countryOwned = getItem(position);
        final View view;
        final ViewHolder viewHolder;
        if(convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.cityName = view.findViewById(R.id.owned_cityname_text);
            viewHolder.DeleteView = view.findViewById(R.id.delete_iv);
            view.setTag(viewHolder);
        }else{
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.cityName.setText(countryOwned.getCountryName());
        viewHolder.DeleteView.setImageResource(R.mipmap.ic_clear_black_24dp);

        viewHolder.DeleteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                remove(countryOwned);
                countryOwned.delete();
            }
        });

        return view;
    }

    class ViewHolder{
        TextView cityName;
        ImageView DeleteView;
    }

}
