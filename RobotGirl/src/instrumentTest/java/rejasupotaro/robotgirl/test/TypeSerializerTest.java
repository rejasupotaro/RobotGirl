package rejasupotaro.robotgirl.test;

import com.activeandroid.serializer.TypeSerializer;

import android.content.Context;
import android.test.InstrumentationTestCase;

import java.util.List;

import rejasupotaro.robotgirl.TypeSerializerScanner;

public class TypeSerializerTest extends InstrumentationTestCase {

    public void testScan() {
        Context testContext = getInstrumentation().getContext();
        List<Class<? extends TypeSerializer>> modelClasses = TypeSerializerScanner.scan(testContext);
        assertEquals(1, modelClasses.size());
    }
}
