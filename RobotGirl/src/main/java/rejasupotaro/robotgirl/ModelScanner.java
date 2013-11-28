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

    public static List<Class<? extends Model>> scan(Context context) {
        List<Class<? extends Model>> modelClasses = PackageScanner.scan(context, new PackageScanner.Filter() {
            @Override
            public boolean filter(Class clazz) {
                return ReflectionUtils.isModel(clazz);
            }
        });
        return modelClasses;
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
