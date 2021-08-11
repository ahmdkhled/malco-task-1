package com.malcoo.malcotask1.Utils;

import android.graphics.Color;
import android.location.Location;
import android.view.View;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.malcoo.malcotask1.R;
import com.malcoo.malcotask1.databinding.ActivityMapsBinding;

public class MapUtil {
    Circle circle;
    Marker lastMarker;



    // add marker to google map
    public void addCurrentLocationMarker(GoogleMap mMap, LatLng coordinates,String... title){
        if (lastMarker!=null) lastMarker.remove();
        MarkerOptions markerOptions=new MarkerOptions();
        markerOptions.position(coordinates);
        if (title.length>0)
        markerOptions.title(title[0]);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
        lastMarker=mMap.addMarker(markerOptions);


        mMap.animateCamera( CameraUpdateFactory.newLatLngZoom(coordinates, 12f));

    }

    // delete old marker from map view
    public void clearLastMarker(){
        lastMarker.remove();
    }


    //function to add circle surrounding plcaes like ware house
    public void drawCircle(double radius,LatLng place, GoogleMap map){
        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(place);
        circleOptions.radius(radius);
        circleOptions.strokeColor(Color.BLACK);
        circleOptions.fillColor(0x30000000);
        circleOptions.strokeWidth(1);
        circle=map.addCircle(circleOptions);
    }

    // check if user is inside the radius of circle
    public boolean isInCircle(LatLng location){
        float[] distance=new float[2];
        Location.distanceBetween( location.latitude, location.longitude,
                circle.getCenter().latitude, circle.getCenter().longitude, distance);
        return  distance[0] <= circle.getRadius();

    }

    //get distance between two  LatLng coordinates (current location and warehouse)
    public static float getDistanceBetween(LatLng Currentlocation,LatLng destination){
        float[] distance=new float[2];
         Location.distanceBetween( Currentlocation.latitude, Currentlocation.longitude,
                destination.latitude, destination.longitude, distance);
         return distance[0];
    }

    // convert qr string value to lat and lng by splitting it
    public static LatLng getCoordinates(String coordinatesString){
        if (coordinatesString==null) return null;
        if (coordinatesString.contains(",")){
            String[] values=coordinatesString.split(",");
            try {
                double lat =  Double.parseDouble(values[0]);
                double lng = Double.parseDouble(values[1]);
                return new LatLng(lat,lng);
            }catch (Exception e){ return  null; }
        }
        return null;
    }

    // fake route for testing
    public static LatLng[] getFakeRoute(){

        return new LatLng[]{
                new LatLng(30.0780708,31.3228058),
                new LatLng(30.0560173,31.3169771),
                new LatLng(30.0752362,31.309582),
                new LatLng(30.0752362,31.3095829),
                new LatLng(30.0758615,31.3085954),
                new LatLng(30.0758615,31.3085954),
                new LatLng(30.0768828,31.3107631),
                new LatLng(30.0791337,31.3132559),
                new LatLng(30.0801342,31.3155079),
                new LatLng(30.0812388,31.3170975),
        };
    }

}
