package rejasupotaro.robotgirl.test;

import android.os.Bundle;
import android.test.AndroidTestCase;

import rejasupotaro.robotgirl.Factory;
import rejasupotaro.robotgirl.RobotGirl;
import rejasupotaro.robotgirl.test.models.User;

public class RobotGirlTest extends AndroidTestCase {

    public void testDefine() throws Exception {
        RobotGirl.define(new Factory("admin", User.class) {
            @Override
            public Bundle set(Bundle bundle) {
                bundle.putString("name", "John");
                bundle.putString("age", "24");
                bundle.putString("admin", "true");
                return bundle;
            }
        });

        User user = RobotGirl.build("admin");
        assertEquals("John", user.name);
        assertEquals(24, user.age);
        assertEquals(true, user.admin);
    }
}
