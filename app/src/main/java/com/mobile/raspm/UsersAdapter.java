package com.mobile.raspm;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
//implements View.OnClickListener 맨 밑의 onClick 쓰려면 이거 추가
public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.CustomViewHolder>  {

    private ArrayList<PersonalData> mList = null;
    private Activity context = null;


    public UsersAdapter(Activity context, ArrayList<PersonalData> list) {
        this.context = context;
        this.mList = list;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        public  final View mview;
        protected TextView cityName;
        protected TextView pm10Value;
        protected TextView pm25Value;


        public CustomViewHolder(View view) {
            super(view);
            mview=view;
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
    public void onBindViewHolder(@NonNull CustomViewHolder viewholder, final int position) {

        // 값 설정 ( set )
        viewholder.cityName.setText(mList.get(position).getMember_cityName());
        viewholder.pm10Value.setText(mList.get(position).getMember_pm10Value());
        viewholder.pm25Value.setText(mList.get(position).getMember_pm25Value());

        // 클릭리스너
        //viewholder.cityName.setOnClickListener(this);

        // (강서)구 를 클릭하면 mList.get(position).getMember_cityName() 으로 강서구라는 String값을 가져옴
        viewholder.cityName.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Toast.makeText(context, "선택한 값 : " + mList.get(position).getMember_cityName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }

/*
    // Click @@@@@@@@@@@  하면은 똑같은 값만 나온다 구글에 리사이클러뷰 아이템클릭 쳐서 내부 글자 따오는거 찾아보자
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textView_list_cityName:
                Toast.makeText(context, "TITLE : " + R.id.textView_list_cityName, Toast.LENGTH_SHORT).show();

                break;

        }
    }
*/

}

