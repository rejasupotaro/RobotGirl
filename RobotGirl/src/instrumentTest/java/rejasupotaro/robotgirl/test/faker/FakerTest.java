package rejasupotaro.robotgirl.test.faker;

import android.test.InstrumentationTestCase;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import rejasupotaro.robotgirl.faker.Faker;
import rejasupotaro.robotgirl.faker.Locale;

public class FakerTest extends InstrumentationTestCase {

    @Override
    public void setUp() throws Exception {
        super.setUp();
        Faker.init(getInstrumentation().getContext(), Locale.JA);
    }

    public void testGetFirstNameList() {
        List<String> firstNameList = Faker.Name.getFirstNameList();
        assertEquals(21, firstNameList.size());
    }

    public void testGetLastNameList() {
        List<String> firstNameList = Faker.Name.getFirstNameList();
        assertEquals(21, firstNameList.size());
    }

    public void testGetName() {
        assertNotNull(Faker.Name.name());
    }
}
