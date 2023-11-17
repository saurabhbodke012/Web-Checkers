package com.webcheckers.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageTest {
    @Test
    void error() {
        Message message = Message.error("Error!");
        Message message2 = Message.info("Info!");

        assertEquals(Message.Type.ERROR, message.getType());
        assertEquals("Error!", message.getText());
        assertFalse(message.isSuccessful());
        assertTrue(message2.isSuccessful());
    }
}