package rejasupotaro.robotgirl;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Cache;
import com.activeandroid.Model;
import com.activeandroid.TableInfo;
import com.activeandroid.serializer.TypeSerializer;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class RobotGirl {

    public static final String TAG = RobotGirl.class.getSimpleName();

    private static final String TEST_DB_NAME = "test";

    private static Map<Class<?>, TypeSerializer> sTypeSerializers = new HashMap<Class<?>, TypeSerializer>();

    private static Map<String, Object> sNameModelHashMap = new HashMap<String, Object>();

    private static boolean sIsActiveAndroidAlreadyInitialized = false;

    public static <T extends Model> T build(String name) {
        return (T) sNameModelHashMap.get(name);
    }

    private static void initActiveAndroid(Context dbContext, Context packageContext) {
        if (sIsActiveAndroidAlreadyInitialized) return;

        com.activeandroid.Configuration.Builder configurationBuilder = new com.activeandroid.Configuration.Builder(dbContext);
        configurationBuilder.setDatabaseName(TEST_DB_NAME);
        configurationBuilder.setDatabaseVersion(1);
        configurationBuilder.setModelClasses(
                (Class<? extends Model>[]) ModelScanner.scan(packageContext).toArray(new Class[0]));
        com.activeandroid.Configuration configuration = configurationBuilder.create();

        ActiveAndroid.initialize(configuration);

        sIsActiveAndroidAlreadyInitialized = true;
    }

    private static void setTypeSerializers(Class<? extends TypeSerializer>... typeSerializers) {
        if (typeSerializers == null || typeSerializers.length <= 0) return;

        for (Class<? extends TypeSerializer> typeSerializer : typeSerializers) {
            try {
                TypeSerializer instance = typeSerializer.newInstance();
                sTypeSerializers.put(instance.getDeserializedType(), instance);
            } catch (InstantiationException e) {
                Log.e(TAG, "Couldn't instantiate TypeSerializer.", e);
            } catch (IllegalAccessException e) {
                Log.e(TAG, e.getClass().getName(), e);
            }
        }
    }

    public static RobotGirl init(Context dbContext, Context packageContext, Class<? extends TypeSerializer>... typeSerializers) {
        initActiveAndroid(dbContext, packageContext);
        setTypeSerializers(typeSerializers);
        return null;
    }

    public static RobotGirl define(Factory factory) throws IllegalAccessException, InstantiationException {
        Class<? extends Model> modelClass = (Class<? extends Model>) factory.getModelClass();
        TableInfo tableInfo = Cache.getTableInfo(modelClass);
        if (tableInfo == null) {
            tableInfo = new TableInfo(modelClass);
        }

        Bundle attribute = factory.set(new Bundle());

        Object model = modelClass.newInstance();
        for (Field field : tableInfo.getFields()) {
            field.setAccessible(true);

            Class<?> fieldType = field.getType();
            String fieldName = tableInfo.getColumnName(field);
            Object value = readDeserializedValue(fieldType, attribute, fieldName);

            if (value != null) {
                field.set(model, value);
            }
        }

        sNameModelHashMap.put(factory.getName(), model);
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

    public static class Builder {

        private Context dbContext;

        private Context packageContext;

        private Class<? extends TypeSerializer>[] typeSerializers;

        public Builder(Context dbContext) {
            this.dbContext = dbContext;
        }

        public Builder packageContext(Context packageContext) {
            this.packageContext = packageContext;
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

            RobotGirl.init(dbContext, packageContext, typeSerializers);
        }
    }
}
