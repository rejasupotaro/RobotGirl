package rejasupotaro.robotgirl.test.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Users")
public class User extends Model {
    @Column(name = "name") public String name;
    @Column(name = "age") public int age;
    @Column(name = "adming") public boolean admin;

    public User() {
        super();
    }
}
