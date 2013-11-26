package rejasupotaro.robotgirl;

public enum CastMap {
    BYTE(Byte.class, Byte.class, byte.class),
    SHORT(Short.class, Short.class, short.class),
    INT(Integer.class, Integer.class, int.class),
    LONG(Long.class, Long.class, long.class),
    FLOAT(Float.class, Float.class, float.class),
    DOUBLE(Double.class, Double.class, double.class),
    BOOLEAN(Boolean.class, Boolean.class, boolean.class),
    CHAR(Character.class, Character.class, char.class),
    STRING(String.class, String.class),
    BYTE_ARRAY(Byte[].class, Byte[].class, byte[].class);

    private Class<?> castType;

    private Class<?>[] checkTypes;

    private CastMap(Class<?> castType, Class<?>... checkTypes) {
        this.castType = castType;
        this.checkTypes = checkTypes;
    }

    public static Class<?> getCastType(Class<?> targetType) {
        for (CastMap castMap : CastMap.values()) {
            for (Class<?> checkType : castMap.checkTypes) {
                if (targetType.equals(checkType)) {
                    return castMap.castType;
                }
            }
        }
        return null;
    }
}

