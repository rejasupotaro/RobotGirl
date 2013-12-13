package rejasupotaro.robotgirl.test;

import android.os.Bundle;
import android.test.AndroidTestCase;

import rejasupotaro.robotgirl.SequenceDefinition;
import rejasupotaro.robotgirl.test.models.Book;

public class SequenceDefinitionTest extends AndroidTestCase {

    public void testCreate() {
        SequenceDefinition factory = new SequenceDefinition(Book.class, "Book") {
            @Override
            public Bundle set(Bundle attrs, int n) {
                attrs.putInt("book_id", 100 + n);
                attrs.putString("title", "Land of Lisp #" + n);
                return attrs;
            }
        };

        assertEquals("Book", factory.getLabel());
        assertEquals(Book.class, factory.getType());

        {
            Bundle bundle = factory.get();
            assertEquals(100, bundle.getInt("book_id"));
            assertEquals("Land of Lisp #0", bundle.getString("title"));
        }

        {
            Bundle bundle = factory.get();
            assertEquals(101, bundle.getInt("book_id"));
            assertEquals("Land of Lisp #1", bundle.getString("title"));
        }
    }
}

