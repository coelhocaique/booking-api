package com.coelhocaique.booking.service;

import com.coelhocaique.booking.dto.PropertyDTO;
import com.coelhocaique.booking.entity.Property;
import com.coelhocaique.booking.mapper.PropertyMapper;
import com.coelhocaique.booking.repository.PropertyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor
public class PropertyService {

    private final PropertyRepository repository;

    public Optional<Property> findById(Long id) {
        return repository.findById(id);
    }

    public List<PropertyDTO> findAll() {
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .map(PropertyMapper::toDTO)
                .collect(Collectors.toList());
    }
}
