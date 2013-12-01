package rejasupotaro.robotgirl.test;

import android.net.Uri;
import android.os.Bundle;
import android.test.InstrumentationTestCase;

import rejasupotaro.robotgirl.Factory;
import rejasupotaro.robotgirl.RobotGirl;
import rejasupotaro.robotgirl.test.models.User;
import rejasupotaro.robotgirl.test.models.UserGroup;

public class RobotGirlTest extends InstrumentationTestCase {

    public void testDefineUserGroup() {
        new RobotGirl.Builder(getInstrumentation().getTargetContext())
                .packageContext(getInstrumentation().getContext())
                .typeSerializers(UriTypeSerializer.class)
                .build();

        RobotGirl.define(
                new Factory(UserGroup.class, "developer") {
                    @Override
                    public Bundle set(Bundle bundle) {
                        bundle.putString("name", "developer");
                        return bundle;
                    }
                });

        UserGroup userGroup = RobotGirl.build(UserGroup.class, "developer");
        assertNotNull(userGroup);
        assertEquals("developer", userGroup.getName());

        RobotGirl.clear();
    }

    public void testDefineUserWithUserGroup() throws Exception {
        new RobotGirl.Builder(getInstrumentation().getTargetContext())
                .packageContext(getInstrumentation().getContext())
                .typeSerializers(UriTypeSerializer.class)
                .build();

        RobotGirl.define(
                new Factory(User.class) {
                    @Override
                    public Bundle set(Bundle attrs) {
                        attrs.putString("name", "John");
                        attrs.putInt("age", 24);
                        attrs.putBoolean("admin", false);
                        attrs.putString("uri", "http://www.google.com/");
                        attrs.putString("user_group", "developer");
                        return attrs;
                    }
                }, new Factory(UserGroup.class, "developer") {
                    @Override
                    public Bundle set(Bundle attrs) {
                        attrs.putString("name", "developer");
                        return attrs;
                    }
                }
        );

        User user = RobotGirl.build(User.class);
        assertEquals("John", user.getName());
        assertEquals(24, user.getAge());
        assertFalse(user.isAdmin());
        assertEquals(Uri.parse("http://www.google.com/"), user.getUri());

        UserGroup userGroup = user.getUserGroup();
        assertNotNull(userGroup);
        assertEquals("developer", userGroup.getName());

        RobotGirl.clear();
    }

    public void testDefineUserWithNullUserGroup() throws Exception {
        new RobotGirl.Builder(getInstrumentation().getTargetContext())
                .packageContext(getInstrumentation().getContext())
                .typeSerializers(UriTypeSerializer.class)
                .build();

        RobotGirl.define(
                new Factory(User.class) {
                    @Override
                    public Bundle set(Bundle attrs) {
                        attrs.putString("name", "John");
                        attrs.putInt("age", 24);
                        attrs.putBoolean("admin", false);
                        attrs.putString("uri", "http://www.google.com/");
                        return attrs;
                    }
                }, new Factory(User.class, "Admin") {
                    @Override
                    public Bundle set(Bundle attrs) {
                        attrs.putString("name", "Admin");
                        attrs.putBoolean("admin", true);
                        return attrs;
                    }
                }
        );

        User user = RobotGirl.build(User.class);
        assertEquals("John", user.getName());
        assertEquals(24, user.getAge());
        assertFalse(user.isAdmin());
        assertEquals(Uri.parse("http://www.google.com/"), user.getUri());
        assertNull(user.getUserGroup());

        User admin = RobotGirl.build(User.class, "Admin");
        assertEquals("Admin", admin.getName());
        assertTrue(admin.isAdmin());
        assertNull(admin.getUserGroup());

        RobotGirl.clear();
    }
}
