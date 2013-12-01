package rejasupotaro.robotgirl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Cache;
import com.activeandroid.Model;
import com.activeandroid.TableInfo;
import com.activeandroid.serializer.TypeSerializer;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RobotGirl {

    public static final String TAG = RobotGirl.class.getSimpleName();

    private static String sDbName;

    private static List<String> sTableNames;

    private static Map<Class<? extends TypeSerializer>, TypeSerializer> sTypeSerializers =
            new HashMap<Class<? extends TypeSerializer>, TypeSerializer>();

    private static Map<String, Object> sNameModelHashMap = new HashMap<String, Object>();

    private static boolean sIsActiveAndroidAlreadyInitialized = false;

    public static <T extends Model> T build(Class<T> type) {
        return build(type.getSimpleName());
    }

    public static <T extends Model> T build(String name) {
        return (T) sNameModelHashMap.get(name);
    }

    private static void initActiveAndroid(Context dbContext, Context packageContext,
            String dbName) {
        if (sIsActiveAndroidAlreadyInitialized) return;
        sDbName = dbName;

        List<Class<? extends Model>> modelClasses = ModelScanner.scan(packageContext);
        sTableNames = ModelScanner.getTableNames(modelClasses);

        com.activeandroid.Configuration.Builder configurationBuilder = new com.activeandroid.Configuration.Builder(dbContext);
        configurationBuilder.setDatabaseName(sDbName);
        configurationBuilder.setDatabaseVersion(1);
        configurationBuilder.setModelClasses(modelClasses.toArray(new Class[0]));
        com.activeandroid.Configuration configuration = configurationBuilder.create();

        ActiveAndroid.initialize(configuration);

        sIsActiveAndroidAlreadyInitialized = true;
    }

    private static void setTypeSerializers(Class<? extends TypeSerializer>... typeSerializers) {
        if (typeSerializers == null || typeSerializers.length <= 0) return;

        for (Class<? extends TypeSerializer> typeSerializer : typeSerializers) {
            try {
                TypeSerializer instance = typeSerializer.newInstance();
                sTypeSerializers.put(
                        (Class<? extends TypeSerializer>) instance.getDeserializedType(),
                        instance);
            } catch (InstantiationException e) {
                Log.e(TAG, "Couldn't instantiate TypeSerializer.", e);
            } catch (IllegalAccessException e) {
                Log.e(TAG, e.getClass().getName(), e);
            }
        }
    }

    public static RobotGirl init(Context dbContext, Context packageContext, String dbName,
            Class<? extends TypeSerializer>... typeSerializers) {
        initActiveAndroid(dbContext, packageContext, dbName);
        setTypeSerializers(typeSerializers);
        return null;
    }

    public static RobotGirl define(Factory... factories) {
        for (Factory factory : factories) {
            define(factory);
        }
        return null;
    }

    public static RobotGirl define(Factory factory) {
        Class<? extends Model> modelClass = factory.getType();
        Bundle attrs = factory.set(new Bundle());

        Model model = buildModelFromAttributes(modelClass, attrs);
        if (model != null) {
            sNameModelHashMap.put(factory.getLabel(), model);
        }

        return null;
    }

    private static <T extends Model> Model buildModelFromAttributes(Class<T> modelClass, Bundle attrs) {
        TableInfo tableInfo = Cache.getTableInfo(modelClass);
        if (tableInfo == null) {
            tableInfo = new TableInfo(modelClass);
        }

        try {
            Object model = modelClass.newInstance();
            for (Field field : tableInfo.getFields()) {
                field.setAccessible(true);

                Class<?> fieldType = field.getType();
                String fieldName = tableInfo.getColumnName(field);
                Object value = readDeserializedValue(fieldType, attrs, fieldName);

                if (value != null) {
                    field.set(model, value);
                }
            }

            return (T) model;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static Object readDeserializedValue(Class<?> fieldType, Bundle attribute, String key) {
        TypeSerializer typeSerializer = sTypeSerializers.get(fieldType);
        if (typeSerializer != null) {
            fieldType = typeSerializer.getSerializedType();
        }

        Object value = readDataFromBundle(fieldType, attribute, key);

        if (typeSerializer != null) {
            value = typeSerializer.deserialize(value);
        }

        return value;
    }

    private static Object readDataFromBundle(Class<?> fieldType, Bundle attribute, String key) {
        Object value = null;
        Class<?> castType = CastMap.getCastType(fieldType);

        if (castType != null) {
            value = castType.cast(attribute.get(key));
        }

        return value;
    }

    public static void clear() {
        SQLiteDatabase db = ActiveAndroid.getDatabase();
        for (String tableName : sTableNames) {
            db.delete(tableName, null, null);
        }
    }

    public static class Builder {

        private Context dbContext;

        private Context packageContext;

        private String dbName = "test.db";

        private Class<? extends TypeSerializer>[] typeSerializers;

        public Builder(Context dbContext) {
            this.dbContext = dbContext;
        }

        public Builder packageContext(Context packageContext) {
            this.packageContext = packageContext;
            return this;
        }

        public Builder dbName(String dbName) {
            this.dbName = dbName;
            return this;
        }

        public Builder typeSerializers(Class<? extends TypeSerializer>... typeSerializers) {
            this.typeSerializers = typeSerializers;
            return this;
        }

        public void build() {
            if (packageContext == null) {
                packageContext = dbContext;
            }

            RobotGirl.init(dbContext, packageContext, dbName, typeSerializers);
        }
    }
}
