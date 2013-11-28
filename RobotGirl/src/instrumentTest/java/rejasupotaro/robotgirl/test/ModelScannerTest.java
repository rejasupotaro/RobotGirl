package rejasupotaro.robotgirl.test;

import com.activeandroid.Model;

import android.content.Context;
import android.test.InstrumentationTestCase;

import java.util.ArrayList;
import java.util.List;

import rejasupotaro.robotgirl.ModelScanner;
import rejasupotaro.robotgirl.test.models.User;

public class ModelScannerTest extends InstrumentationTestCase {

    public void testScan() {
        Context testContext = getInstrumentation().getContext();
        List<Class<? extends Model>> modelClasses = ModelScanner.scan(testContext);
        assertEquals(1, modelClasses.size());
    }

    public void testGetTableNames() {
        List<Class<? extends Model>> modelsClasses = new ArrayList<Class<? extends Model>>();
        modelsClasses.add(User.class);
        List<String> tableNames = ModelScanner.getTableNames(modelsClasses);

        assertEquals(1, tableNames.size());
        assertEquals("Users", tableNames.get(0));
    }
}
