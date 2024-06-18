package com.coelhocaique.booking.validator;

import com.coelhocaique.booking.exception.ValidationException;
import com.coelhocaique.booking.service.PropertyService;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PropertyIdValidator implements ConstraintValidator<PropertyId, Long> {

    private final PropertyService propertyService;

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        if (propertyService.findById(value).isEmpty()) {
            throw ValidationException.propertyNotFound();
        }
        return true;
    }
}
