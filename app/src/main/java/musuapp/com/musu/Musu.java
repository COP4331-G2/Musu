package musuapp.com.musu;

import android.app.Application;

import com.cloudinary.android.MediaManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by david on 3/24/2018.
 */

public class Musu extends Application {

    @Override
    public void onCreate()
    {
        Map myMap = new HashMap();
        myMap.put("cloud_name", "dgz4xvj8r");
        super.onCreate();
        MediaManager.init(this, myMap);
    }
}
