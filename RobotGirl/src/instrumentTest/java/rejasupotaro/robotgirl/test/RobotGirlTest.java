package rejasupotaro.robotgirl.test;

import android.net.Uri;
import android.os.Bundle;
import android.test.InstrumentationTestCase;

import rejasupotaro.robotgirl.Factory;
import rejasupotaro.robotgirl.RobotGirl;
import rejasupotaro.robotgirl.test.models.User;

public class RobotGirlTest extends InstrumentationTestCase {

    public void testDefine() throws Exception {
        new RobotGirl.Builder(getInstrumentation().getTargetContext())
                .packageContext(getInstrumentation().getContext())
                .typeSerializers(UriTypeSerializer.class)
                .build();

        RobotGirl.define(new Factory(User.class, "admin") {
            @Override
            public Bundle set(Bundle bundle) {
                bundle.putString("name", "John");
                bundle.putInt("age", 24);
                bundle.putBoolean("admin", true);
                bundle.putString("uri", "http://rejasupota.ro/");
                return bundle;
            }
        });

        User user = RobotGirl.build(User.class, "admin");
        assertEquals("John", user.getName());
        assertEquals(24, user.getAge());
        assertEquals(true, user.isAdmin());
        assertEquals(Uri.parse("http://rejasupota.ro/"), user.getUri());
    }
}
