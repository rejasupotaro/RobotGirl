package rejasupotaro.robotgirl.test;

import android.net.Uri;
import android.os.Bundle;
import android.test.InstrumentationTestCase;

import rejasupotaro.robotgirl.Definition;
import rejasupotaro.robotgirl.Factory;
import rejasupotaro.robotgirl.RobotGirlConfiguration;
import rejasupotaro.robotgirl.SequenceDefinition;
import rejasupotaro.robotgirl.test.models.Book;
import rejasupotaro.robotgirl.test.models.User;
import rejasupotaro.robotgirl.test.models.UserGroup;

public class FactoryTest extends InstrumentationTestCase {

    public void testDefineUserGroup() {
        RobotGirlConfiguration conf =
                new RobotGirlConfiguration.Builder(getInstrumentation().getTargetContext())
                        .packageContext(getInstrumentation().getContext())
                        .typeSerializers(UriTypeSerializer.class)
                        .build();
        Factory.init(conf);

        Factory.define(
                new Definition(UserGroup.class, "developer") {
                    @Override
                    public Bundle set(Bundle bundle) {
                        bundle.putString("name", "developer");
                        return bundle;
                    }
                });

        UserGroup userGroup = Factory.build(UserGroup.class, "developer");
        assertNotNull(userGroup);
        assertEquals("developer", userGroup.getName());

        Factory.clear();
    }

    public void testDefineUserWithUserGroup() throws Exception {
        RobotGirlConfiguration conf =
                new RobotGirlConfiguration.Builder(getInstrumentation().getTargetContext())
                        .packageContext(getInstrumentation().getContext())
                        .typeSerializers(UriTypeSerializer.class)
                        .build();
        Factory.init(conf);

        Factory.define(
                new Definition(User.class) {
                    @Override
                    public Bundle set(Bundle attrs) {
                        attrs.putString("name", "John");
                        attrs.putInt("age", 24);
                        attrs.putBoolean("admin", false);
                        attrs.putString("uri", "http://www.google.com/");
                        attrs.putString("user_group", "developer");
                        return attrs;
                    }
                }, new Definition(UserGroup.class, "developer") {
                    @Override
                    public Bundle set(Bundle attrs) {
                        attrs.putString("name", "developer");
                        return attrs;
                    }
                }
        );

        User user = Factory.build(User.class);
        assertEquals("John", user.getName());
        assertEquals(24, user.getAge());
        assertFalse(user.isAdmin());
        assertEquals(Uri.parse("http://www.google.com/"), user.getUri());

        UserGroup userGroup = user.getUserGroup();
        assertNotNull(userGroup);
        assertEquals("developer", userGroup.getName());

        Factory.clear();
    }

    public void testDefineUserWithNullUserGroup() throws Exception {
        RobotGirlConfiguration conf =
                new RobotGirlConfiguration.Builder(getInstrumentation().getTargetContext())
                        .packageContext(getInstrumentation().getContext())
                        .typeSerializers(UriTypeSerializer.class)
                        .build();
        Factory.init(conf);

        Factory.define(
                new Definition(User.class) {
                    @Override
                    public Bundle set(Bundle attrs) {
                        attrs.putString("name", "John");
                        attrs.putInt("age", 24);
                        attrs.putBoolean("admin", false);
                        attrs.putString("uri", "http://www.google.com/");
                        return attrs;
                    }
                }, new Definition(User.class, "Admin") {
                    @Override
                    public Bundle set(Bundle attrs) {
                        attrs.putString("name", "Admin");
                        attrs.putBoolean("admin", true);
                        return attrs;
                    }
                }
        );

        User user = Factory.build(User.class);
        assertEquals("John", user.getName());
        assertEquals(24, user.getAge());
        assertFalse(user.isAdmin());
        assertEquals(Uri.parse("http://www.google.com/"), user.getUri());
        assertNull(user.getUserGroup());

        User admin = Factory.build(User.class, "Admin");
        assertEquals("Admin", admin.getName());
        assertTrue(admin.isAdmin());
        assertNull(admin.getUserGroup());

        Factory.clear();
    }

    public void testSequence() {
        RobotGirlConfiguration conf =
                new RobotGirlConfiguration.Builder(getInstrumentation().getTargetContext())
                        .packageContext(getInstrumentation().getContext())
                        .build();
        Factory.init(conf);

        Factory.define(
                new SequenceDefinition(Book.class) {
                    @Override
                    public Bundle set(Bundle attrs, int n) {
                        attrs.putInt("book_id", 100 + n);
                        attrs.putString("title", "Land of Lisp #" + n);
                        return attrs;
                    }
                });

        Book book1 = Factory.next(Book.class);
        assertEquals(100, book1.getBookId());
        assertEquals("Land of Lisp #0", book1.getTitle());

        Book book2 = Factory.next(Book.class);
        assertEquals(101, book2.getBookId());
        assertEquals("Land of Lisp #1", book2.getTitle());
    }
}
