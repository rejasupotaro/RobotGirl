package rejasupotaro.robotgirl.test;

import com.activeandroid.Model;

import android.content.Context;
import android.test.InstrumentationTestCase;

import java.util.List;

import rejasupotaro.robotgirl.ModelScanner;

public class ModelScannerTest extends InstrumentationTestCase {

    public void testScan() {
        Context testContext = getInstrumentation().getContext();
        List<Class<? extends Model>> modelClasses = ModelScanner.scan(testContext);
        assertEquals(1, modelClasses.size());
    }
}
