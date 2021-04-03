package chat.tidy.json.impl;

import com.google.common.base.Preconditions;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.UUID;

public class UUIDDeserializer implements JsonDeserializer<UUID> {

    private static final int[] DASHES_INDEXES = {20, 16, 12, 8};

    @Override
    public UUID deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Preconditions.checkNotNull(json);
        return UUID.fromString(verifyDashed(json.getAsString()));
    }

    private static String verifyDashed(String uuid) {
        if (uuid == null) return null;
        if (uuid.length() < 32) return null;
        if (uuid.contains("-")) return uuid;
        StringBuilder uuidWithDashesBuilder = new StringBuilder(uuid);
        for (int dashIndex : DASHES_INDEXES) {
            uuidWithDashesBuilder.insert(dashIndex, "-");
        }
        return uuidWithDashesBuilder.toString();
    }
}