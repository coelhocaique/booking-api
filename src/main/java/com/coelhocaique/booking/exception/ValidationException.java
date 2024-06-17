package com.coelhocaique.booking.exception;

public class ValidationException extends RuntimeException {

    public ValidationException(String message) {
        super(message);
    }

    public static ValidationException bookingNotFound() {
        return new ValidationException("Booking with specified ID not found.");
    }

    public static ValidationException invalidDateInterval() {
        return new ValidationException("End date can't be less than or equal start date.");
    }

    public static ValidationException propertyAlreadyBooked() {
        return new ValidationException("Property is already booked for the period requested.");
    }

    public static ValidationException propertyAlreadyBlocked() {
        return new ValidationException("Property is already blocked for the period requested.");
    }

    public static ValidationException propertyBlockDoesNotExist() {
        return new ValidationException("Property block doesn't exist.");
    }

    public static ValidationException propertyBlocked() {
        return new ValidationException("Property is blocked for this period.");
    }

}
