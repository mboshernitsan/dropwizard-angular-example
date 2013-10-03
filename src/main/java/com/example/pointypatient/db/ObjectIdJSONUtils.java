package com.example.pointypatient.db;

import java.io.IOException;

import org.bson.types.ObjectId;

import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.core.Base64Variants;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class ObjectIdJSONUtils {
    private static final Base64Variant base64Variant = Base64Variants.MODIFIED_FOR_URL;

    public static class ObjectIdSerializer extends JsonSerializer<ObjectId> {
        @Override
        public void serialize(ObjectId value, JsonGenerator jgen, SerializerProvider provider) throws IOException,
            JsonProcessingException {
            byte[] byteArray = value.toByteArray();
            jgen.writeBinary(base64Variant, byteArray, 0, byteArray.length);
        }
    }

    public final static ObjectIdSerializer serializer = new ObjectIdSerializer();

    public static class ObjectIdDeserializer extends JsonDeserializer<ObjectId> {
        @Override
        public ObjectId deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException,
            JsonProcessingException {
            return new ObjectId(jp.getBinaryValue(base64Variant));
        }
    }

    public final static ObjectIdDeserializer deserializer = new ObjectIdDeserializer();

    public static ObjectId decode(String input) {
        return new ObjectId(base64Variant.decode(input));
    }
}
