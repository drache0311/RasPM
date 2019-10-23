package com.mobile.raspm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
    static String name;

    public UsersAdapter(Activity context, ArrayList<PersonalData> list) {
        this.context = context;
        this.mList = list;

    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        public  final View mview;
        protected TextView cityName;
        protected TextView pm10Value;
        protected TextView pm25Value;
        protected int display;

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
    public void onBindViewHolder(@NonNull final CustomViewHolder viewholder, final int position) {

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
                if(mList.get(position).getMember_display()==0) { // display가 0이면 밑의 문 실행 !!!
                    Toast.makeText(context, "선택한 값 : " + mList.get(position).getMember_cityName()+"디스플=="+mList.get(position).getMember_display(), Toast.LENGTH_SHORT).show();
                    // 여기서 바로 php파일 불러와서 display를 1로 변경할 것인지
                    // 아니면 intent에 putExtra로 값을 넣어서 다른 Activity에서 할것인지 와서 정하자
                    UsersAdapter.name = mList.get(position).getMember_cityName();
                    Intent intent = new Intent(v.getContext(), MainActivity.class);
                    v.getContext().startActivity(intent);
                }
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

