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
    double lat;             // ����
    double lon;             // �浵
    // �ּ� GPS ���� ������Ʈ �Ÿ� 10����
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    // �ּ� GPS ���� ������Ʈ �ð� �и������̹Ƿ� 1��
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;
    // GPS �Ŵ���
    protected LocationManager locationManager;
    // ���� GPS ��� ����
    private boolean isGPSEnabled = false;
    // ��Ʈ��ũ ��� ����
    private boolean isNetworkEnabled = false;
    // GPS ���°�
    private boolean isGetLocation = false;

    // ������
    public GpsInfo(Context context) {
        this.mContext = context;
        getLocation();
    }

    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext
                    .getSystemService(Context.LOCATION_SERVICE);

            // GPS ���� ��������
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // ���� ��Ʈ��ũ ���� �� ��������
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // GPS �� ��Ʈ��ũ����� �������� ������ �ҽ� ����
            } else {

                // ��Ʈ��ũ ������ ���� ��ġ�� ��������
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    if (locationManager != null) {
                        // location �ʱ�ȭ
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            // ���� �浵 ����
                            lat = location.getLatitude();
                            lon = location.getLongitude();
                        }
                    }
                }

                // GPS ������ ���� ��ġ�� ��������
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

    // ���� ��ȯ
    public double GetLatitude(){
        return location.getLatitude();
    }

    // �浵 ��ȯ
    public double GetLongitude(){
        return location.getLongitude();
    }

    // ����L�浵 ������ ���� �浵�� ������ �Ҽ��� ����
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

    // ����, �浵 �����κ��� �ּ� ��ȯ
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
                    "Address : "+mAddress.getCountryName()+" "      // ��ġ ����
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