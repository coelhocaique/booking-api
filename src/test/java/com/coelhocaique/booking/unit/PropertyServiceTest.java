package com.coelhocaique.booking.unit;

import com.coelhocaique.booking.dto.PropertyDTO;
import com.coelhocaique.booking.entity.Property;
import com.coelhocaique.booking.repository.PropertyRepository;
import com.coelhocaique.booking.service.PropertyService;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PropertyServiceTest {

    @Mock
    private PropertyRepository repository;

    @InjectMocks
    private PropertyService service;

    private final Long PROPERTY_ID = 1L;

    @Test
    void findById() {
        Property entity = createEntity();
        when(repository.findById(PROPERTY_ID)).thenReturn(Optional.of(entity));

        Optional<Property> result = service.findById(PROPERTY_ID);

        assertTrue(result.isPresent());

        assertEquals(entity, result.get());

        verify(repository).findById(PROPERTY_ID);
    }

    @Test
    void findByIdWhenEmpty() {
        when(repository.findById(PROPERTY_ID)).thenReturn(Optional.empty());

        Optional<Property> property = service.findById(PROPERTY_ID);

        assertTrue(property.isEmpty());

        verify(repository).findById(PROPERTY_ID);
    }

    @Test
    void findAll() {
        Property entity = createEntity();

        when(repository.findAll()).thenReturn(() -> List.of(entity).iterator());

        List<PropertyDTO> properties = service.findAll();

        assertEquals(1, properties.size());
        PropertyDTO propertyDTO = properties.get(0);
        assertEquals(entity.getId(), propertyDTO.getId());
        assertEquals(entity.getName(), propertyDTO.getName());
        assertEquals(entity.getAddress(), propertyDTO.getAddress());

        verify(repository).findAll();
    }

    private Property createEntity() {
        return Property.builder()
                .id(PROPERTY_ID)
                .name("My Property")
                .address("My Address, 101")
                .build();
    }
}