package rejasupotaro.robotgirl.test.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Users")
public class User extends Model {
    @Column(name = "name") private String mName;
    @Column(name = "age") private int mAge;
    @Column(name = "admin") private boolean mAdmin;

    public String getName() {
        return mName;
    }

    public int getAge() {
        return mAge;
    }

    public boolean isAdmin() {
        return mAdmin;
    }

    public User() {
        super();
    }
}
