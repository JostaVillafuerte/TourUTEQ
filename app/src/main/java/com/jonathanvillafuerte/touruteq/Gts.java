package com.jonathanvillafuerte.touruteq;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingEvent;
import com.google.firebase.messaging.FirebaseMessaging;
import com.jonathanvillafuerte.touruteq.Model.MyResponse;
import com.jonathanvillafuerte.touruteq.Model.Notification;
import com.jonathanvillafuerte.touruteq.Model.Sender;
import com.jonathanvillafuerte.touruteq.Remote.APIService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Gts extends IntentService {
    private static final String TAG = Gts.class.getSimpleName();
    public static final int GEOFENCE_NOTIFICATION_ID = 0;
    private SharedPreferences sharedPref;

    public Gts() {
        super(TAG);
    }
    public String asunto="NOTIFICACION DE GEOFENCE!";
    public APIService mService = Common.GETFCMClient();
    Geofencing geofencing = new Geofencing();
    private SharedPreferences.Editor editor;




    @Override
    public void onStart( Intent intent, int startId) {
        super.onStart(intent, startId);
        sharedPref = getSharedPreferences("Preferencias", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("boton", "desactivar");
        editor.commit();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError() ) {
            String errorMsg = getErrorString(geofencingEvent.getErrorCode() );
            Log.e( TAG, errorMsg );
            return;    }
        int geoFenceTransition = geofencingEvent.getGeofenceTransition();
        if ( geoFenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
                geoFenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT ) {
            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();
            String geofenceTransitionDetails = getGeofenceTransitionDetails(geoFenceTransition, triggeringGeofences );
            sendNotification( geofenceTransitionDetails );
        }

        if (geoFenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER){
            editor = sharedPref.edit();
            editor.putString("boton", "activar");
            editor.commit();
        }
        if (geoFenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT){
            editor = sharedPref.edit();
            editor.putString("boton", "desactivar");
            editor.commit();
        }

    }

    private static String getErrorString(int errorCode) {
        switch (errorCode) {
            case GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE:
                return "GeoFence not available";
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES:
                return "Too many GeoFences";
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS:
                return "Too many pending intents";
            default:
                return "Unknown error.";
        }
    }

    private String getGeofenceTransitionDetails(int geoFenceTransition, List<Geofence> triggeringGeofences) {
        ArrayList<String> triggeringGeofencesList = new ArrayList<>();
        for ( Geofence geofence : triggeringGeofences ) {
            triggeringGeofencesList.add( geofence.getRequestId() );
        }

        String status = null;
        if ( geoFenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER )
        {
            status = "Entrando en ";
        }

        else if ( geoFenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT )
        {
            status = "Saliendo de ";
        }
        Toast.makeText(getApplicationContext(),status + TextUtils.join( ", ", triggeringGeofencesList),
                Toast.LENGTH_LONG).show();
        return status + TextUtils.join( ", ", triggeringGeofencesList);
    }



    private void sendNotification( String msg) {

        String user = Geofencing.user;
        String content = user;

        FirebaseMessaging.getInstance().subscribeToTopic("touruteq");
        mService = Common.GETFCMClient();

        try {
            Notification notification = new Notification(msg,content);
            Sender sender = new Sender("/topics/touruteq", notification);
            mService.sendNotification(sender)
                    .enqueue(new Callback<MyResponse>() {

                        @Override
                        public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                            if (response.body().success == 1) {
                            } else {
                                //Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<MyResponse> call, Throwable t) {
                            Log.e("Error", t.getMessage());
                        }
                    });
        }
        catch (Exception ex){
        }
    }
}
