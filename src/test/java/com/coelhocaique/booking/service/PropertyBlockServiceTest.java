package com.coelhocaique.booking.service;

import com.coelhocaique.booking.dto.PropertyBlockDTO;
import com.coelhocaique.booking.dto.PropertyBlockRequestDTO;
import com.coelhocaique.booking.entity.PropertyBlock;
import com.coelhocaique.booking.exception.ValidationException;
import com.coelhocaique.booking.repository.PropertyBlockRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PropertyBlockServiceTest {

    @Mock
    private PropertyBlockRepository repository;

    @InjectMocks
    private PropertyBlockService service;

    private final Long PROPERTY_ID = 1L;
    private final Long PROPERTY_BLOCK_ID = 2L;

    @Test
    public void testCreate() {
        PropertyBlockRequestDTO propertyBlockRequest = createPropertyBlockRequest();

        when(repository.save(any(PropertyBlock.class))).thenAnswer(it -> {
            PropertyBlock propertyBlock = it.getArgument(0);
            propertyBlock.setId(PROPERTY_BLOCK_ID);
            return propertyBlock;
        });
        when(repository.findByPropertyIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(PROPERTY_ID, propertyBlockRequest.getEndDate(), propertyBlockRequest.getStartDate()))
                .thenReturn(Collections.emptyList());

        PropertyBlockDTO result = service.create(PROPERTY_ID, propertyBlockRequest);

        assertNotNull(result);
        assertEquals(PROPERTY_BLOCK_ID, result.getId());
        assertEquals(PROPERTY_ID, result.getPropertyId());
        assertEquals(propertyBlockRequest.getStartDate(), result.getStartDate());
        assertEquals(propertyBlockRequest.getEndDate(), result.getEndDate());
        assertEquals(propertyBlockRequest.getReason(), result.getReason());

        verify(repository).save(any(PropertyBlock.class));
        verify(repository).findByPropertyIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(PROPERTY_ID, propertyBlockRequest.getEndDate(), propertyBlockRequest.getStartDate());
    }

    @Test
    public void testCreateWhenInvalidDateRange() {
        LocalDate startDate = LocalDate.of(2024, 6, 10);
        LocalDate endDate = LocalDate.of(2024, 6, 9);

        PropertyBlockRequestDTO propertyBlockRequest = createPropertyBlockRequest(startDate, endDate, "Painting");

        assertThrows(ValidationException.class, () -> service.create(PROPERTY_ID, propertyBlockRequest));

        verify(repository, times(0)).save(any(PropertyBlock.class));
        verify(repository, times(0)).findByPropertyIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(PROPERTY_ID, propertyBlockRequest.getEndDate(), propertyBlockRequest.getStartDate());
    }

    @Test
    public void testCreateWhenOverlappingDates() {
        LocalDate startDate = LocalDate.of(2024, 6, 10);
        LocalDate endDate = LocalDate.of(2024, 6, 12);

        PropertyBlockRequestDTO propertyBlockRequest = createPropertyBlockRequest(startDate, endDate, "Painting");

        when(repository.findByPropertyIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(PROPERTY_ID, propertyBlockRequest.getEndDate(), propertyBlockRequest.getStartDate()))
                .thenReturn(List.of(createEntity()));

        assertThrows(ValidationException.class, () -> service.create(PROPERTY_ID, propertyBlockRequest));

        verify(repository, times(0)).save(any(PropertyBlock.class));
        verify(repository, times(1)).findByPropertyIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(PROPERTY_ID, propertyBlockRequest.getEndDate(), propertyBlockRequest.getStartDate());
    }

    @Test
    public void testUpdateWhenPaymentBlockNotFound() {
        PropertyBlockRequestDTO propertyBlockRequest = createPropertyBlockRequest();

        when(repository.findByPropertyIdAndId(PROPERTY_ID, PROPERTY_BLOCK_ID)).thenReturn(Optional.empty());

        assertThrows(ValidationException.class, () -> service.update(PROPERTY_ID, PROPERTY_BLOCK_ID, propertyBlockRequest));

        verify(repository, times(0)).save(any(PropertyBlock.class));
        verify(repository, times(0)).findByPropertyIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(PROPERTY_ID, propertyBlockRequest.getEndDate(), propertyBlockRequest.getStartDate());
        verify(repository).findByPropertyIdAndId(PROPERTY_ID, PROPERTY_BLOCK_ID);
    }

    @Test
    public void testUpdateWhenInvalidDateRange() {
        LocalDate startDate = LocalDate.of(2024, 6, 10);
        LocalDate endDate = LocalDate.of(2024, 6, 9);

        PropertyBlockRequestDTO propertyBlockRequest = createPropertyBlockRequest(startDate, endDate, "Painting");
        PropertyBlock propertyBlock = createEntity();

        when(repository.findByPropertyIdAndId(PROPERTY_ID, PROPERTY_BLOCK_ID)).thenReturn(Optional.of(propertyBlock));

        assertThrows(ValidationException.class, () -> service.update(PROPERTY_ID, PROPERTY_BLOCK_ID, propertyBlockRequest));

        verify(repository, times(0)).save(any(PropertyBlock.class));
        verify(repository, times(0)).findByPropertyIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(PROPERTY_ID, propertyBlockRequest.getEndDate(), propertyBlockRequest.getStartDate());
        verify(repository).findByPropertyIdAndId(PROPERTY_ID, PROPERTY_BLOCK_ID);
    }

    @Test
    public void testUpdateWhenOverlappingDateRange() {
        LocalDate startDate = LocalDate.of(2024, 6, 10);
        LocalDate endDate = LocalDate.of(2024, 6, 12);

        PropertyBlockRequestDTO propertyBlockRequest = createPropertyBlockRequest(startDate, endDate, "Painting");
        PropertyBlock propertyBlock = createEntity();

        when(repository.findByPropertyIdAndId(PROPERTY_ID, PROPERTY_BLOCK_ID)).thenReturn(Optional.of(propertyBlock));

        when(repository.findByPropertyIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(PROPERTY_ID, propertyBlockRequest.getEndDate(), propertyBlockRequest.getStartDate()))
                .thenReturn(List.of(propertyBlock, propertyBlock));

        assertThrows(ValidationException.class, () -> service.update(PROPERTY_ID, PROPERTY_BLOCK_ID, propertyBlockRequest));

        verify(repository, times(0)).save(any(PropertyBlock.class));
        verify(repository).findByPropertyIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(PROPERTY_ID, propertyBlockRequest.getEndDate(), propertyBlockRequest.getStartDate());
        verify(repository).findByPropertyIdAndId(PROPERTY_ID, PROPERTY_BLOCK_ID);
    }

    @Test
    public void testUpdate() {
        PropertyBlockRequestDTO propertyBlockRequest = createPropertyBlockRequest();
        PropertyBlock propertyBlock = createEntity();

        when(repository.save(any(PropertyBlock.class))).thenAnswer(it -> it.getArgument(0));

        when(repository.findByPropertyIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(PROPERTY_ID, propertyBlockRequest.getEndDate(), propertyBlockRequest.getStartDate()))
                .thenReturn(List.of(propertyBlock));

        when(repository.findByPropertyIdAndId(PROPERTY_ID, PROPERTY_BLOCK_ID)).thenReturn(Optional.of(propertyBlock));

        PropertyBlockDTO result = service.update(PROPERTY_ID, PROPERTY_BLOCK_ID, propertyBlockRequest);

        assertNotNull(result);
        assertEquals(PROPERTY_BLOCK_ID, result.getId());
        assertEquals(PROPERTY_ID, result.getPropertyId());
        assertEquals(propertyBlockRequest.getStartDate(), result.getStartDate());
        assertEquals(propertyBlockRequest.getEndDate(), result.getEndDate());
        assertEquals(propertyBlockRequest.getReason(), result.getReason());

        verify(repository).save(any(PropertyBlock.class));
        verify(repository).findByPropertyIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(PROPERTY_ID, propertyBlockRequest.getEndDate(), propertyBlockRequest.getStartDate());
        verify(repository).findByPropertyIdAndId(PROPERTY_ID, PROPERTY_BLOCK_ID);
    }

    @Test
    public void testDelete() {
        PropertyBlock propertyBlock = createEntity();
        when(repository.findByPropertyIdAndId(PROPERTY_ID, PROPERTY_BLOCK_ID)).thenReturn(Optional.of(propertyBlock));

        service.delete(PROPERTY_ID, PROPERTY_BLOCK_ID);

        verify(repository).delete(propertyBlock);
        verify(repository).findByPropertyIdAndId(PROPERTY_ID, PROPERTY_BLOCK_ID);
    }

    @Test
    public void testDeleteWhenPaymentBlockNotFound() {
        PropertyBlock propertyBlock = createEntity();
        when(repository.findByPropertyIdAndId(PROPERTY_ID, PROPERTY_BLOCK_ID)).thenReturn(Optional.empty());

        assertThrows(ValidationException.class, () -> service.delete(PROPERTY_ID, PROPERTY_BLOCK_ID));

        verify(repository, times(0)).delete(propertyBlock);
        verify(repository).findByPropertyIdAndId(PROPERTY_ID, PROPERTY_BLOCK_ID);
    }

    @Test
    public void testFindByPropertyId() {
        PropertyBlock propertyBlock = createEntity();

        when(repository.findByPropertyId(PROPERTY_ID)).thenReturn(List.of(propertyBlock));

        List<PropertyBlockDTO> result = service.findByPropertyId(1L);
        assertEquals(1, result.size());

        PropertyBlockDTO propertyBlockDTO = result.get(0);

        assertEquals(propertyBlock.getId(), propertyBlockDTO.getId());
        assertEquals(propertyBlock.getPropertyId(), propertyBlockDTO.getPropertyId());
        assertEquals(propertyBlock.getStartDate(), propertyBlockDTO.getStartDate());
        assertEquals(propertyBlock.getEndDate(), propertyBlockDTO.getEndDate());
        assertEquals(propertyBlock.getReason(), propertyBlockDTO.getReason());

        verify(repository).findByPropertyId(PROPERTY_ID);
    }

    @Test
    public void testIsPropertyBlocked() {
        LocalDate startDate = LocalDate.of(2024, 6, 5);
        LocalDate endDate = LocalDate.of(2024, 6, 7);
        when(repository.findByPropertyIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(PROPERTY_ID, endDate, startDate))
                .thenReturn(List.of(createEntity()));

        boolean result = service.isPropertyBlocked(1L, startDate, endDate);

        assertTrue(result);
        verify(repository).findByPropertyIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(PROPERTY_ID, endDate, startDate);
    }

    private PropertyBlock createEntity() {
        return PropertyBlock.builder()
                .id(PROPERTY_BLOCK_ID)
                .propertyId(PROPERTY_ID)
                .startDate(LocalDate.of(2024, 6, 1))
                .endDate(LocalDate.of(2024, 6, 10))
                .reason("Maintenance")
                .build();
    }

    private PropertyBlockRequestDTO createPropertyBlockRequest() {
        return createPropertyBlockRequest(LocalDate.of(2024, 6, 1), LocalDate.of(2024, 6, 10), "Maintenance");
    }

    private PropertyBlockRequestDTO createPropertyBlockRequest(LocalDate startDate, LocalDate endDate, String reason) {
        return PropertyBlockRequestDTO.builder()
                .startDate(startDate)
                .endDate(endDate)
                .reason(reason)
                .build();
    }

}