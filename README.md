RobotGirl [![wercker status](https://app.wercker.com/status/4c80f0762f2a4f416329f08341e90e3b/s/ "wercker status")](https://app.wercker.com/project/bykey/4c80f0762f2a4f416329f08341e90e3b)
======

<a href="http://molybdenumgp03.deviantart.com/art/robot-girl-195104830"><img src="https://dl.dropboxusercontent.com/u/54255753/blog/201312/robotgirl.png"></a>

RobotGirl is a fixtures replacement library for [ActiveAndroid](http://www.activeandroid.com/).
Like ruby gem [FactoryGirl](https://github.com/thoughtbot/factory_girl)

Installation
------

Generate jar and put generated jar in your libs directory.

```sh
$ git clone https://github.com/rejasupotaro/RobotGirl.git
$ cd RobotGirl
$ gradle jar -Penv=release
```

Defining Factories
------

Each factory has name and set of attributes.
The name is used to guess the class of the object by default, but it's possible to explicitly specify it:

```java
Factory.define(
        // This will guess the User class
        new Definition(User.class) {
            @Override
            public Bundle set(Bundle attrs) {
                attrs.putString("name", "John");
                attrs.putBoolean("admin", false);
                return attrs;
            }
        // This will use the User class (Adming would have been guessed)
        }, new Definition(User.class, "admin") {
            @Override
            public Bundle set(Bundle attrs) {
                attrs.putString("name", "Admin");
                attrs.putBoolean("admin", true);
                return attrs;
            }
        });
```

Using Factories
------

RobotGirl supports several different build strategies: build, create:

```java
// Returns a User instance that's not saved
user = Factory.build(User.class);

// Returns a saves User instance
user = Factory.create(User.class);

// No matter which strategy is used
user.getName(); // => "John"
user.isAdmin(); // => false

User admin = Factory.build(User.class, "admin");
admin.getName(); // => "Admin"
admin.isAdmin(); // => true
```

Sequences
------

Unique values in a specific format (for example, e-mail addresses) can be generated using sequences.
Sequences are defined by using SequenceFactory as a definition arguments, and values in a sequence are generated by calling Factory.next:

```java
// Defines a new sequence
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
book1.getBookId(); // => 100
book1.getTitle();  // => Land of Lisp #0

Book book2 = Factory.next(Book.class);
book2.getBookId(); // => 101
book2.getTitle();  // => Land of Lisp #1 
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

        User user = Definition.build(User.class);
        user.getUserGroup().getName(); // => developer
    }
```

Using Faker
------

This function is a port of Perl's Data::Faker library that generates fake data.

```java
Faker.init(context, Locale.EN);

Faker.Name.name      // => "Christophe Bartell"
Faker.Internet.email // => "kirsten.greenholt@corkeryfisher.info"
```

Contributing
------

1. Fork it
2. Create your feature branch (`git checkout -b my-new-feature`)
3. Commit your changes (`git commit -am 'Added some feature'`)
4. Push to the branch (`git push origin my-new-feature`)
5. Create new Pull Request

License
------

```
 Copyright 2013 rejasupotaro

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
```
