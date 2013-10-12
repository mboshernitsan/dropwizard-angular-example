package com.example.pointypatient.util;

import static org.hamcrest.CoreMatchers.equalTo;
import io.dropwizard.jackson.Jackson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.assertj.core.api.Condition;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.rules.ExpectedException;

import com.example.pointypatient.db.MongoUtils;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;

public class TestUtil {
    private static final ObjectMapper objectMapper = MongoUtils.configureObjectMapper(Jackson.newObjectMapper());

    private static class SameJsonCondition extends Condition<String> {
        private String expected;

        public SameJsonCondition(String expected) {
            this.expected = expected;
        }

        @Override
        public boolean matches(String value) {
            try {
                JsonNode expectedNode = objectMapper.readTree(expected);
                JsonNode valueNode = objectMapper.readTree(value);
                return expectedNode.equals(valueNode);
            } catch (JsonProcessingException e) {
                return false;
            } catch (IOException e) {
                return false;
            }
        }
    }

    public static SameJsonCondition sameJsonAs(String expected) {
        return new SameJsonCondition(expected);
    }

    public static String jsonFromFile(String filename) {
        try {
            return Resources.toString(Resources.getResource(filename), Charsets.UTF_8).trim();
        } catch (IOException e) {
            return null;
        }
    }

    public static String jsonFromObject(Object obj) throws JsonGenerationException, JsonMappingException, IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        objectMapper.writeValue(out, obj);
        return out.toString(Charsets.UTF_8.name());
    }

    public static <T> T objectFromJson(String json, Class<T> cls) throws JsonParseException, JsonMappingException,
        IOException {
        return objectMapper.readValue(json, cls);
    }

    public static <T> List<T> listFromJson(String json, Class<T> cls) throws JsonParseException, JsonMappingException,
        IOException {
        return objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, cls));
    }

    public static DBObject dbObjectFromJson(String json) throws JsonProcessingException, IOException {
        JsonNode node = objectMapper.readTree(json);
        Object dbObject = dbObjectFromJsonNode(node);
        if (dbObject instanceof DBObject) {
            return (DBObject) dbObject;
        }
        throw new RuntimeException("Expected an object at the top-level");
    }

    private static Object dbObjectFromJsonNode(JsonNode node) throws IOException {
        switch (node.getNodeType()) {
        case STRING:
            return node.textValue();
        case BINARY:
            return node.binaryValue();
        case BOOLEAN:
            return node.booleanValue();
        case MISSING:
        case NULL:
        case POJO:
            return null;
        case NUMBER:
            return node.numberValue();
        case OBJECT:
            BasicDBObject dbObject = new BasicDBObject();
            for (Iterator<Map.Entry<String, JsonNode>> i = node.fields(); i.hasNext();) {
                Map.Entry<String, JsonNode> f = i.next();
                dbObject.put(f.getKey(), dbObjectFromJsonNode(f.getValue()));
            }
            return dbObject;
        case ARRAY:
            BasicDBList dbList = new BasicDBList();
            for (Iterator<JsonNode> i = node.elements(); i.hasNext();) {
                dbList.add(i.next());
            }
            return dbList;
        default:
            throw new UnsupportedOperationException();
        }
    }

    private static class WebApplicationExceptionStatusMatcher extends TypeSafeMatcher<WebApplicationException> {
        private final Matcher<Integer> fMatcher;

        public WebApplicationExceptionStatusMatcher(Matcher<Integer> matcher) {
            fMatcher = matcher;
        }

        public void describeTo(Description description) {
            description.appendText("status ");
            description.appendDescriptionOf(fMatcher);
        }

        @Override
        protected boolean matchesSafely(WebApplicationException item) {
            return fMatcher.matches(item.getResponse().getStatus());
        }

        @Override
        protected void describeMismatchSafely(WebApplicationException item, Description description) {
            description.appendText("status ");
            fMatcher.describeMismatch(item.getResponse().getStatus(), description);
        }
    }

    private static class UniformInterfaceExceptionStatusMatcher extends TypeSafeMatcher<UniformInterfaceException> {
        private final Matcher<Integer> fMatcher;

        public UniformInterfaceExceptionStatusMatcher(Matcher<Integer> matcher) {
            fMatcher = matcher;
        }

        public void describeTo(Description description) {
            description.appendText("status ");
            description.appendDescriptionOf(fMatcher);
        }

        @Override
        protected boolean matchesSafely(UniformInterfaceException item) {
            return fMatcher.matches(item.getResponse().getStatus());
        }

        @Override
        protected void describeMismatchSafely(UniformInterfaceException item, Description description) {
            description.appendText("status ");
            fMatcher.describeMismatch(item.getResponse().getStatus(), description);
        }
    }

    public static void expectWebApplicationException(ExpectedException rule, Response.Status status) {
        rule.expect(WebApplicationException.class);
        rule.expect(new WebApplicationExceptionStatusMatcher(equalTo(status.getStatusCode())));
    }

    public static void expectUniformInterfaceException(ExpectedException rule, ClientResponse.Status status) {
        rule.expect(UniformInterfaceException.class);
        rule.expect(new UniformInterfaceExceptionStatusMatcher(equalTo(status.getStatusCode())));
    }
}
