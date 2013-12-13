package rejasupotaro.robotgirl;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Cache;
import com.activeandroid.Model;
import com.activeandroid.TableInfo;
import com.activeandroid.serializer.TypeSerializer;
import com.activeandroid.util.ReflectionUtils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Factory {

    public static final String TAG = Factory.class.getSimpleName();

    private static String sDbName;

    private static List<String> sTableNames;

    private static Map<Class<? extends TypeSerializer>, TypeSerializer> sTypeSerializers =
            new HashMap<Class<? extends TypeSerializer>, TypeSerializer>();

    private static Map<String, Definition> sLabelFactoryMap = new HashMap<String, Definition>();

    private static boolean sIsActiveAndroidAlreadyInitialized = false;

    public static <T extends Model> T build(Class<T> type) {
        return build(type, type.getSimpleName());
    }

    public static <T extends Model> T build(Class<T> type, String label) {
        Definition definition = sLabelFactoryMap.get(label);
        if (definition == null) {
            throw new ModelCreateFailedException("Received unknown type or label" +
                    ": label => " + label +
                    ", labelFactoryMap => " + sLabelFactoryMap.toString());
        }
        Bundle attrs = definition.get();
        if (attrs == null) {
            throw new ModelCreateFailedException("Cannot generate model from empty attrs");
        }
        return (T) buildModelFromAttributes(type, attrs);
    }

    public static <T extends Model> T next(Class<T> type) {
        return next(type, type.getSimpleName());
    }

    public static <T extends Model> T next(Class<T> type, String label) {
        return build(type, label);
    }

    private static <T extends Model> Model buildModelFromAttributes(Class<T> modelClass,
            Bundle attrs) {
        TableInfo tableInfo = Cache.getTableInfo(modelClass);
        if (tableInfo == null) {
            tableInfo = new TableInfo(modelClass);
        }

        try {
            Object model = modelClass.newInstance();
            for (Field field : tableInfo.getFields()) {
                field.setAccessible(true);

                Class<?> fieldType = field.getType();
                String columnName = tableInfo.getColumnName(field);
                Object value = readFieldValue(fieldType, columnName, attrs);

                if (value == null) {
                    if (columnName.equals("Id")) {
                        field.set(model, 1L);
                    } else {
                        Log.d(TAG, "Skip to set value to field" +
                                ": fieldType => " + fieldType.getSimpleName() +
                                ", columnName => " + columnName + attrs.toString() +
                                ", attrs => " + attrs.toString());
                    }
                } else {
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

    private static Object readFieldValue(Class<?> fieldType, String columnName, Bundle attrs) {
        TypeSerializer typeSerializer = sTypeSerializers.get(fieldType);
        if (typeSerializer != null) {
            fieldType = typeSerializer.getSerializedType();
        }

        Object value = null;
        if (ReflectionUtils.isModel(fieldType)) {
            String associationLabel = attrs.getString(columnName);
            if (associationLabel == null) {
                value = null;
            } else {
                value = build((Class<? extends Model>) fieldType, attrs.getString(columnName));
            }
        } else {
            value = readDataFromBundle(fieldType, columnName, attrs);
        }

        if (value != null && typeSerializer != null) {
            value = typeSerializer.deserialize(value);
        }

        return value;
    }

    private static Object readDataFromBundle(Class<?> fieldType, String key, Bundle attrs) {
        if (attrs == null) {
            return null;
        }

        Object value = null;
        Class<?> castType = CastMap.getCastType(fieldType);

        if (castType != null) {
            value = castType.cast(attrs.get(key));
        }

        return value;
    }

    private static void initActiveAndroid(Context dbContext, Context packageContext,
            String dbName) {
        if (sIsActiveAndroidAlreadyInitialized) {
            return;
        }
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

        sIsActiveAndroidAlreadyInitialized = true;
    }

    private static void setTypeSerializers(Class<? extends TypeSerializer>... typeSerializers) {
        if (typeSerializers == null || typeSerializers.length <= 0) {
            return;
        }

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

    public static Factory init(RobotGirlConfiguration conf) {
        return init(conf.getDbContext(), conf.getPackageContext(), conf.getDbName(),
                conf.getTypeSerializers());
    }

    public static Factory init(Context dbContext, Context packageContext, String dbName,
            Class<? extends TypeSerializer>... typeSerializers) {
        initActiveAndroid(dbContext, packageContext, dbName);
        setTypeSerializers(typeSerializers);
        return null;
    }

    public static Factory define(Definition... factories) {
        for (Definition definition : factories) {
            define(definition);
        }
        return null;
    }

    public static Factory define(Definition definition) {
        sLabelFactoryMap.put(definition.getLabel(), definition);
        return null;
    }

    public static void clear() {
        SQLiteDatabase db = ActiveAndroid.getDatabase();
        for (String tableName : sTableNames) {
            db.delete(tableName, null, null);
        }
    }
}
