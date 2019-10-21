package com.mobile.raspm;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.CustomViewHolder> {

    private ArrayList<PersonalData> mList = null;
    private Activity context = null;


    public UsersAdapter(Activity context, ArrayList<PersonalData> list) {
        this.context = context;
        this.mList = list;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView cityName;
        protected TextView pm10Value;
        protected TextView pm25Value;


        public CustomViewHolder(View view) {
            super(view);
            this.cityName = (TextView) view.findViewById(R.id.textView_list_cityName);
            this.pm10Value = (TextView) view.findViewById(R.id.textView_list_pm10Value);
            this.pm25Value = (TextView) view.findViewById(R.id.textView_list_pm25Value);
        }
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder viewholder, int position) {

        viewholder.cityName.setText(mList.get(position).getMember_cityName());
        viewholder.pm10Value.setText(mList.get(position).getMember_pm10Value());
        viewholder.pm25Value.setText(mList.get(position).getMember_pm25Value());
    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }

}

