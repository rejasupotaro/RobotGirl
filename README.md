RobotGirl
======

<a href="http://molybdenumgp03.deviantart.com/art/robot-girl-195104830"><img src="http://ninjacrunch.com/wp-content/uploads/2011/12/4/2.jpg"></a>

RobotGirl is a fixtures replacement library for [ActiveAndroid](http://www.activeandroid.com/).
Like Ruby gem [factory_girl](https://github.com/thoughtbot/factory_girl)

Installation
------

Generate jar and put generated jar in your libs directory.

```sh
$ gradle jar -Penv=release
```

Setup
------

Inheritance ActiveAndroidTestCase.

```java
public class UserTest extends ActiveAndroidTestCase {

    @Override
    protected void setUp() {
        super.setUp();

        RobotGirl.define(new Factory("admin", User.class) {
            @Override
            public Bundle set(Bundle bundle) {
                bundle.putString("name", "John");
                bundle.putInt("age", 24);
                bundle.putBoolean("admin", true);
                bundle.putString("uri", "http://rejasupota.ro/");
                return bundle;
            }
        });
    }
```

Usage
------

```java
User user = RobotGirl.build("admin");
assertEquals("John", user.getName());
assertEquals(24, user.getAge());
assertEquals(true, user.isAdmin());
assertEquals(Uri.parse("http://rejasupota.ro/"), user.getUri());
```

TODO
------

- [x] Scan TypeSerializers
- [x] Support association
- [x] Create Faker

Contributing
------

1. Fork it
2. Create your feature branch (`git checkout -b my-new-feature`)
3. Commit your changes (`git commit -am 'Added some feature'`)
4. Push to the branch (`git push origin my-new-feature`)
5. Create new Pull Request
