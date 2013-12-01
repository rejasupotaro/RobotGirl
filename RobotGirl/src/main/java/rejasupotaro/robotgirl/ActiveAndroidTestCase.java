package rejasupotaro.robotgirl;

import com.activeandroid.serializer.TypeSerializer;

import android.content.Context;
import android.test.InstrumentationTestCase;

import java.util.List;

public class ActiveAndroidTestCase extends InstrumentationTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Context targetContext = getInstrumentation().getTargetContext();
        List<Class<? extends TypeSerializer>> typeSerializers =
                TypeSerializerScanner.scan(targetContext);
        new RobotGirl.Builder(getInstrumentation().getContext())
                .packageContext(getInstrumentation().getTargetContext())
                .typeSerializers(typeSerializers.toArray(new Class[0]))
                .build();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        RobotGirl.clear();
    }
}
