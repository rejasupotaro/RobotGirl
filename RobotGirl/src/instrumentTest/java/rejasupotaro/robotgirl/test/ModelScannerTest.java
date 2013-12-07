package rejasupotaro.robotgirl.test;

import com.activeandroid.Model;

import android.content.Context;
import android.test.InstrumentationTestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rejasupotaro.robotgirl.ModelScanner;
import rejasupotaro.robotgirl.test.models.User;
import rejasupotaro.robotgirl.test.models.UserGroup;

public class ModelScannerTest extends InstrumentationTestCase {

    public void testScan() {
        Context testContext = getInstrumentation().getContext();
        List<Class<? extends Model>> modelClasses = ModelScanner.scan(testContext);

        List<String> modelClassNames = new ArrayList<String>();
        for (Class type : modelClasses) {
            modelClassNames.add(type.getSimpleName());
        }
        String[] modelClassNameArray = modelClassNames.toArray(new String[]{});
        Arrays.sort(modelClassNameArray);

        assertEquals("Book", modelClassNameArray[0]);
        assertEquals("User", modelClassNameArray[1]);
        assertEquals("UserGroup", modelClassNameArray[2]);

        assertEquals(3, modelClasses.size());
    }

    public void testGetTableNames() {
        List<Class<? extends Model>> modelsClasses = new ArrayList<Class<? extends Model>>();
        modelsClasses.add(User.class);
        modelsClasses.add(UserGroup.class);
        List<String> tableNames = ModelScanner.getTableNames(modelsClasses);

        assertEquals(2, tableNames.size());
        assertEquals("Users", tableNames.get(0));
    }
}
