package com.acme.booking.exception;

public class PastBookingException extends RuntimeException {
    public PastBookingException(String message) {
        super(message);
    }
}
