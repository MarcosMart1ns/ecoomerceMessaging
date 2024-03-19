package br.com.shop;

import br.com.shop.domain.Message;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class MessageAdapter implements JsonSerializer<Message> {
    @Override
    public JsonElement serialize(Message message, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.add("type",context.serialize(message.getPayload().getClass().getName()));
        object.add("payload",context.serialize(message.getPayload()));
        object.add("correlationId",context.serialize(message.getId()));

        return object;
    }
}
