RobotGirl
======

RobotGirl is a fixtures replacement tool for ActiveAndroid

Like Ruby gem `factory_girl`

Installation
------

Add dependency in your build settings.

```groovy
repositories {
    mavenCentral()
    maven { url 'https://raw.github.com/rejasupotaro/RobotGirl/master/RobotGirl/repository' }
}

dependencies {
    ...
    compile 'rejasupotaro:robotgirl:0.0.3'
}
```

Setup
------

In your test cast.

```java
RobotGirl.init(getContext(), UriTypeSerializer.class).define(new Factory("admin", User.class) {
    @Override
    public Bundle set(Bundle bundle) {
        bundle.putString("name", "John");
        bundle.putInt("age", 24);
        bundle.putBoolean("admin", true);
        bundle.putString("uri", "http://rejasupota.ro/");
        return bundle;
    }
});
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

Contributing
------

1. Fork it
2. Create your feature branch (`git checkout -b my-new-feature`)
3. Commit your changes (`git commit -am 'Added some feature'`)
4. Push to the branch (`git push origin my-new-feature`)
5. Create new Pull Request
