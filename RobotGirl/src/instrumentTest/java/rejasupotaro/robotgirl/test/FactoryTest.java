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
                bundle.putString("mName", "John");
                bundle.putInt("mAge", 24);
                bundle.putBoolean("mAdmin", true);
                return bundle;
            }
        };
        assertEquals("admin", factory.getName());
        assertEquals(User.class, factory.getModelClass());

        Bundle bundle = factory.set(new Bundle());
        assertEquals("John", bundle.getString("mName"));
        assertEquals(24, bundle.getInt("mAge"));
        assertEquals(true, bundle.getBoolean("mAdmin"));
    }
}
