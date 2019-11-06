package com.mobile.raspm;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.model.LatLng;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

 public class FrontActivity extends AppCompatActivity {


 //   private TextView txtResult;
    private TextView currentCity; // 위에 보여줄 현재 지역구
    private TextView pm10Value;
    private TextView pm25Value;
    String address;
    LatLng currentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front);

        currentCity = findViewById(R.id.currentCity);
        pm10Value = findViewById(R.id.pm10Value);
        pm25Value = findViewById(R.id.pm25Value);
        // txtResult = (TextView)findViewById(R.id.txtResult);


        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


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


                    // 이 자리에 현재위치 지역구 이름 넣을꺼임
                    currentCity.setText("위치정보 : " + provider + "\n" +
                            "위도 : " + longitude + "\n" +
                            "경도 : " + latitude + "\n" +
                            "고도  : " + altitude);

                    pm10Value.setText("여기가 PM10자리");
                    pm25Value.setText("여기가 PM25자리");
                    // 여기서 위도,경도를 구할 수 있는데 그걸로 서울시 OO구 (지역구)를 가져와서
                    // 이 화면에서 바로 그 지역구의 미세먼지를 보여주자
                    // + 미세먼지 값에 따른 아이콘의 표정변화 추가 (이미지)
                    // + 밑에는 버튼 클릭시 라즈베리로 이동!!!
                    //
                    //     Example
                    //              구로구
                    //    이미지-> ( *.* )
                    //   미세먼지 15 : 초미세먼지 15
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
