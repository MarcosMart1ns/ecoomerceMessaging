package br.com.shop;

import br.com.shop.domain.CorrelationId;
import br.com.shop.domain.Message;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class MessageAdapter implements JsonSerializer<Message>, JsonDeserializer<Message> {
    @Override
    public JsonElement serialize(Message message, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.add("type",context.serialize(message.getPayload().getClass().getName()));
        object.add("payload",context.serialize(message.getPayload()));
        object.add("correlationId",context.serialize(message.getId()));

        return object;
    }

    @Override
    public Message deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        String payloadType = obj.get("type").getAsString();
        CorrelationId correlationId = context.deserialize(obj.get("correlationId"),CorrelationId.class);
        try {
            var payload = context.deserialize(obj.get("payload"), Class.forName(payloadType));

            return new Message(correlationId,payload);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
