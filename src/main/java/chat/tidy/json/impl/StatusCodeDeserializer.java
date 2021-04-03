package chat.tidy.json.impl;

import chat.tidy.message.StatusCode;
import com.google.common.base.Preconditions;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class StatusCodeDeserializer implements JsonDeserializer<StatusCode> {

    @Override
    public StatusCode deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Preconditions.checkNotNull(json);
        for (StatusCode statusCode : StatusCode.values()) {
            if (statusCode.getId() == json.getAsInt()) {
                return statusCode;
            }
        }
        try {
            return StatusCode.valueOf(json.getAsString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}