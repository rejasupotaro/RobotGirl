package rejasupotaro.robotgirl.test;

import android.test.AndroidTestCase;

import rejasupotaro.robotgirl.CastMap;

public class CastMapTest extends AndroidTestCase {

    public void testGetCastType() {
        assertEquals(Byte.class, CastMap.getCastType(Byte.class));
        assertEquals(Byte.class, CastMap.getCastType(byte.class));
        assertEquals(Short.class, CastMap.getCastType(Short.class));
        assertEquals(Short.class, CastMap.getCastType(short.class));
        assertEquals(Integer.class, CastMap.getCastType(Integer.class));
        assertEquals(Integer.class, CastMap.getCastType(int.class));
        assertEquals(Long.class, CastMap.getCastType(Long.class));
        assertEquals(Long.class, CastMap.getCastType(long.class));
        assertEquals(Float.class, CastMap.getCastType(Float.class));
        assertEquals(Float.class, CastMap.getCastType(float.class));
        assertEquals(Double.class, CastMap.getCastType(Double.class));
        assertEquals(Double.class, CastMap.getCastType(double.class));
        assertEquals(Boolean.class, CastMap.getCastType(Boolean.class));
        assertEquals(Boolean.class, CastMap.getCastType(boolean.class));
        assertEquals(Character.class, CastMap.getCastType(Character.class));
        assertEquals(Character.class, CastMap.getCastType(char.class));
        assertEquals(String.class, CastMap.getCastType(String.class));
        assertEquals(Byte[].class, CastMap.getCastType(Byte[].class));
        assertEquals(Byte[].class, CastMap.getCastType(byte[].class));
    }
}
