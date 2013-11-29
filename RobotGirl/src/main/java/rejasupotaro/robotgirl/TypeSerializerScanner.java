package rejasupotaro.robotgirl;

import com.activeandroid.serializer.TypeSerializer;
import com.activeandroid.util.ReflectionUtils;

import android.content.Context;

import java.util.List;

public class TypeSerializerScanner {

    public static List<Class<? extends TypeSerializer>> scan(Context context) {
        List<Class<? extends TypeSerializer>> typeSerializers = PackageScanner.scan(context, new PackageScanner.Filter() {
            @Override
            public boolean filter(Class clazz) {
                return ReflectionUtils.isSubclassOf(clazz, TypeSerializer.class);
            }
        });
        return typeSerializers;
    }
}
