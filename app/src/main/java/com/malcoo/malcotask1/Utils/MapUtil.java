package com.malcoo.malcotask1.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;

import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.malcoo.malcotask1.R;

import java.util.ArrayList;

public class MapUtil {
    Circle circle;
    Marker lastMarker;
    private Polyline lastPolyline;
    private  boolean firstTime=true;

    // add marker to google map
    public void addCurrentLocationMarker(GoogleMap mMap, LatLng coordinates,Context context){
        if (lastMarker!=null) lastMarker.remove();
        MarkerOptions markerOptions=new MarkerOptions();
        markerOptions.position(coordinates);

        markerOptions.icon(bitmapDescriptorFromVector(context,R.drawable.ic_current_location));
        lastMarker=mMap.addMarker(markerOptions);
        animate(mMap,coordinates);

    }
    public void animate(GoogleMap mMap, LatLng... coordinates){
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (LatLng point : coordinates) {
                builder.include(point);
            }
            LatLngBounds bounds = builder.build();
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds,50);
            mMap.animateCamera(cu);


    }

    public void animate(GoogleMap mMap, LatLng coordinates){
        if (firstTime){
            CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(coordinates,17);
            mMap.animateCamera(cu);
            firstTime=false;
        }
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

    public void drawPollyLine(GoogleMap map,LatLng currentLocation,LatLng destination){
        PolylineOptions options = new PolylineOptions();

        options.color( Color.parseColor("#FF38759E"));
        options.width( 20 );
        options.visible( true );
        options.add(currentLocation);
        options.add(destination);
        map.addPolyline(options);
    }

    public void drawPollyLine(GoogleMap map, Iterable<LatLng> points){
        if (lastPolyline!=null)lastPolyline.remove();
        PolylineOptions options = new PolylineOptions();

        options.color( Color.parseColor("#235CA9"));
        options.width( 13 );
        options.visible( true );
        options.addAll(points);
        lastPolyline=map.addPolyline(options);
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
    public static String fromCoordinates(LatLng coordinate){
        return coordinate.latitude+","+coordinate.longitude;
    }
    // convert encoded points string to list of latlng points
    public static ArrayList<LatLng> decodePolyPoints(String encodedPath){
        int len = encodedPath.length();

        final ArrayList<LatLng> path = new ArrayList<LatLng>();
        int index = 0;
        int lat = 0;
        int lng = 0;

        while (index < len) {
            int result = 1;
            int shift = 0;
            int b;
            do {
                b = encodedPath.charAt(index++) - 63 - 1;
                result += b << shift;
                shift += 5;
            } while (b >= 0x1f);
            lat += (result & 1) != 0 ? ~(result >> 1) : (result >> 1);

            result = 1;
            shift = 0;
            do {
                b = encodedPath.charAt(index++) - 63 - 1;
                result += b << shift;
                shift += 5;
            } while (b >= 0x1f);
            lng += (result & 1) != 0 ? ~(result >> 1) : (result >> 1);

            path.add(new LatLng(lat * 1e-5, lng * 1e-5));
        }

        return path;
    }

    public static BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
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
