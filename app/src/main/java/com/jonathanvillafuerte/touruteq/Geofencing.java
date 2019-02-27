package com.jonathanvillafuerte.touruteq;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.iid.FirebaseInstanceId;
import com.jonathanvillafuerte.touruteq.Remote.APIService;

public class Geofencing extends AppCompatActivity
        implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        OnMapReadyCallback,
        ResultCallback<Status> {

    public static String user;

    private APIService mService;
    private static final String TAG = Geofencing.class.getSimpleName();
    private Button btnEnviar;
    private GoogleMap map;
    private GoogleApiClient googleApiClient;
    private Location lastLocation;

    private TextView textLat, textLong;

    private MapFragment mapFragment;
    public FloatingActionButton fab;

    private static final long GEO_DURATION = 60 * 60 * 1000;

    private static final String NOTIFICATION_MSG = "NOTIFICATION MSG";

    public static Intent makeNotificationIntent(Context context, String msg) {
        Intent intent = new Intent(context, Geofencing.class);
        intent.putExtra( NOTIFICATION_MSG, msg );
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geofencing);

        textLat = (TextView) findViewById(R.id.lat);
        textLong = (TextView) findViewById(R.id.lon);
        TextView textView = (TextView) findViewById(R.id.txtError);
        Common.currentToken = FirebaseInstanceId.getInstance().getToken();
        mService = Common.GETFCMClient();

        Bundle bundle = this.getIntent().getExtras();
        textView.setText(bundle.getString("Nombre"));
        user = bundle.getString("Nombre");
        initGMaps();

        createGoogleApi();

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), IdentificacionObjetos.class);
                startActivity(intent);
            }
        });


        SharedPreferences sharedPref = getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
        String clave = sharedPref.getString("boton", "nada");
        if("activar".equals(clave)) {
            fab.setEnabled(true);
        }else {
            fab.setEnabled(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPref = getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
        String clave = sharedPref.getString("boton", "nada");
        if("activar".equals(clave)) {
            fab.setEnabled(true);
        }else {
            fab.setEnabled(false);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences sharedPref = getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
        String clave = sharedPref.getString("boton", "nada");
        if("activar".equals(clave)) {
            fab.setEnabled(true);
        }else {
            fab.setEnabled(false);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "onConnected()");
        getLastKnownLocation();
        String GEOFENCE_REQ_ID;
        float GEOFENCE_RADIUS;


        GEOFENCE_REQ_ID = "Facultad de Ciencias Empresariales";
        GEOFENCE_RADIUS = 25.0f;
        Double l1 = Double.parseDouble("-1.0121647");
        Double l2 = Double.parseDouble("-79.470063");
        markerForGeofence(new LatLng(l1, l2),GEOFENCE_REQ_ID,GEOFENCE_RADIUS);
        Toast.makeText(this, "GEOFENCING "+GEOFENCE_REQ_ID+" CREADO", Toast.LENGTH_SHORT).show();

        GEOFENCE_REQ_ID = "Facultad de Ciencias Agrarias";
        l1 = Double.parseDouble("-1.0129049");
        l2 = Double.parseDouble("-79.469296");
        markerForGeofence(new LatLng(l1, l2),GEOFENCE_REQ_ID,GEOFENCE_RADIUS);
        Toast.makeText(this, "GEOFENCING "+GEOFENCE_REQ_ID+" CREADO", Toast.LENGTH_SHORT).show();

        GEOFENCE_REQ_ID = "Facultad de Ciencias Ambientales";
        l1 = Double.parseDouble("-1.0126903");
        l2 = Double.parseDouble("-79.471026");
        markerForGeofence(new LatLng(l1, l2),GEOFENCE_REQ_ID,GEOFENCE_RADIUS);
        Toast.makeText(this, "GEOFENCING "+GEOFENCE_REQ_ID+" CREADO", Toast.LENGTH_SHORT).show();

        GEOFENCE_REQ_ID = "Facultad de Ciencias de la Ingeniería";
        l1 = Double.parseDouble("-1.0125938");
        l2 = Double.parseDouble("-79.470618");
        markerForGeofence(new LatLng(l1, l2),GEOFENCE_REQ_ID,GEOFENCE_RADIUS);
        Toast.makeText(this, "GEOFENCING "+GEOFENCE_REQ_ID+" CREADO", Toast.LENGTH_SHORT).show();

        GEOFENCE_REQ_ID = "Unidad de TICS";
        l1 = Double.parseDouble("-1.012409");
        l2 = Double.parseDouble("-79.470364");
        markerForGeofence(new LatLng(l1, l2),GEOFENCE_REQ_ID,GEOFENCE_RADIUS);
        Toast.makeText(this, "GEOFENCING "+GEOFENCE_REQ_ID+" CREADO", Toast.LENGTH_SHORT).show();
    }

    private void createGoogleApi() {
        Log.d(TAG, "createGoogleApi()");
        if ( googleApiClient == null ) {
            googleApiClient = new GoogleApiClient.Builder( this )
                    .addConnectionCallbacks( this )
                    .addOnConnectionFailedListener( this )
                    .addApi( LocationServices.API )
                    .build();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        googleApiClient.disconnect();
    }

    private final int REQ_PERMISSION = 999;
    private boolean checkPermission() {
        Log.d(TAG, "checkPermission()");
        return (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED );
    }
    private void askPermission() {
        Log.d(TAG, "askPermission()");
        ActivityCompat.requestPermissions(
                this,
                new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                REQ_PERMISSION
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult()");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch ( requestCode ) {
            case REQ_PERMISSION: {
                if ( grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED ){
                    getLastKnownLocation();
                } else {
                    permissionsDenied();
                }
                break;
            }
        }
    }

    private void permissionsDenied() {
        Log.w(TAG, "permissionsDenied()");
        // TODO close app and warn user
    }

    private void initGMaps(){
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady()");
        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.getUiSettings().setZoomControlsEnabled(true);
    }

    private LocationRequest locationRequest;
    private final int UPDATE_INTERVAL =  1000;
    private final int FASTEST_INTERVAL = 900;

    private void startLocationUpdates(){
        Log.i(TAG, "startLocationUpdates()");
        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);
        if ( checkPermission() )
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged ["+location+"]");
        lastLocation = location;
        writeActualLocation(location);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.w(TAG, "onConnectionSuspended()");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.w(TAG, "onConnectionFailed()");
    }

    @Override
    public void onResult(@NonNull Status status) {
        Log.i(TAG, "onResult: " + status);
    }

    private void getLastKnownLocation() {
        Log.d(TAG, "getLastKnownLocation()");
        if ( checkPermission() ) {
            lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if ( lastLocation != null ) {
                Log.i(TAG, "LasKnown location. " +
                        "Long: " + lastLocation.getLongitude() +
                        " | Lat: " + lastLocation.getLatitude());
                writeLastLocation();
                startLocationUpdates();
            } else {
                Log.w(TAG, "No location retrieved yet");
                startLocationUpdates();
            }
        }
        else askPermission();
    }


    private void writeActualLocation(Location location) {
        textLat.setText( "Lat: " + location.getLatitude() );
        textLong.setText( "Long: " + location.getLongitude() );
        markerLocation(new LatLng(location.getLatitude(), location.getLongitude()));
    }

    private void writeLastLocation() {
        writeActualLocation(lastLocation);
    }

    private Marker locationMarker;
    private void markerLocation(LatLng latLng) {
        Log.i(TAG, "markerLocation("+latLng+")");
        String title = "Mi Ubicacion es: "+latLng.latitude + ", " + latLng.longitude;
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_person))
                .title(title);
        if ( map!=null ) {
            if ( locationMarker != null )
                locationMarker.remove();
            locationMarker = map.addMarker(markerOptions);
            float zoom = 19f;
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, zoom);
            map.animateCamera(cameraUpdate);
        }
    }

    private Marker geoFenceMarker;
    private void markerForGeofence(LatLng latLng,String nombregeofen,float radio ) {
        Log.i(TAG, "markerForGeofence("+latLng+")");
        String title = ""+nombregeofen+":"+latLng.latitude + ", " + latLng.longitude;
        // Define marker options
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_faculty))
                .title(title);
        if ( map!=null ) {


            geoFenceMarker = map.addMarker(markerOptions);
            drawGeofence(geoFenceMarker,radio);
            startGeofence(latLng,nombregeofen,radio);
        }
    }


    private void startGeofence(LatLng latLng,String nombregeofen,float radio ) {
        Log.i(TAG, "startGeofence()");
        if( geoFenceMarker != null ) {
            Geofence geofence = createGeofence( latLng,nombregeofen, radio );
            GeofencingRequest geofenceRequest = createGeofenceRequest( geofence );
            addGeofence( geofenceRequest );
        } else {
            Log.e(TAG, "Geofence marker is null");
        }
    }

    private Geofence createGeofence( LatLng latLng,String nombregeo, float radius ) {
        Log.d(TAG, "createGeofence");
        return new Geofence.Builder()
                .setRequestId(nombregeo)
                .setCircularRegion( latLng.latitude, latLng.longitude, radius)
                .setExpirationDuration( GEO_DURATION )
                .setTransitionTypes( Geofence.GEOFENCE_TRANSITION_ENTER
                        | Geofence.GEOFENCE_TRANSITION_EXIT )
                .build();
    }

    // Create a Geofence Request
    private GeofencingRequest createGeofenceRequest( Geofence geofence ) {
        Log.d(TAG, "createGeofenceRequest");
        return new GeofencingRequest.Builder()
                .setInitialTrigger( GeofencingRequest.INITIAL_TRIGGER_ENTER )
                .addGeofence( geofence )
                .build();
    }

    private PendingIntent geoFencePendingIntent;
    private final int GEOFENCE_REQ_CODE = 0;
    private PendingIntent createGeofencePendingIntent() {
        Log.d(TAG, "createGeofencePendingIntent");
        if ( geoFencePendingIntent != null )
            return geoFencePendingIntent;

        Intent intent = new Intent( this, Gts.class);
        return PendingIntent.getService(
                this, GEOFENCE_REQ_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT );
    }

    private void addGeofence(GeofencingRequest request) {
        Log.d(TAG, "addGeofence");
        if (checkPermission())
            LocationServices.GeofencingApi.addGeofences(
                    googleApiClient,
                    request,
                    createGeofencePendingIntent()
            ).setResultCallback(this);
    }

    private Circle geoFenceLimits;
    private void drawGeofence(Marker geoFenceMarker, float radio) {
        Log.d(TAG, "drawGeofence()");

        CircleOptions circleOptions = new CircleOptions()
                .center( geoFenceMarker.getPosition())
                .strokeColor(Color.argb(50, 70,70,70))
                .fillColor( Color.argb(100, 150,150,150) )
                .radius( radio );
        geoFenceLimits = map.addCircle( circleOptions );
    }
}
