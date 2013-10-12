package com.example.pointypatient.db;

import java.io.IOException;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.ValidationExtension;

import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.core.Base64Variants;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.mongodb.Mongo;

public class MongoUtils {
    private static final Base64Variant base64Variant = Base64Variants.MODIFIED_FOR_URL;

    public static class ObjectIdSerializer extends JsonSerializer<ObjectId> {
        @Override
        public void serialize(ObjectId value, JsonGenerator jgen, SerializerProvider provider) throws IOException,
            JsonProcessingException {
            byte[] byteArray = value.toByteArray();
            jgen.writeBinary(base64Variant, byteArray, 0, byteArray.length);
        }
    }

    private final static ObjectIdSerializer objectIdSerializer = new ObjectIdSerializer();

    public static class ObjectIdDeserializer extends JsonDeserializer<ObjectId> {
        @Override
        public ObjectId deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException,
            JsonProcessingException {
            return new ObjectId(jp.getBinaryValue(base64Variant));
        }
    }

    private final static ObjectIdDeserializer objectIdDeserializer = new ObjectIdDeserializer();

    public static ObjectId decodeObjectId(String input) {
        return new ObjectId(base64Variant.decode(input));
    }

    public static String encodeObjectId(ObjectId id) {
        return base64Variant.encode(id.toByteArray());
    }
    
    public static ObjectMapper configureObjectMapper(ObjectMapper objectMapper) {
        SimpleModule module = new SimpleModule();
        module.addSerializer(ObjectId.class, objectIdSerializer);
        module.addDeserializer(ObjectId.class, objectIdDeserializer);
        objectMapper.registerModule(module);
        return objectMapper;
    }

    public static Datastore createDatastore(Mongo mongo, String dbName) {
        Morphia morphia = new Morphia();
        new ValidationExtension(morphia);
        return morphia.createDatastore(mongo, dbName);
    }
}
