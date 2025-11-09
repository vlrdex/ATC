package org.example.Utils;

import com.google.gson.*;
import javafx.geometry.Point2D;
import java.lang.reflect.Type;

public class Point2DAdapter implements JsonSerializer<Point2D>, JsonDeserializer<Point2D> {

    @Override
    public JsonElement serialize(Point2D src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        obj.addProperty("x", src.getX());
        obj.addProperty("y", src.getY());
        return obj;
    }

    @Override
    public Point2D deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        return new Point2D(
                obj.get("x").getAsDouble(),
                obj.get("y").getAsDouble()
        );
    }
}
