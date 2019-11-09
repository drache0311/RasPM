package com.mobile.raspm;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

 public class FrontActivity extends AppCompatActivity {

    private LinearLayout layout;
    private RecyclerView mRecyclerView; // 위에 보여줄 현재 지역구
    private TextView pm10Value; // pm10 담음
    private TextView pmText; // pm2.5 담음
    private TextView currentTime; // 현재시간 담음
    private String mJsonString;
    private ArrayList<PersonalData> mArrayList;
     private UsersAdapter mAdapter;
     private ImageView faceIcon; // 얼굴
    String address; // OO구 담을 변수
    LatLng currentPosition;
     long now ;
     Date date;



     private static String IP_ADDRESS = "ec2-15-164-153-137.ap-northeast-2.compute.amazonaws.com/phpdb";
     private static String TAG = "phptest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front);

        layout = findViewById(R.id.frontMain);
        mRecyclerView = (RecyclerView) findViewById(R.id.currentCity);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        pm10Value = findViewById(R.id.pm10Value);
        pmText = findViewById(R.id.pmText);
        faceIcon = findViewById(R.id.smile);
        List<Address> list = null;
        mArrayList = new ArrayList<>();
        mAdapter = new UsersAdapter(this, mArrayList);
        mRecyclerView.setAdapter(mAdapter);
        // txtResult = (TextView)findViewById(R.id.txtResult);
        currentTime = findViewById(R.id.currentTime);




        final Geocoder geocoder = new Geocoder(this);
        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

     //   mArrayList.clear();
        mAdapter.notifyDataSetChanged();


        GetData task = new GetData();
        task.execute( "http://" + IP_ADDRESS + "/getCurrent.php", "구로구");



        // 현재시간 스레드 시작
        t.start();










                if ( Build.VERSION.SDK_INT >= 23 &&
                        ContextCompat.checkSelfPermission( getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
                    ActivityCompat.requestPermissions( FrontActivity.this, new String[] {  android.Manifest.permission.ACCESS_FINE_LOCATION  },
                            0 );
                }
                else{
                        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        String provider = location.getProvider();
                        double longitude = location.getLongitude();
                        double latitude = location.getLatitude();
                        double altitude = location.getAltitude();

                    try {
                        list = geocoder.getFromLocation(latitude,longitude, 4);
                    }catch (IOException e){
                        e.printStackTrace();
                        Log.e("test","입출력 오류 - 서버에서 주소변환시 에러발생");
                    }
                    // 이 자리에 현재위치 지역구 이름 넣을꺼임
                    // AddressLine(0) 이 서울시 OO구 인데 이걸 OO구만 짜르자
                 /*   currentCity.setText("위도 : " + longitude + "\n" +
                            "경도 : " + latitude + "\n" +
                            "변환주소 : " + list.get(0).getAddressLine(0) + "\n" +
                            "직접 쓴 구로구 : " + address);
                  */



                    // 미세먼지 수치에 따라 표정 아이콘 변화시키기
                    if(UsersAdapter.currentPm10<=30){ // 미세먼지 좋음
                        faceIcon.setImageResource(R.drawable.over0);
                        layout.setBackgroundResource(R.color.colorBlue);
                        pmText.setText("좋음");
                        pm10Value.setText("미세먼지 : "+ UsersAdapter.currentPm10);
                    }else if(UsersAdapter.currentPm10<=80){ // 미세먼지 보통
                        faceIcon.setImageResource(R.drawable.over180);
                        layout.setBackgroundResource(R.color.colorGreen);
                        pmText.setText("보통");
                        pm10Value.setText("미세먼지 : "+ UsersAdapter.currentPm10);
                    }else if(UsersAdapter.currentPm10<=150){ // 미세먼지 나쁨
                        faceIcon.setImageResource(R.drawable.over150);
                        layout.setBackgroundResource(R.color.colorYellow);
                        pmText.setText("나쁨");
                        pm10Value.setText("미세먼지 : "+ UsersAdapter.currentPm10);
                    }else{ // 미세먼지 매우나쁨
                        faceIcon.setImageResource(R.drawable.over180);
                        layout.setBackgroundResource(R.color.colorOrange);
                        pmText.setText("매우나쁨");
                        pm10Value.setText("미세먼지 : "+ UsersAdapter.currentPm10);
                    }

                    // 여기서 위도,경도를 구할 수 있는데 그걸로 서울시 OO구 (지역구)를 가져와서
                    // 이 화면에서 바로 그 지역구의 미세먼지를 보여주자
                    // + 미세먼지 값에 따른 아이콘의 표정변화 추가 (이미지)
                    // + 밑에는 버튼 클릭시 라즈베리로 이동!!!
                    //
                    //     Example
                    //              구로구
                    //    이미지-> ( *.* )
                    //              보통   ( 좋음,보통,나쁨,매우나쁨 4개)
                    //         미세먼지 : OO
                    //   ---------------------------
                    //   ┌--------┐┌--------┐
                    //   ㅣ라즈베리ㅣㅣ라즈베리ㅣ   <-- 왼쪽엔 진짜 라즈베리 / 2번째엔 가짜 라즈베리
                    //   └--------┘└--------┘

                    /*
                    txtResult.setText("위치정보 : " + provider + "\n" +
                            "위도 : " + longitude + "\n" +
                            "경도 : " + latitude + "\n" +
                            "고도  : " + altitude);
                     */
                    // location에 위도경도 담기
                    //location1.setLatitude(latitude);
                    //location1.setLongitude(longitude);

                    // location의 위도경도를 하나로 담기
      //              currentPosition = new LatLng(latitude,longitude);

                    // address에 주소 담기
        //            address = getCurrentAddress(currentPosition);

          //          Toast.makeText(this, address, Toast.LENGTH_LONG).show();

                    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                            1000,
                            1,
                            gpsLocationListener);
                    lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                            1000,
                            1,
                            gpsLocationListener);
                }
    }
    final LocationListener gpsLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {

            String provider = location.getProvider();
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            double altitude = location.getAltitude();

            /*
            txtResult.setText("위치정보 : " + provider + "\n" +
                    "위도 : " + longitude + "\n" +
                    "경도 : " + latitude + "\n" +
                    "고도  : " + altitude);
             */
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onProviderDisabled(String provider) {
        }
    };

     private class GetData extends AsyncTask<String, Void, String> {

         ProgressDialog progressDialog;
         String errorString = null;

         @Override
         protected void onPreExecute() {
             super.onPreExecute();

             progressDialog = ProgressDialog.show(FrontActivity.this,
                     "Please Wait", null, true, true);
         }



         @Override
         protected void onPostExecute(String result) {
             super.onPostExecute(result);

             progressDialog.dismiss();
             Log.d(TAG, "response - result : " + result);

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
             String currentName = (String)params[1];
             String postParameters = "name=" + currentName;

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

     Thread t = new Thread() {
         @Override
         public void run() {
             try {
                 while (!isInterrupted()) {
                     Thread.sleep(1000);
                     runOnUiThread(new Runnable() {
                         @Override
                         public void run() {
                             updateYOURthing();
                         }
                     });
                 }
             } catch (InterruptedException e) {
             }
         }
     };
     private void updateYOURthing() {
         now = System.currentTimeMillis();
         date = new Date(now);
         SimpleDateFormat sdfnow = new SimpleDateFormat("yyyy-MM-dd HH:mm a");
         String strNow = sdfnow.format(date);
         currentTime.setText(strNow);
     }



/*
    // 위도경도를 주소로 변환해주는 메소드
    public String getCurrentAddress(LatLng latlng) {

        //지오코더... GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        List<Address> addresses;

        try {

            addresses = geocoder.getFromLocation(
                    latlng.latitude,
                    latlng.longitude,
                    1);
        } catch (IOException ioException) {
            //네트워크 문제
            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";

        }


        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";

        } else {
            Address address = addresses.get(0);
            return address.getAddressLine(0).toString();
        }

    }
 */

}
