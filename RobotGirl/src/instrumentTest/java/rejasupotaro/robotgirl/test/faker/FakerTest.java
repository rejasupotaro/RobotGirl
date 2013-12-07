package rejasupotaro.robotgirl.test.faker;

import android.test.InstrumentationTestCase;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import rejasupotaro.robotgirl.faker.Faker;
import rejasupotaro.robotgirl.faker.Locale;

public class FakerTest extends InstrumentationTestCase {

    public void testGetFirstNameListWithEN() throws IOException {
        Faker.init(getInstrumentation().getContext(), Locale.EN);
        List<String> firstNameList = Faker.Name.getFirstNameList();
        assertEquals(3007, firstNameList.size());
    }

    public void testGetLastNameListWithEN() throws IOException {
        Faker.init(getInstrumentation().getContext(), Locale.EN);
        List<String> firstNameList = Faker.Name.getFirstNameList();
        assertEquals(3007, firstNameList.size());
    }

    public void testGetNameWithEN() throws IOException {
        Faker.init(getInstrumentation().getContext(), Locale.EN);
        assertNotNull(Faker.Name.name());
        assertFalse(Faker.Name.name().indexOf(" ") == 0);
    }

    public void testGetFirstNameListWithJA() throws IOException {
        Faker.init(getInstrumentation().getContext(), Locale.JA);
        List<String> firstNameList = Faker.Name.getFirstNameList();
        assertEquals(21, firstNameList.size());
    }

    public void testGetLastNameListWithJA() throws IOException {
        Faker.init(getInstrumentation().getContext(), Locale.JA);
        List<String> firstNameList = Faker.Name.getFirstNameList();
        assertEquals(21, firstNameList.size());
    }

    public void testGetNameWithJA() throws IOException {
        Faker.init(getInstrumentation().getContext(), Locale.JA);
        assertNotNull(Faker.Name.name());
    }
}
