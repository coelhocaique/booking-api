package com.coelhocaique.booking.mapper;

import com.coelhocaique.booking.dto.PropertyBlockDTO;
import com.coelhocaique.booking.dto.PropertyBlockRequestDTO;
import com.coelhocaique.booking.entity.PropertyBlock;

public class PropertyBlockMapper {

    public static PropertyBlock toEntity(Long propertyId, PropertyBlockRequestDTO propertyBlockRequest) {
        return PropertyBlock.builder()
                .propertyId(propertyId)
                .startDate(propertyBlockRequest.getStartDate())
                .endDate(propertyBlockRequest.getEndDate())
                .reason(propertyBlockRequest.getReason())
                .build();
    }

    public static PropertyBlockDTO toDTO(PropertyBlock propertyBlock) {
        return PropertyBlockDTO.builder()
                .id(propertyBlock.getId())
                .propertyId(propertyBlock.getPropertyId())
                .startDate(propertyBlock.getStartDate())
                .endDate(propertyBlock.getEndDate())
                .reason(propertyBlock.getReason())
                .build();
    }
}
