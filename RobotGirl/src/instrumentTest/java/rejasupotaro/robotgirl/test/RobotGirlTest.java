package rejasupotaro.robotgirl.test;

import android.os.Bundle;
import android.test.AndroidTestCase;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;

import rejasupotaro.robotgirl.Factory;
import rejasupotaro.robotgirl.RobotGirl;
import rejasupotaro.robotgirl.test.models.User;

public class RobotGirlTest extends AndroidTestCase {

    @Override
    public void setUp() {
        ActiveAndroid.initialize(new Configuration.Builder(getContext())
                .setDatabaseName("test")
                .setDatabaseVersion(1)
                .create());
    }

    public void testDefine() throws Exception {
        RobotGirl.define(new Factory("admin", User.class) {
            @Override
            public Bundle set(Bundle bundle) {
                bundle.putString("name", "John");
                bundle.putInt("age", 24);
                bundle.putBoolean("admin", true);
                return bundle;
            }
        });

        User user = RobotGirl.build("admin");
        assertEquals("John", user.getName());
        assertEquals(24, user.getAge());
        assertEquals(true, user.isAdmin());
    }
}
