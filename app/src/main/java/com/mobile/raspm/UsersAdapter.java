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
public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.CustomViewHolder> {

    private ArrayList<PersonalData> mList = null;
    private Activity context = null;
    static String currentName; // 여기다가 front액티비티의 address값을 넣어볼까?
    static int currentPm10;
    static int currentPm25;
    private static String IP_ADDRESS = "ec2-15-164-153-137.ap-northeast-2.compute.amazonaws.com/phpdb";
    private static String TAG = "phptest";


    public UsersAdapter(Activity context, ArrayList<PersonalData> list) {
        this.context = context;
        this.mList = list;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        public final View mview;
        protected TextView cityName;
        protected TextView pm10Value;
        protected TextView pm25Value;
        protected TextView pmState;
        protected int display;
        protected String currentName;
        protected int currentPm10;
        protected int currentPm25;

        public CustomViewHolder(View view) {
            super(view);
            mview = view;
            this.cityName = (TextView) view.findViewById(R.id.textView_list_cityName);
            this.pm10Value = (TextView) view.findViewById(R.id.textView_list_pm10Value);
            this.pm25Value = (TextView) view.findViewById(R.id.textView_list_pm25Value);
            this.pmState = view.findViewById(R.id.textView_list_pmState);
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
        viewholder.currentName = (mList.get(position).getMember_cityName());
        viewholder.currentPm10 = Integer.parseInt(mList.get(position).getMember_pm10Value());
        viewholder.currentPm25 = Integer.parseInt(mList.get(position).getMember_pm25Value());

        currentName = viewholder.currentName;
        currentPm10 = viewholder.currentPm10;
        currentPm25 = viewholder.currentPm25;


        if(currentPm10<=30){
            viewholder.pmState.setText("좋음");
        }else if(currentPm10<=80){
            viewholder.pmState.setText("보통");
        }else if(currentPm10<=150){
            viewholder.pmState.setText("나쁨");
        }else{
            viewholder.pmState.setText("매우 나쁨");
        }



        // 즐겨찾기 추가할 지 삭제할 지 작동하는 곳
        // 서브에서 (강서)구 를 클릭하면 mList.get(position).getMember_cityName() 으로 강서구라는 String값을 가져옴
        viewholder.cityName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show(v, position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }

    // 업데이트 하는 곳
    private class UpdateData extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            String serverURL = (String) params[0];
            String bookName = (String) params[1];
            String postParameters = "name=" + bookName;


            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "POST response code - " + responseStatusCode);

                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }


                bufferedReader.close();


                return sb.toString();


            } catch (Exception e) {

                Log.d(TAG, "UpdateData: Error ", e);

                return new String("Error: " + e.getMessage());
            }

        }
    }

    // 알림창 뜬 후 Yes면 삭제 , No면은 삭제안함
    void show(final View v, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        if (mList.get(position).getMember_display() == 0) { // display가 0이면 밑의 문 실행 !!!
            builder.setTitle("즐겨찾기 추가");
            builder.setMessage("추가 하시겠습니까?");
            // Yes 클릭시
            builder.setPositiveButton("예",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // display를 1로 바꾸어 즐겨찾기에 추가

                            UpdateData task = new UpdateData();
                            task.execute("http://" + IP_ADDRESS + "/updateDb.php", mList.get(position).getMember_cityName());

                            Toast.makeText(v.getContext(), "추가 했습니다.", Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(v.getContext(), MainActivity.class);
                            v.getContext().startActivity(intent);
                        }
                    });
            // No 클릭 시
            builder.setNegativeButton("아니오",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(v.getContext(), "취소 했습니다.", Toast.LENGTH_LONG).show();
                        }
                    });
            builder.show();
        } else {
            builder.setTitle("즐겨찾기 삭제");
            builder.setMessage("삭제하시겠습니까?");
            // Yes 클릭시
            builder.setPositiveButton("예",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // display를 0으로 바꿈
                            Toast.makeText(v.getContext(), "삭제 했습니다.", Toast.LENGTH_LONG).show();

                            UpdateData task = new UpdateData();
                            task.execute("http://" + IP_ADDRESS + "/deleteDb.php", mList.get(position).getMember_cityName());

                            Intent intent = new Intent(v.getContext(), MainActivity.class);
                            v.getContext().startActivity(intent);
                        }
                    });
            // No 클릭 시
            builder.setNegativeButton("아니오",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(v.getContext(), "취소 했습니다.", Toast.LENGTH_LONG).show();
                        }
                    });
            builder.show();
        }
    }
}

