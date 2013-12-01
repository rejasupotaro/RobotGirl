package rejasupotaro.robotgirl;

import com.activeandroid.Model;

import android.os.Bundle;

public abstract class Factory {

    private Class mType;

    private String mLabel;

    public String getLabel() {
        return mLabel;
    }

    public Class<? extends Model> getType() {
        return mType;
    }

    public Factory(Class<? extends Model> type) {
        mType = type;
        mLabel = type.getSimpleName();
    }

    public Factory(Class<? extends Model> type, String label) {
        mType = type;
        mLabel = label;
    }

    public abstract Bundle set(Bundle attrs);
}
