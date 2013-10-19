package rejasupotaro.robotgirl;

import android.os.Bundle;

public abstract class Factory {

    private String mName;

    private Class mModelClass;

    public String getName() {
        return mName;
    }

    public Class getModelClass() {
        return mModelClass;
    }

    public Factory(String name, Class modelClass) {
        mName = name;
        mModelClass = modelClass;
    }

    public abstract Bundle set(Bundle bundle);
}
