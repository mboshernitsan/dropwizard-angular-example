package com.example.pointypatient.util;

import static com.example.pointypatient.util.TestUtil.sameJsonAs;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class TestUtilTest {
    @Test
    public void testEmptyJsonDocumentsEqual() {
        assertThat("{}").is(sameJsonAs("{}"));
    }

    @Test
    public void testEmptyJsonArrayAndObjectNotEqual() {
        assertThat("{}").isNot(sameJsonAs("[]"));
    }
    
    @Test
    public void testSimpleJsonDocumentsEqual() {
        assertThat("{\"foo\": 123 }").is(sameJsonAs("{\"foo\": 123 }"));
    }

    @Test
    public void testSimpleJsonDocumentsNotEqual() {
        assertThat("{\"foo\": 123 }").isNot(sameJsonAs("{\"foo\": 456 }"));
    }

    @Test
    public void testJsonFieldOrderIgnored() {
        assertThat("{\"foo\": 123, \"bar\": 456 }").is(sameJsonAs("{\"bar\": 456, \"foo\": 123 }"));
    }

    @Test
    public void testDeepJsonDocumentsEqual() {
        assertThat("{\"foo\": { \"bar\": 456 } }").is(sameJsonAs("{\"foo\": { \"bar\": 456  } }"));
    }

    @Test
    public void testDeepsonDocumentsNotEqual() {
        assertThat("{\"foo\": { \"bar\": 456 } }").isNot(sameJsonAs("{\"foo\": { \"xyxy\": 456  } }"));
    }

    
}
