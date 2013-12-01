package rejasupotaro.robotgirl.test.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "UserGroups")
public class UserGroup extends Model {
    @Column(name = "name") private String mName;

    public String getName() {
        return mName;
    }

    public UserGroup() {
        super();
    }
}
