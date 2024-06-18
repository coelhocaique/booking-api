package com.coelhocaique.booking.exception;

import javax.validation.ConstraintDeclarationException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

public class ValidationException extends ConstraintDeclarationException {

    public static final String BOOKING_WITH_SPECIFIED_ID_NOT_FOUND = "Booking with specified ID not found.";
    public static final String END_DATE_CAN_T_BE_LESS_THAN_OR_EQUAL_START_DATE = "End date can't be less than or equal start date.";
    public static final String PROPERTY_IS_ALREADY_BOOKED_FOR_THE_PERIOD_REQUESTED = "Property is already booked for the period requested.";
    public static final String PROPERTY_IS_ALREADY_BLOCKED_FOR_THE_PERIOD_REQUESTED = "Property is already blocked for the period requested.";
    public static final String PROPERTY_BLOCK_DOES_NOT_EXIST = "Property block doesn't exist.";
    public static final String PROPERTY_IS_BLOCKED_FOR_THIS_PERIOD = "Property is blocked for this period.";
    public static final String PROPERTY_ID_NOT_FOUND = "Property with specified ID not found.";

    @Getter
    private final HttpStatus status;

    public ValidationException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public ValidationException(String message) {
        this(message, HttpStatus.BAD_REQUEST);
    }

    public static ValidationException bookingNotFound() {
        return new ValidationException(BOOKING_WITH_SPECIFIED_ID_NOT_FOUND, HttpStatus.NOT_FOUND);
    }

    public static ValidationException invalidDateInterval() {
        return new ValidationException(END_DATE_CAN_T_BE_LESS_THAN_OR_EQUAL_START_DATE);
    }

    public static ValidationException propertyAlreadyBooked() {
        return new ValidationException(PROPERTY_IS_ALREADY_BOOKED_FOR_THE_PERIOD_REQUESTED, HttpStatus.CONFLICT);
    }

    public static ValidationException propertyAlreadyBlocked() {
        return new ValidationException(PROPERTY_IS_ALREADY_BLOCKED_FOR_THE_PERIOD_REQUESTED, HttpStatus.CONFLICT);
    }

    public static ValidationException propertyBlockDoesNotExist() {
        return new ValidationException(PROPERTY_BLOCK_DOES_NOT_EXIST, HttpStatus.NOT_FOUND);
    }

    public static ValidationException propertyBlocked() {
        return new ValidationException(PROPERTY_IS_BLOCKED_FOR_THIS_PERIOD, HttpStatus.CONFLICT);
    }

    public static ValidationException propertyNotFound() {
        return new ValidationException(PROPERTY_ID_NOT_FOUND, HttpStatus.NOT_FOUND);
    }
}
