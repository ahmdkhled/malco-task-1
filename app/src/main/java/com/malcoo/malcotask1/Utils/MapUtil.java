package com.malcoo.malcotask1.Utils;

import android.app.Activity;
import android.graphics.Color;
import android.location.Location;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.malcoo.malcotask1.R;
import com.tapadoo.alerter.Alerter;

public class MapUtil {
    Circle circle;

    public Marker addCurrentLocationMarker(GoogleMap mMap, LatLng coordinates){
        Marker marker=mMap.addMarker(new MarkerOptions().position(coordinates)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
        mMap.animateCamera( CameraUpdateFactory.newLatLngZoom(coordinates, 12f));

        return marker;
    }

    public void alert(boolean inCircle, Activity activity){
        Alerter.create(activity).setTitle("your status")
                .setText(getMessage(inCircle)).
                setBackgroundColorRes(getColor(inCircle))
                .show();
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

    public String getMessage(boolean inCircle){
        if (inCircle) return "you are inside the circle";
        return "you are outside the circle";
    }
    public int getColor(boolean inCircle){
        if (inCircle) return R.color.green;
        return R.color.red;
    }
}
