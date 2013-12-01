package rejasupotaro.robotgirl;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import dalvik.system.DexFile;

public class PackageScanner {

    public static String TAG = PackageScanner.class.getSimpleName();

    public static <T> List<Class<? extends T>> scan(Context context, Filter filter) {
        List<Class<? extends T>> classes = new ArrayList<Class<? extends T>>();

        List<String> paths = scanClassPaths(context);
        if (paths == null || paths.size() == 0) {
            return classes;
        }

        String packageName = context.getPackageName();
        for (String path : paths) {
            File file = new File(path);
            Class<? extends T> clazz =
                    scanClasses(file, packageName, context.getClassLoader(), filter);

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

    private static <T> Class<? extends T> scanClasses(File path, String packageName,
            ClassLoader classLoader, Filter filter) {
        if (path.isDirectory()) {
            for (File file : path.listFiles()) {
                scanClasses(file, packageName, classLoader, filter);
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
                Class<? extends T> discoveredClass =
                        (Class<? extends T>) Class.forName(className, false, classLoader);
                if (filter.filter(discoveredClass)) {
                    return discoveredClass;
                }
            } catch (ClassNotFoundException e) {
                // ignore
            }
        }

        return null;
    }

    public static abstract class Filter {

        public abstract boolean filter(Class clazz);
    }

}
