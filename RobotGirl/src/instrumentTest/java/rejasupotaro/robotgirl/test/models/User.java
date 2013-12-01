package rejasupotaro.robotgirl.test.models;

import android.net.Uri;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Users")
public class User extends Model {
    @Column(name = "name") private String mName;
    @Column(name = "age") private int mAge;
    @Column(name = "admin") private boolean mAdmin;
    @Column(name = "uri") private Uri mUri;
    @Column(name = "user_group") private UserGroup mUserGroup;

    public String getName() {
        return mName;
    }

    public int getAge() {
        return mAge;
    }

    public boolean isAdmin() {
        return mAdmin;
    }

    public Uri getUri() {
        return mUri;
    }

    public UserGroup getUserGroup() {
        return mUserGroup;
    }

    public User() {
        super();
    }
}
