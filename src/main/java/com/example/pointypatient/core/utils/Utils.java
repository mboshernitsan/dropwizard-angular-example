package com.example.pointypatient.core.utils;

import io.dropwizard.jackson.Jackson;

import java.io.ByteArrayOutputStream;

import com.example.pointypatient.db.MongoUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;

public class Utils {
    private static final ObjectMapper toStringMapper = MongoUtils.configureObjectMapper(Jackson.newObjectMapper());

    public static String toString(Object obj) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            toStringMapper.writeValue(out, obj);
            return out.toString(Charsets.UTF_8.name());
        } catch (Exception e) {
            return String.format("<json conversion failed: @%x>", obj.hashCode());
        }
    }
}
