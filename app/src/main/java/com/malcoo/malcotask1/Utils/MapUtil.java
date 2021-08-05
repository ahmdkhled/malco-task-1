package com.malcoo.malcotask1.Utils;

import android.graphics.Color;
import android.location.Location;
import android.util.Log;
import android.view.View;

import androidx.fragment.app.FragmentManager;

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

    public Marker addCurrentLocationMarker(GoogleMap mMap, LatLng coordinates){
        Marker marker=mMap.addMarker(new MarkerOptions().position(coordinates)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
        mMap.animateCamera( CameraUpdateFactory.newLatLngZoom(coordinates, 12f));

        return marker;
    }



    public void update(boolean inCircle, ActivityMapsBinding binding){
        binding.statusFooter.getRoot().setVisibility(View.VISIBLE);
        binding.statusFooter.status.setText(getMessage(inCircle));
        binding.statusFooter.getRoot().setBackgroundResource(getColor(inCircle));
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

    public boolean isInCircle(LatLng location){
      /*
        @location : place which we wanna check if it is in radius or not
      */
        float[] distance=new float[2];
        Location.distanceBetween( location.latitude, location.longitude,
                circle.getCenter().latitude, circle.getCenter().longitude, distance);

        return  distance[0] <= circle.getRadius();

    }

    public static float getDistanceBetween(LatLng Currentlocation,LatLng destination){
        float[] distance=new float[2];
         Location.distanceBetween( Currentlocation.latitude, Currentlocation.longitude,
                destination.latitude, destination.longitude, distance);
         return distance[0];

    }

    public static LatLng getCoordinates(String coordinatesString){
        Log.d("MainActivityyy", "getCoordinates: "+coordinatesString);
        if (coordinatesString==null) return null;
        if (coordinatesString.contains(",")){
            String[] values=coordinatesString.split(",");
            try {


                double lat =  Double.parseDouble(values[0]);
                double lng = Double.parseDouble(values[1]);
                return new LatLng(lat,lng);
            }catch (Exception e){
                Log.d("MainActivityyy", "getCoordinates exception: "+e.getMessage());
                return  null;
            }

        }
        return null;
    }

    public String getMessage(boolean inCircle){
        if (inCircle) return "you are inside the circle";
        return "you are outside the circle";
    }
    public int getColor(boolean inCircle){
        if (inCircle) return R.color.green;
        return R.color.red;
    }
}
