package chat.tidy.json;

import com.google.common.primitives.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public abstract class JsonContainer<C, O extends E, E, A extends E> {

    Class<C> selfClass;
    Class<C[]> selfArrayClass;
    Class<E> elementClass;
    Class<E[]> elementArrayClass;

    JsonContainer() {
    }

    public final String getString(String key) {
        return getString(key, "");
    }

    public String getString(String key, String fallback) {
        return fallback;
    }

    public abstract C addString(String key, String value);

    public final boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean fallback) {
        return fallback;
    }

    public abstract C addBoolean(String key, boolean value);

    public final byte getByte(String key) {
        return getByte(key, (byte) 0);
    }

    public byte getByte(String key, byte fallback) {
        return fallback;
    }

    public abstract C addByte(String key, byte value);

    public final short getShort(String key) {
        return getShort(key, (short) 0);
    }

    public short getShort(String key, short fallback) {
        return fallback;
    }

    public abstract C addShort(String key, short value);

    public final int getInt(String key) {
        return getInt(key, 0);
    }

    public int getInt(String key, int fallback) {
        return fallback;
    }

    public abstract C addInt(String key, int value);

    public final float getFloat(String key) {
        return getFloat(key, 0f);
    }

    public float getFloat(String key, float fallback) {
        return fallback;
    }

    public abstract C addFloat(String key, float value);

    public final long getLong(String key) {
        return getLong(key, 0L);
    }

    public long getLong(String key, long fallback) {
        return fallback;
    }

    public abstract C addLong(String key, long value);

    public final double getDouble(String key) {
        return getDouble(key, 0d);
    }

    public double getDouble(String key, double fallback) {
        return fallback;
    }

    public abstract C addDouble(String key, double value);

    public abstract O getObject();

    public abstract E getElement(String key);

    public abstract C addElement(String key, Object value);

    public abstract A getArray(String key);

    public abstract <T> Iterable<T> getArray(String key, Class<T> type);

    public abstract C addArray(String key, Iterable iterable);

    public abstract C addArray(String key, A value);

    public abstract void copy(C other);

    public abstract boolean has(String key);

    public abstract void remove(String key);

    public final C add(Object... objects) {
        for (int i = 0; i < objects.length; i++) {
            if (i % 2 == 1) {
                try {
                    Object key = objects[i - 1];
                    if (key instanceof String) {
                        Object value = objects[i];
                        if (value != null) {
                            if (value.getClass().equals(selfClass)) {
                                addElement(key.toString(), value);
                            } else if (value.getClass().equals(elementClass)) {
                                addElement(key.toString(), value);
                            } else if (value instanceof String) {
                                addString(key.toString(), value.toString());
                            } else if (value instanceof Boolean) {
                                addBoolean(key.toString(), (Boolean) value);
                            } else if (value instanceof Byte) {
                                addByte(key.toString(), (Byte) value);
                            } else if (value instanceof Short) {
                                addShort(key.toString(), ((Number) value).shortValue());
                            } else if (value instanceof Integer) {
                                addInt(key.toString(), ((Number) value).intValue());
                            } else if (value instanceof Float) {
                                addFloat(key.toString(), ((Number) value).floatValue());
                            } else if (value instanceof Long) {
                                addLong(key.toString(), ((Number) value).longValue());
                            } else if (value instanceof Double) {
                                addDouble(key.toString(), ((Number) value).doubleValue());
                            } else if (value.getClass().equals(selfArrayClass)) {
                                addArray(key.toString(), Arrays.asList((Object[]) value));
                            } else if (value.getClass().equals(elementArrayClass)) {
                                addArray(key.toString(), Arrays.asList((Object[]) value));
                            } else if (value instanceof String[]) {
                                addArray(key.toString(), Arrays.asList((String[]) value));
                            } else if (value instanceof boolean[]) {
                                addArray(key.toString(), Booleans.asList((boolean[]) value));
                            } else if (value instanceof byte[]) {
                                addArray(key.toString(), Bytes.asList((byte[]) value));
                            } else if (value instanceof short[]) {
                                addArray(key.toString(), Shorts.asList((short[]) value));
                            } else if (value instanceof int[]) {
                                addArray(key.toString(), Ints.asList((int[]) value));
                            } else if (value instanceof float[]) {
                                addArray(key.toString(), Floats.asList((float[]) value));
                            } else if (value instanceof long[]) {
                                addArray(key.toString(), Longs.asList((long[]) value));
                            } else if (value instanceof double[]) {
                                addArray(key.toString(), Doubles.asList((double[]) value));
                            } else if (value instanceof Iterable) {
                                addArray(key.toString(), ((Iterable) value));
                            } else {
                                addElement(key.toString(), value);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return (C) this;
    }

    public final int getLength() {
        return toString().length();
    }

    public final String getMD5() {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            StringBuilder stringBuilder = new StringBuilder();
            for (byte bytes : digest.digest(toString().getBytes())) {
                stringBuilder.append(Integer.toHexString((bytes & 0xFF) | 0x100), 1, 3);
            }
            return stringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public abstract String toString();
}
