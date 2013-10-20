package rejasupotaro.robotgirl;

import android.os.Bundle;

import com.activeandroid.Cache;
import com.activeandroid.Model;
import com.activeandroid.TableInfo;

import java.lang.reflect.Field;
import java.util.HashMap;

public class RobotGirl {

    public static final String TAG = RobotGirl.class.getSimpleName();

    private static HashMap<String, Object> sNameModelHashMap = new HashMap<String, Object>();

    public static <T extends Model> T build(String name) {
        return (T) sNameModelHashMap.get(name);
    }

    public static void define(Factory factory) throws IllegalAccessException, InstantiationException {
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

            Object value = null;
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

            if (value != null) {
                field.set(model, value);
            }
        }

        sNameModelHashMap.put(factory.getName(), model);
    }
}
