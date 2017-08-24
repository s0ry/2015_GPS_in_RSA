package com.example.hyunmo.gps_in_rsa;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import java.io.IOException;
import java.util.List;

public class GpsInfo  implements LocationListener {

    private final Context mContext;
    Location location;      // GPS go
    double lat;             // 위도
    double lon;             // 경도
    // 최소 GPS 정보 업데이트 거리 10미터
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    // 최소 GPS 정보 업데이트 시간 밀리세컨이므로 1분
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;
    // GPS 매니저
    protected LocationManager locationManager;
    // 현재 GPS 사용 유무
    private boolean isGPSEnabled = false;
    // 네트워크 사용 유무
    private boolean isNetworkEnabled = false;
    // GPS 상태값
    private boolean isGetLocation = false;

    // 생성자
    public GpsInfo(Context context) {
        this.mContext = context;
        getLocation();
    }

    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext
                    .getSystemService(Context.LOCATION_SERVICE);

            // GPS 정보 가져오기
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // 현재 네트워크 상태 값 가져오기
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // GPS 와 네트워크사용이 가능하지 않을때 소스 구현
            } else {

                // 네트워크 정보로 부터 위치값 가져오기
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    if (locationManager != null) {
                        // location 초기화
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            // 위도 경도 저장
                            lat = location.getLatitude();
                            lon = location.getLongitude();
                        }
                    }
                }

                // GPS 정보로 부터 위치값 가져오기
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                lat = location.getLatitude();
                                lon = location.getLongitude();
                            }
                        }
                    }
                }
                this.isGetLocation = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;
    }

    // 위도 반환
    public double GetLatitude(){
        return location.getLatitude();
    }

    // 경도 반환
    public double GetLongitude(){
        return location.getLongitude();
    }

    // 위도L경도 값에서 위도 경도를 나누고 소수점 복구
    public void changeLocation(String n){
        String local[] = n.split("L");

        double tmp_lat, tmp_lon;
        tmp_lat = Double.parseDouble(local[0]);
        tmp_lon = Double.parseDouble(local[1]);

        double x = (double)(tmp_lat / 10000000);
        double y = (double)(tmp_lon / 10000000);

        lat = x;
        lon = y;
    }

    // 위도, 경도 정보로부터 주소 변환
    public String GetAddress(){
        Geocoder geoCoder = new Geocoder(mContext);
        String mAddressStr = null;
        List<Address> addresses = null;
        try {
            addresses = geoCoder.getFromLocation(lat, lon, 5);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(addresses.size()>0){
            Address mAddress = addresses.get(0);
            mAddressStr = "Latitude/Longitude : " + lat + "/" + lon + "\n" +
                    "Address : "+mAddress.getCountryName()+" "      // 위치 정보
                    +mAddress.getLocality()+" "
                    +mAddress.getThoroughfare()+" "
                    +mAddress.getFeatureName();
        }
        return mAddressStr;
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
    }
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
    }
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub
    }
}