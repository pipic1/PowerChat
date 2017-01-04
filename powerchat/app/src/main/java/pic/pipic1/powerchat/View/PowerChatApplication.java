package pic.pipic1.powerchat.View;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by emimo on 12/03/2016.
 */
public class PowerChatApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
