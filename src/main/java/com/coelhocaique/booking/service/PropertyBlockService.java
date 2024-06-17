package com.coelhocaique.booking.service;

import com.coelhocaique.booking.dto.PropertyBlockDTO;
import com.coelhocaique.booking.dto.PropertyBlockRequestDTO;
import com.coelhocaique.booking.entity.PropertyBlock;
import com.coelhocaique.booking.exception.ValidationException;
import com.coelhocaique.booking.mapper.PropertyBlockMapper;
import com.coelhocaique.booking.repository.PropertyBlockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
public class PropertyBlockService {

    private final PropertyBlockRepository repository;

    public PropertyBlockDTO create(Long propertyId, PropertyBlockRequestDTO propertyBlockRequest) {
        validateOverlappingDates(propertyBlockRequest.getStartDate(), propertyBlockRequest.getEndDate(), propertyId, false);
        PropertyBlock propertyBlock = PropertyBlockMapper.toEntity(propertyId, propertyBlockRequest);
        return PropertyBlockMapper.toDTO(repository.save(propertyBlock));
    }

    public PropertyBlockDTO update(Long propertyId, Long id, PropertyBlockRequestDTO propertyBlockRequest) {
        PropertyBlock propertyBlock = findOne(propertyId, id);
        LocalDate newStartDate = propertyBlockRequest.getStartDate();
        LocalDate newEndDate = propertyBlockRequest.getEndDate();

        if (nonNull(newStartDate) && nonNull(newEndDate)) {
            validateOverlappingDates(newStartDate, newEndDate, propertyId, true);
            propertyBlock.setStartDate(newStartDate);
            propertyBlock.setEndDate(newEndDate);
        }

        if (StringUtils.hasText(propertyBlockRequest.getReason())) {
            propertyBlock.setReason(propertyBlockRequest.getReason());
        }

        return PropertyBlockMapper.toDTO(repository.save(propertyBlock));
    }

    public void delete(Long propertyId, Long id) {
        PropertyBlock propertyBlock = findOne(propertyId, id);
        repository.delete(propertyBlock);
    }

    public List<PropertyBlockDTO> findByPropertyId(Long propertyId) {
        return repository.findByPropertyId(propertyId)
                .stream()
                .map(PropertyBlockMapper::toDTO)
                .collect(Collectors.toList());
    }

    public boolean isPropertyBlocked(Long propertyId, LocalDate startDate, LocalDate endDate) {
        return !repository.findByPropertyIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(propertyId,
                endDate,
                startDate).isEmpty();
    }

    private PropertyBlock findOne(Long propertyId, Long id) {
        return repository.findByPropertyIdAndId(propertyId, id)
                .orElseThrow(ValidationException::propertyBlockDoesNotExist);
    }

    private void validateOverlappingDates(LocalDate startDate, LocalDate endDate, Long propertyId, boolean isUpdate) {
        if (endDate.isBefore(startDate)) {
            throw ValidationException.invalidDateInterval();
        }

        List<PropertyBlock> propertyBlocks = repository.findByPropertyIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                propertyId,
                endDate,
                startDate);

        int sizeToCompare = isUpdate ? 1 : 0;

        if (propertyBlocks.size() > sizeToCompare) {
            throw ValidationException.propertyAlreadyBlocked();
        }
    }
}
