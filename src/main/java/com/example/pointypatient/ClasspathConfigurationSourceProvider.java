package com.example.pointypatient;

import io.dropwizard.configuration.ConfigurationSourceProvider;

import java.io.IOException;
import java.io.InputStream;

public class ClasspathConfigurationSourceProvider implements ConfigurationSourceProvider {
    @Override
    public InputStream open(String path) throws IOException {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
    }
}
