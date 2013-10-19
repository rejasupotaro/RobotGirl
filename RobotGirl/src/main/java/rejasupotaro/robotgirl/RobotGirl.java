package rejasupotaro.robotgirl;

import android.os.Bundle;
import android.util.Log;

import com.activeandroid.Model;

import java.util.HashMap;

public class RobotGirl {

    public static final String TAG = RobotGirl.class.getSimpleName();

    private HashMap<String, Model> mNameModelHashMap;

    public static void define(Factory factory) {
        try {
            Model model = (Model) factory.getModelClass().newInstance();
            Bundle attribute = factory.set(new Bundle());
        } catch (InstantiationException e) {
            Log.e(TAG, e.getMessage());
        } catch (IllegalAccessException e) {
            Log.e(TAG, e.getMessage());
        }

    }
}
