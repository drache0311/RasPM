package com.mobile.raspm;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
//implements View.OnClickListener 맨 밑의 onClick 쓰려면 이거 추가
public class RaspAdapter extends RecyclerView.Adapter<RaspAdapter.CustomViewHolder> {

    private ArrayList<RaspData> mList = null;
    private Activity context = null;
    private static String IP_ADDRESS = "ec2-15-164-153-137.ap-northeast-2.compute.amazonaws.com/phpdb";
    private static String TAG = "phptest";
    private double raspLatitude;
    private double raspLongitude;
    private int raspPm10;
    private int raspPm25;


    public RaspAdapter(Activity context, ArrayList<RaspData> list) {
        this.context = context;
        this.mList = list;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        public final View mview;

        protected double latitude;
        protected double longitude;
        protected int pm10;
        protected int pm25;

        public CustomViewHolder(View view) {
            super(view);
            mview = view;
        }
    }

// 여기 레이아웃이 item_list인데 map액티비로 바꿔야 하는지 나주엥 확인해보자
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }



    @Override
    public void onBindViewHolder(@NonNull final CustomViewHolder viewholder, final int position) {


        // 값 설정 ( set )
        viewholder.latitude = (mList.get(position).getMember_latitude());
        viewholder.longitude = (mList.get(position).getMember_longitude());
        viewholder.pm10 = (mList.get(position).getMember_pm10());
        viewholder.pm25 = (mList.get(position).getMember_pm25());

        raspLatitude = viewholder.latitude;
        raspLongitude = viewholder.longitude;
        raspPm10 = viewholder.pm10;
        raspPm25 = viewholder.pm25;


    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }
}

