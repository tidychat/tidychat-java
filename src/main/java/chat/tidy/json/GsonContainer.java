package chat.tidy.json;

import chat.tidy.json.impl.StatusCodeDeserializer;
import chat.tidy.json.impl.StatusCodeSerializer;
import chat.tidy.json.impl.UUIDDeserializer;
import chat.tidy.json.impl.UUIDSerializer;
import chat.tidy.message.StatusCode;
import com.google.common.base.Preconditions;
import com.google.gson.*;
import com.google.gson.annotations.Expose;

import java.util.*;

public class GsonContainer extends JsonContainer<GsonContainer, JsonObject, JsonElement, JsonArray> {

    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(UUID.class, new UUIDSerializer())
            .registerTypeAdapter(UUID.class, new UUIDDeserializer())
            .registerTypeAdapter(StatusCode.class, new StatusCodeSerializer())
            .registerTypeAdapter(StatusCode.class, new StatusCodeDeserializer())
            .excludeFieldsWithoutExposeAnnotation()
            .create();
    private static final JsonParser PARSER = new JsonParser();

    @Expose
    private final JsonObject jsonObject;

    public GsonContainer() {
        this(new JsonObject());
    }

    public GsonContainer(JsonElement jsonElement) {
        super();
        if (jsonElement == null || !jsonElement.isJsonObject()) {
            this.jsonObject = new JsonObject();
        } else {
            this.jsonObject = jsonElement.getAsJsonObject();
        }
        this.selfClass = GsonContainer.class;
        this.selfArrayClass = GsonContainer[].class;
        this.elementClass = JsonElement.class;
        this.elementArrayClass = JsonElement[].class;
    }

    public static GsonContainer serialize(Object object) {
        return new GsonContainer(GSON.toJsonTree(object).getAsJsonObject());
    }

    public static GsonContainer serialize(String json) {
        return new GsonContainer(PARSER.parse(json).getAsJsonObject());
    }

    public static <T> T deserialize(String json, Class<T> clazz) {
        return GSON.fromJson(json, clazz);
    }

    @Override
    public String getString(String key, String fallback) {
        Preconditions.checkNotNull(key);
        JsonElement jsonElement = getElement(key);
        if (jsonElement != null && jsonElement.isJsonPrimitive() && jsonElement.getAsJsonPrimitive().isString()) {
            return jsonElement.getAsJsonPrimitive().getAsString();
        }
        return fallback;
    }

    @Override
    public GsonContainer addString(String key, String value) {
        Preconditions.checkNotNull(key);
        Preconditions.checkNotNull(value);
        return addElement(key, value);
    }

    @Override
    public boolean getBoolean(String key, boolean fallback) {
        Preconditions.checkNotNull(key);
        JsonElement jsonElement = getElement(key);
        if (jsonElement != null && jsonElement.isJsonPrimitive() && jsonElement.getAsJsonPrimitive().isBoolean()) {
            return jsonElement.getAsJsonPrimitive().getAsBoolean();
        }
        return fallback;
    }

    @Override
    public GsonContainer addBoolean(String key, boolean value) {
        Preconditions.checkNotNull(key);
        return addElement(key, value);
    }

    @Override
    public byte getByte(String key, byte fallback) {
        Preconditions.checkNotNull(key);
        Number number = getNumber(key);
        if (number != null) {
            return number.byteValue();
        }
        return fallback;
    }

    @Override
    public GsonContainer addByte(String key, byte value) {
        Preconditions.checkNotNull(key);
        addElement(key, value);
        return this;
    }

    @Override
    public short getShort(String key, short fallback) {
        Preconditions.checkNotNull(key);
        Number number = getNumber(key);
        if (number != null) {
            return number.shortValue();
        }
        return fallback;
    }

    @Override
    public GsonContainer addShort(String key, short value) {
        Preconditions.checkNotNull(key);
        return addElement(key, value);
    }

    @Override
    public int getInt(String key, int fallback) {
        Preconditions.checkNotNull(key);
        Number number = getNumber(key);
        if (number != null) {
            return number.intValue();
        }
        return fallback;
    }

    @Override
    public GsonContainer addInt(String key, int value) {
        Preconditions.checkNotNull(key);
        return addElement(key, value);
    }

    @Override
    public float getFloat(String key, float fallback) {
        Preconditions.checkNotNull(key);
        Number number = getNumber(key);
        if (number != null) {
            return number.floatValue();
        }
        return fallback;
    }

    @Override
    public GsonContainer addFloat(String key, float value) {
        Preconditions.checkNotNull(key);
        return addElement(key, value);
    }

    @Override
    public long getLong(String key, long fallback) {
        Preconditions.checkNotNull(key);
        Number number = getNumber(key);
        if (number != null) {
            return number.longValue();
        }
        return fallback;
    }

    @Override
    public GsonContainer addLong(String key, long value) {
        Preconditions.checkNotNull(key);
        return addElement(key, value);
    }

    @Override
    public double getDouble(String key, double fallback) {
        Preconditions.checkNotNull(key);
        Number number = getNumber(key);
        if (number != null) {
            return number.doubleValue();
        }
        return fallback;
    }

    @Override
    public GsonContainer addDouble(String key, double value) {
        Preconditions.checkNotNull(key);
        return addElement(key, value);
    }

    @Override
    public JsonObject getObject() {
        return jsonObject;
    }

    @Override
    public JsonElement getElement(String key) {
        Preconditions.checkNotNull(key);
        try {
            String[] keys = key.split("\\.");
            JsonElement latestElement = jsonObject;
            for (String s : keys) {
                if (latestElement == null || !latestElement.isJsonObject() || !latestElement.getAsJsonObject().has(s)) {
                    return null;
                }
                latestElement = latestElement.getAsJsonObject().get(s);
            }
            return latestElement;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public GsonContainer addElement(String key, Object value) {
        List<String> keys = Arrays.asList(key.split("\\."));
        if (keys.size() > 0) {
            JsonObject jsonObject = null;
            if (keys.size() == 1) {
                jsonObject = this.jsonObject;
            } else {
                JsonElement jsonElement = getElement(String.join(".", keys.subList(0, keys.size() - 1)));
                if (jsonElement != null && jsonElement.isJsonObject()) {
                    jsonObject = jsonElement.getAsJsonObject();
                }
                key = keys.get(keys.size() - 1);
            }
            if (jsonObject != null) {
                if (value instanceof GsonContainer) {
                    jsonObject.add(key, ((GsonContainer) value).getObject());
                } else if (value instanceof JsonElement) {
                    jsonObject.add(key, (JsonElement) value);
                } else if (value instanceof String) {
                    jsonObject.addProperty(key, (String) value);
                } else if (value instanceof Boolean) {
                    jsonObject.addProperty(key, (Boolean) value);
                } else if (value instanceof Number) {
                    jsonObject.addProperty(key, (Number) value);
                } else {
                    jsonObject.add(key, GsonContainer.serialize(value).getObject());
                }
            }
        }
        return this;
    }

    @Override
    public JsonArray getArray(String key) {
        Preconditions.checkNotNull(key);
        JsonElement jsonElement = getElement(key);
        if (jsonElement != null && jsonElement.isJsonArray()) {
            return jsonElement.getAsJsonArray();
        }
        return null;
    }

    @Override
    public <T> Set<T> getArray(String key, Class<T> type) {
        Set<T> list = new HashSet<>();
        JsonArray jsonArray = getArray(key);
        if (jsonArray != null) {
            try {
                for (JsonElement jsonElement : jsonArray) {
                    if (jsonElement.isJsonPrimitive()) {
                        JsonPrimitive jsonPrimitive = jsonElement.getAsJsonPrimitive();
                        if (jsonPrimitive.isString() && type.equals(String.class)) {
                            list.add((T) jsonPrimitive.getAsString());
                        } else if (jsonPrimitive.isBoolean() && type.equals(Boolean.class)) {
                            list.add((T) Boolean.valueOf(jsonPrimitive.getAsBoolean()));
                        } else if (jsonPrimitive.isNumber()) {
                            if (type.equals(Byte.class)) {
                                list.add((T) Byte.valueOf(jsonPrimitive.getAsNumber().byteValue()));
                            } else if (type.equals(Short.class)) {
                                list.add((T) Short.valueOf(jsonPrimitive.getAsNumber().shortValue()));
                            } else if (type.equals(Integer.class)) {
                                list.add((T) Integer.valueOf(jsonPrimitive.getAsNumber().intValue()));
                            } else if (type.equals(Float.class)) {
                                list.add((T) Float.valueOf(jsonPrimitive.getAsNumber().floatValue()));
                            } else if (type.equals(Long.class)) {
                                list.add((T) Long.valueOf(jsonPrimitive.getAsNumber().longValue()));
                            } else if (type.equals(Double.class)) {
                                list.add((T) Double.valueOf(jsonPrimitive.getAsNumber().doubleValue()));
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    @Override
    public GsonContainer addArray(String key, Iterable iterable) {
        JsonArray jsonArray = new JsonArray();
        for (Object value : iterable) {
            if (value instanceof GsonContainer) {
                jsonArray.add(((GsonContainer) value).getObject());
            } else if (value instanceof JsonElement) {
                jsonArray.add((JsonElement) value);
            } else if (value instanceof String) {
                jsonArray.add((String) value);
            } else if (value instanceof Boolean) {
                jsonArray.add((Boolean) value);
            } else if (value instanceof Number) {
                jsonArray.add((Number) value);
            }
        }
        return addArray(key, jsonArray);
    }

    @Override
    public GsonContainer addArray(String key, JsonArray value) {
        return addElement(key, value);
    }

    @Override
    public void copy(GsonContainer other) {
        for (Map.Entry<String, JsonElement> entry : other.getObject().entrySet()) {
            jsonObject.add(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public boolean has(String key) {
        return getElement(key) != null;
    }

    @Override
    public void remove(String key) {
        List<String> keys = Arrays.asList(key.split("\\."));
        if (keys.size() > 0) {
            JsonObject jsonObject = null;
            if (keys.size() == 1) {
                jsonObject = this.jsonObject;
            } else {
                JsonElement jsonElement = getElement(String.join(".", keys.subList(0, keys.size() - 1)));
                if (jsonElement != null && jsonElement.isJsonObject()) {
                    jsonObject = jsonElement.getAsJsonObject();
                }
                key = keys.get(keys.size() - 1);
            }
            if (jsonObject != null) {
                jsonObject.remove(key);
            }
        }
    }

    private Number getNumber(String key) {
        JsonElement jsonElement = getElement(key);
        if (jsonElement != null && jsonElement.isJsonPrimitive() && jsonElement.getAsJsonPrimitive().isNumber()) {
            return jsonElement.getAsJsonPrimitive().getAsNumber();
        }
        return null;
    }

    @Override
    public String toString() {
        return jsonObject.toString();
    }
}
