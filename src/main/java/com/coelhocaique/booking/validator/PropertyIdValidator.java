package com.coelhocaique.booking.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.coelhocaique.booking.service.PropertyService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PropertyIdValidator implements ConstraintValidator<PropertyId, Long> {

    private final PropertyService propertyService;

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        return propertyService.findById(value).isPresent();
    }
}
