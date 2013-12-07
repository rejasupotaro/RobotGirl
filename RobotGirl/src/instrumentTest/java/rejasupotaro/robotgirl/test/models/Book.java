package rejasupotaro.robotgirl.test.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Books")
public class Book extends Model {
    @Column(name = "book_id") private int mBookId;
    @Column(name = "title") private String mTitle;

    public int getBookId() {
        return mBookId;
    }

    public String getTitle() {
        return mTitle;
    }

    public Book() {
        super();
    }
}

