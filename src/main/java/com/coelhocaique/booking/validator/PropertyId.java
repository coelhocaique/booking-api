package com.coelhocaique.booking.validator;

import com.coelhocaique.booking.exception.ValidationException;
import javax.validation.Constraint;
import javax.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = PropertyIdValidator.class)
@Target({ TYPE, FIELD, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Documented
public @interface PropertyId {
    String message() default ValidationException.PROPERTY_ID_NOT_FOUND;
    Class <?> [] groups() default {};
    Class <? extends Payload> [] payload() default {};
}
