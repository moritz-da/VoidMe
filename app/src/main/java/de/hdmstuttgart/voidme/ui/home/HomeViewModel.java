package de.hdmstuttgart.voidme.ui.home;

import android.location.Location;
import android.os.Handler;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> latitude;
    private MutableLiveData<String> longitude;
    private MutableLiveData<String> altitude;
    private MutableLiveData<String> accuracy;
    private Location location;

    public HomeViewModel() {
        //TODO this.location = HomeFragment.updateGPS();
        updateLiveData();
    }

    public void startTracking(){
        // Init
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                //TODO this.location = HomeFragment.updateGPS();

                updateLiveData();
                handler.postDelayed(this, 2000);
            }
        };

        //Start
        handler.postDelayed(runnable, 2000);
    }

    private void updateLiveData(){
        latitude = new MutableLiveData<>();
        latitude.setValue(String.valueOf(location.getLatitude()));

        longitude = new MutableLiveData<>();
        longitude.setValue(String.valueOf(location.getLongitude()));

        altitude = new MutableLiveData<>();
        altitude.setValue(String.valueOf(location.getAltitude()));

        accuracy = new MutableLiveData<>();
        accuracy.setValue(String.valueOf(location.getAccuracy()));
    }

    public MutableLiveData<String> getLatitude() {
        return latitude;
    }

    public MutableLiveData<String> getLongitude() {
        return longitude;
    }

    public MutableLiveData<String> getAltitude() {
        return altitude;
    }

    public MutableLiveData<String> getAccuracy() {
        return accuracy;
    }
}