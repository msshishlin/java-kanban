package ru.yandex.practicum.utils.json;

// region imports

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.Duration;

// endregion

public class DurationTypeAdapter implements JsonSerializer<Duration>, JsonDeserializer<Duration> {
    @Override
    public Duration deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
        return Duration.parse(json.getAsString());
    }

    @Override
    public JsonElement serialize(Duration duration, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(duration.toString());
    }
}