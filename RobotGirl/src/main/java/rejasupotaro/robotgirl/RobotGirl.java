package rejasupotaro.robotgirl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.serializer.TypeSerializer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RobotGirl {

    public static final String TAG = RobotGirl.class.getSimpleName();

    private static String sDbName;

    private static List<String> sTableNames;

    private static boolean sIsAlreadyInitialized = false;

    public static void init(RobotGirlConfiguration conf) {
        init(conf.getDbContext(), conf.getPackageContext(), conf.getDbName(),
                conf.getTypeSerializers());
    }

    public static void init(Context dbContext, Context packageContext, String dbName,
                            Class<? extends TypeSerializer>... typeSerializers) {
        if (sIsAlreadyInitialized) {
            return;
        }

        initActiveAndroid(dbContext, packageContext, dbName);
        Map<Class<? extends TypeSerializer>, TypeSerializer> typeSerializerMap =
                createTypeSerializerMap(typeSerializers);
        Factory.init(typeSerializerMap);

        sIsAlreadyInitialized = true;
        return;
    }

    private static void initActiveAndroid(Context dbContext, Context packageContext,
                                          String dbName) {
        sDbName = dbName;

        List<Class<? extends Model>> modelClasses = ModelScanner.scan(packageContext);
        sTableNames = ModelScanner.getTableNames(modelClasses);

        com.activeandroid.Configuration.Builder configurationBuilder
                = new com.activeandroid.Configuration.Builder(dbContext);
        configurationBuilder.setDatabaseName(sDbName);
        configurationBuilder.setDatabaseVersion(1);
        configurationBuilder.setModelClasses(modelClasses.toArray(new Class[0]));
        com.activeandroid.Configuration configuration = configurationBuilder.create();

        ActiveAndroid.initialize(configuration);
    }

    private static Map<Class<? extends TypeSerializer>, TypeSerializer> createTypeSerializerMap(Class<? extends TypeSerializer>... typeSerializers) {
        Map<Class<? extends TypeSerializer>, TypeSerializer> typeSerializerMap =
                new HashMap<Class<? extends TypeSerializer>, TypeSerializer>();

        if (typeSerializers == null || typeSerializers.length <= 0) {
            return typeSerializerMap;
        }

        for (Class<? extends TypeSerializer> typeSerializer : typeSerializers) {
            try {
                TypeSerializer instance = typeSerializer.newInstance();
                typeSerializerMap.put(
                        (Class<? extends TypeSerializer>) instance.getDeserializedType(),
                        instance);
            } catch (InstantiationException e) {
                Log.e(TAG, "Couldn't instantiate TypeSerializer.", e);
            } catch (IllegalAccessException e) {
                Log.e(TAG, e.getClass().getName(), e);
            }
        }

        return typeSerializerMap;
    }

    public static void clear() {
        SQLiteDatabase db = ActiveAndroid.getDatabase();
        for (String tableName : sTableNames) {
            db.delete(tableName, null, null);
        }
        sIsAlreadyInitialized = false;
    }
}
