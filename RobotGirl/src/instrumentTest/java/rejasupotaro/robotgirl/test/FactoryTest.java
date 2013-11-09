package rejasupotaro.robotgirl.test;

import android.os.Bundle;
import android.test.AndroidTestCase;

import rejasupotaro.robotgirl.Factory;
import rejasupotaro.robotgirl.RobotGirl;
import rejasupotaro.robotgirl.test.models.User;

public class FactoryTest extends AndroidTestCase {

    public void testCreate() {
        Factory factory = new Factory("admin", User.class) {
            @Override
            public Bundle set(Bundle bundle) {
                bundle.putString("name", "John");
                bundle.putInt("age", 24);
                bundle.putBoolean("admin", true);
                bundle.putString("uri", "http://rejasupota.ro/");
                return bundle;
            }
        };
        assertEquals("admin", factory.getName());
        assertEquals(User.class, factory.getModelClass());

        Bundle bundle = factory.set(new Bundle());
        assertEquals("John", bundle.getString("name"));
        assertEquals(24, bundle.getInt("age"));
        assertEquals(true, bundle.getBoolean("admin"));
        assertEquals("http://rejasupota.ro/", bundle.getString("uri"));
    }
}
