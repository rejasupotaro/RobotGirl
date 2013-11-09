package rejasupotaro.robotgirl;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Cache;
import com.activeandroid.Configuration;
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

    private static void initActiveAndroid(Context context) {
        if (sIsActiveAndroidAlreadyInitialized) return;

        Configuration.Builder configurationBuilder = new Configuration.Builder(context);
        configurationBuilder.setDatabaseName(TEST_DB_NAME);
        configurationBuilder.setDatabaseVersion(1);
        ActiveAndroid.initialize(configurationBuilder.create());

        sIsActiveAndroidAlreadyInitialized = true;
    }

    private static void setsTypeSerializers(Class<? extends TypeSerializer>... typeSerializers) {
        if (typeSerializers == null || typeSerializers.length <= 0) return;

        for (Class<? extends TypeSerializer> typeSerializer : typeSerializers) {
            try {
                TypeSerializer instance = (TypeSerializer) typeSerializer.newInstance();
                sTypeSerializers.put(instance.getDeserializedType(), instance);
            } catch (InstantiationException e) {
                Log.e(TAG, "Couldn't instantiate TypeSerializer.", e);
            } catch (IllegalAccessException e) {
                Log.e(TAG, "IllegalAccessException", e);
            }
        }
    }

    public static RobotGirl init(Context context) {
        init(context, new Class[]{});
        return null;
    }

    public static RobotGirl init(Context context, Class<? extends TypeSerializer> typeSerializer) {
        init(context, new Class[]{ typeSerializer });
        return null;
    }

    public static RobotGirl init(Context context, Class<? extends TypeSerializer>... typeSerializers) {
        initActiveAndroid(context);
        setsTypeSerializers(typeSerializers);
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
            final String fieldName = tableInfo.getColumnName(field);
            Class<?> fieldType = field.getType();

            field.setAccessible(true);

            TypeSerializer typeSerializer = sTypeSerializers.get(fieldType);
            Object value = null;

            if (typeSerializer != null) {
                fieldType = typeSerializer.getSerializedType();
            }

            if (fieldType.equals(Byte.class) || field.equals(byte.class)) {
                value = attribute.getByte(fieldName);
            } else if (fieldType.equals(Short.class) || fieldType.equals(short.class)) {
                value = attribute.getShort(fieldName);
            } else if (fieldType.equals(Integer.class) || fieldType.equals(int.class)) {
                value = attribute.getInt(fieldName);
            } else if (fieldType.equals(Long.class) || fieldType.equals(long.class)) {
                value = attribute.getLong(fieldName);
            } else if (fieldType.equals(Float.class) || fieldType.equals(float.class)) {
                value = attribute.getFloat(fieldName);
            } else if (fieldType.equals(Double.class) || fieldType.equals(double.class)) {
                value = attribute.getDouble(fieldName);
            } else if (fieldType.equals(Boolean.class) || fieldType.equals(boolean.class)) {
                value = attribute.getBoolean(fieldName);
            } else if (fieldType.equals(Character.class) || fieldType.equals(char.class)) {
                value = attribute.getChar(fieldName);
            } else if (fieldType.equals(String.class)) {
                value = attribute.getString(fieldName);
            } else if (fieldType.equals(Byte[].class) || fieldType.equals(byte[].class)) {
                value = attribute.getByteArray(fieldName);
            }

            if (typeSerializer != null) {
                value = typeSerializer.deserialize(value);
            }

            if (value != null) {
                field.set(model, value);
            }
        }

        sNameModelHashMap.put(factory.getName(), model);
        return null;
    }
}
