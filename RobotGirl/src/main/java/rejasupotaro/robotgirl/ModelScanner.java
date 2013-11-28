package rejasupotaro.robotgirl;

import com.activeandroid.Model;
import com.activeandroid.annotation.Table;
import com.activeandroid.util.ReflectionUtils;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import dalvik.system.DexFile;

public class ModelScanner {

    public static final String TAG = ModelScanner.class.getSimpleName();

    public static List<Class<? extends Model>> scan(Context context) {
        List<Class<? extends Model>> classes = new ArrayList<Class<? extends Model>>();

        List<String> paths = scanClassPaths(context);
        if (paths == null || paths.size() == 0) {
            return classes;
        }

        String packageName = context.getPackageName();
        for (String path : paths) {
            File file = new File(path);
            Class<? extends Model> clazz =
                    scanModelClasses(file, packageName, context.getClassLoader());

            if (clazz != null) {
                classes.add(clazz);
            }
        }

        return classes;
    }

    private static List<String> scanClassPaths(Context context) {
        List<String> paths = new ArrayList<String>();

        try {
            String sourcePath = context.getApplicationInfo().sourceDir;
            if (sourcePath != null && !(new File(sourcePath).isDirectory())) {
                DexFile dexFile = new DexFile(sourcePath);
                Enumeration<String> entries = dexFile.entries();

                while (entries.hasMoreElements()) {
                    paths.add(entries.nextElement());
                }
            } else {
                // Robolectric fallback
                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                Enumeration<URL> resources = classLoader.getResources("");

                while (resources.hasMoreElements()) {
                    String path = resources.nextElement().getFile();
                    if (path.contains("bin") || path.contains("classes")) {
                        paths.add(path);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return paths;
    }

    private static Class<? extends Model> scanModelClasses(File path, String packageName,
            ClassLoader classLoader) {
        if (path.isDirectory()) {
            for (File file : path.listFiles()) {
                scanModelClasses(file, packageName, classLoader);
            }
        } else {
            // Robolectric fallback
            String className = path.getName();
            if (!path.getPath().equals(className)) {
                className = path.getPath();

                if (className.endsWith(".class")) {
                    className = className.substring(0, className.length() - 6);
                } else {
                    return null;
                }

                className = className.replace("/", ".");

                int packageNameIndex = className.lastIndexOf(packageName);
                if (packageNameIndex < 0) {
                    return null;
                }

                className = className.substring(packageNameIndex);
            }

            try {
                Class<?> discoveredClass = Class.forName(className, false, classLoader);
                if (ReflectionUtils.isModel(discoveredClass)) {
                    @SuppressWarnings("unchecked")
                    Class<? extends Model> modelClass = (Class<? extends Model>) discoveredClass;
                    return modelClass;
                }
            } catch (ClassNotFoundException e) {
                Log.e(TAG, "Couldn't create class.", e);
            }
        }

        return null;
    }

    public static List<String> getTableNames(List<Class<? extends Model>> modelClasses) {
        List<String> tableNames = new ArrayList<String>();
        for (Class<? extends Model> modelClass : modelClasses) {
            final Table tableAnnotation = modelClass.getAnnotation(Table.class);
            if (tableAnnotation != null) {
                tableNames.add(tableAnnotation.name());
            }
        }
        return tableNames;
    }

}
