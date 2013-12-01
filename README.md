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

Defining factories
------

Each factory has name and set of attributes.
The name is used to guess the class of the object by default, but it's possible to explicitly specify it:

```java
RobotGirl.define(
        // This will guess the User class
        new Factory(User.class) {
            @Override
            public Bundle set(Bundle attrs) {
                attrs.putString("name", "John");
                attrs.putBoolean("admin", false);
                return attrs;
            }
        // This will use the User class (Adming would have been guessed)
        }, new Factory(User.class, "admin") {
            @Override
            public Bundle set(Bundle attrs) {
                attrs.putString("name", "Admin");
                attrs.putBoolean("admin", true);
                return attrs;
            }
        });
```

Using factories
------

```java
User user = RobotGirl.build(User.class);
user.getName(); // => "John"
user.isAdmin(); // => false

User admin = RobotGirl.build(User.class, "admin");
admin.getName(); // => "Admin"
admin.isAdmin(); // => true
```

Associations
------

It's possible to set up associations within factories.
If the factory name is the same as the association name, the factory name can be left out.


```java
public class UserTest extends ActiveAndroidTestCase {

    @Override
    protected void setUp() {
        super.setUp();

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
        user.getUserGroup().getName(); // => developer
    }
```

TODO
------

- [x] Run Travis
- [x] Create Faker

Contributing
------

1. Fork it
2. Create your feature branch (`git checkout -b my-new-feature`)
3. Commit your changes (`git commit -am 'Added some feature'`)
4. Push to the branch (`git push origin my-new-feature`)
5. Create new Pull Request
