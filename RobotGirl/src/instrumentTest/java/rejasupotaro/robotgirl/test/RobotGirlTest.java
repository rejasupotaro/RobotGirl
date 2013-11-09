package rejasupotaro.robotgirl.test;

import android.net.Uri;
import android.os.Bundle;
import android.test.AndroidTestCase;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;

import rejasupotaro.robotgirl.Factory;
import rejasupotaro.robotgirl.RobotGirl;
import rejasupotaro.robotgirl.test.models.User;

public class RobotGirlTest extends AndroidTestCase {

    public void testDefine() throws Exception {
        RobotGirl.init(getContext(), UriTypeSerializer.class).define(new Factory("admin", User.class) {
            @Override
            public Bundle set(Bundle bundle) {
                bundle.putString("name", "John");
                bundle.putInt("age", 24);
                bundle.putBoolean("admin", true);
                bundle.putString("uri", "http://rejasupota.ro/");
                return bundle;
            }
        });

        User user = RobotGirl.build("admin");
        assertEquals("John", user.getName());
        assertEquals(24, user.getAge());
        assertEquals(true, user.isAdmin());
        assertEquals(Uri.parse("http://rejasupota.ro/"), user.getUri());
    }
}
