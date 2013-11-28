package rejasupotaro.robotgirl;

import android.test.InstrumentationTestCase;

public class ActiveAndroidTestCase extends InstrumentationTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        new RobotGirl.Builder(getInstrumentation().getTargetContext())
                .packageContext(getInstrumentation().getTargetContext())
                .build();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        RobotGirl.clear();
    }
}
