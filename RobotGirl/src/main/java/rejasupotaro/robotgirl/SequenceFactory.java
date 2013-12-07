package rejasupotaro.robotgirl;

import android.os.Bundle;

import com.activeandroid.Model;

public abstract class SequenceFactory extends Factory {

    private int mIndex = 0;

    public SequenceFactory(Class<? extends Model> type) {
        super(type);
    }

    public SequenceFactory(Class<? extends Model> type, String label) {
        super(type, label);
    }

    @Override
    public final Bundle set(Bundle attrs) {
        return set(attrs, mIndex++);
    }

    public abstract Bundle set(Bundle attrs, int n);
}
