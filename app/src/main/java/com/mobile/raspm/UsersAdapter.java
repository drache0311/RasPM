package com.mobile.raspm;

import android.app.Activity;
import android.content.Context;
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
public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.CustomViewHolder>  {

    private ArrayList<PersonalData> mList = null;
    private Activity context = null;
    static String name;
    private static String IP_ADDRESS = "ec2-15-164-153-137.ap-northeast-2.compute.amazonaws.com/phpdb";
    private static String TAG = "phptest";


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
                    // 아니면 intent에 putExtra로 값(mList의cityname)을 넣어서 다른 Activity에서 할것인지 와서 정하자
                    UsersAdapter.name = mList.get(position).getMember_cityName();

                    UpdateData task = new UpdateData();
                    task.execute( "http://" + IP_ADDRESS + "/updateDb.php", "");

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

    // 업데이트 하는 곳 인데 업데이트 안됨 집가서 확인 ㄱㄱ
    private class UpdateData extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            String name = UsersAdapter.name;

            String serverURL = (String)params[0];
            String postParameters = "name=" + name;


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
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while((line = bufferedReader.readLine()) != null){
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


}


