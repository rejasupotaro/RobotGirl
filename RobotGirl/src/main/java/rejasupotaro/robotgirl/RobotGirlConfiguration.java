package rejasupotaro.robotgirl;

import android.content.Context;

import com.activeandroid.serializer.TypeSerializer;

public class RobotGirlConfiguration {

    private Context mDbContext;

    private Context mPackageContext;

    private String mDbName = "test.db";

    private Class<? extends TypeSerializer>[] mTypeSerializers;

    public Context getDbContext() {
        return mDbContext;
    }

    public Context getPackageContext() {
        return mPackageContext;
    }

    public String getDbName() {
        return mDbName;
    }

    public Class<? extends TypeSerializer>[] getTypeSerializers() {
        return mTypeSerializers;
    }

    public RobotGirlConfiguration(Context dbContext, Context packageContext, String dbName,
                                  Class<? extends TypeSerializer>[] typeSerializers) {
        mDbContext = dbContext;
        mPackageContext = packageContext;
        mDbName = dbName;
        mTypeSerializers = typeSerializers;
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

        public RobotGirlConfiguration build() {
            if (packageContext == null) {
                packageContext = dbContext;
            }

            return new RobotGirlConfiguration(dbContext, packageContext, dbName, typeSerializers);
        }
    }
}
