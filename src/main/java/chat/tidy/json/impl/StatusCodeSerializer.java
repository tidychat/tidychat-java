package chat.tidy.json.impl;

import chat.tidy.message.StatusCode;
import com.google.common.base.Preconditions;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class StatusCodeSerializer implements JsonSerializer<StatusCode> {

    @Override
    public JsonElement serialize(StatusCode src, Type typeOfSrc, JsonSerializationContext context) {
        Preconditions.checkNotNull(src);
        return new JsonPrimitive(src.getId());
    }
}