package com.malcoo.malcotask1.Utils;

import android.graphics.Color;
import android.location.Location;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.malcoo.malcotask1.R;

public class MapUtil {
    Circle circle;

    //function to add circle surrounding plcaes like ware house
    public void drawCircle(LatLng place, GoogleMap map){
        CircleOptions circleOptions = new CircleOptions();

        circleOptions.center(place);

        circleOptions.radius(500);

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

        return distance[0] <= circle.getRadius();

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
