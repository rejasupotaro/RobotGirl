package rejasupotaro.robotgirl.test;

import android.os.Bundle;
import android.test.AndroidTestCase;

import rejasupotaro.robotgirl.Factory;
import rejasupotaro.robotgirl.RobotGirl;
import rejasupotaro.robotgirl.test.models.User;

public class FactoryTest extends AndroidTestCase {

    public void testCreate() {
        Factory factory = new Factory(User.class, "John") {
            @Override
            public Bundle set(Bundle attrs) {
                attrs.putString("name", "John");
                attrs.putInt("age", 24);
                attrs.putBoolean("admin", false);
                attrs.putString("uri", "http://rejasupota.ro/");
                attrs.putString("user_group", "developer");
                return attrs;
            }
        };

        assertEquals("John", factory.getLabel());
        assertEquals(User.class, factory.getType());

        Bundle bundle = factory.get();
        assertEquals("John", bundle.getString("name"));
        assertEquals(24, bundle.getInt("age"));
        assertEquals(false, bundle.getBoolean("admin"));
        assertEquals("http://rejasupota.ro/", bundle.getString("uri"));
        assertEquals("developer", bundle.getString("user_group"));
    }
}
