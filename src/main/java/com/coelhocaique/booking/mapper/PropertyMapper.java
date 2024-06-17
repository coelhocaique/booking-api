package com.coelhocaique.booking.mapper;

import com.coelhocaique.booking.dto.PropertyDTO;
import com.coelhocaique.booking.entity.Property;

public class PropertyMapper {

    public static PropertyDTO toDTO(Property property) {
        return PropertyDTO.builder()
                .id(property.getId())
                .name(property.getName())
                .address(property.getAddress())
                .build();
    }
}
