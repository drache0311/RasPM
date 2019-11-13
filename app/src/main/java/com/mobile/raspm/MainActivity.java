package com.mobile.raspm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private static String IP_ADDRESS = "ec2-15-164-153-137.ap-northeast-2.compute.amazonaws.com/phpdb";
    private static String TAG = "phptest";

    private LinearLayout layout;
    private ArrayList<PersonalData> mArrayList;
    private UsersAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private String mJsonString;
    Button intent_btn; // 즐겨찾기로 이동할 버튼
    private String delCity;
    boolean abc = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("나만의 즐겨찾기");
        layout = findViewById(R.id.mainMain);
        layout.setBackgroundResource(R.color.colorBlue);
        mRecyclerView = (RecyclerView) findViewById(R.id.listView_main_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mArrayList = new ArrayList<>();

        mAdapter = new UsersAdapter(this, mArrayList);
        mRecyclerView.setAdapter(mAdapter);

        // Sub로 넘어가는 버튼
        intent_btn = findViewById(R.id.button_go);
        intent_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //인텐트 선언 -> ㅕㄴ재액티비티, 넘어갈 액티비티
                // 11.05 원래는 SubActivity로 이동해야하는데 테스트를 위해 FrontActivity로 이동시켜놨음!!!!!!!!!!!!!!!!
                Intent intent = new Intent(MainActivity.this,SubActivity.class);
                //인텐트 시랭
                startActivity(intent);
            }
        });

        mArrayList.clear();
        mAdapter.notifyDataSetChanged();

        MainActivity.GetData task = new MainActivity.GetData();
        task.execute( "http://" + IP_ADDRESS + "/getBook.php", "");

    }



    private class GetData extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(MainActivity.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            Log.d(TAG, "response - " + result);

            if (result == null){

            }
            else {

                mJsonString = result;
                showResult();
            }
        }


        @Override
        protected String doInBackground(String... params) {

            String serverURL = params[0];
            String postParameters = params[1];


            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);

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
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }

                bufferedReader.close();

                return sb.toString().trim();


            } catch (Exception e) {

                Log.d(TAG, "GetData : Error ", e);
                errorString = e.toString();

                return null;
            }

        }
    }


    private void showResult(){

        String TAG_JSON="webnautes";
        String TAG_CITYNAME = "cityName";
        String TAG_pm10Value = "pm10Value";
        String TAG_pm25Value ="pm25Value";
        String TAG_display = "display";

        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);

                String cityName = item.getString(TAG_CITYNAME);
                String pm10Value = item.getString(TAG_pm10Value);
                String pm25Value = item.getString(TAG_pm25Value);
                int display = item.getInt(TAG_display);

                PersonalData personalData = new PersonalData();

                personalData.setMember_cityName(cityName);
                personalData.setMember_pm10Value(pm10Value);
                personalData.setMember_pm25Value(pm25Value);
                personalData.setMember_display(display);

                mArrayList.add(personalData);
                mAdapter.notifyDataSetChanged();
            }



        } catch (JSONException e) {

            Log.d(TAG, "showResult : ", e);
        }

    }
}