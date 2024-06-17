package com.coelhocaique.booking.controller;

import com.coelhocaique.booking.dto.PropertyBlockRequestDTO;
import com.coelhocaique.booking.dto.PropertyBlockDTO;
import com.coelhocaique.booking.service.PropertyBlockService;
import com.coelhocaique.booking.validator.PropertyIdValidator;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/properties/{propertyId}/blocks", produces = MediaType.APPLICATION_JSON_VALUE)
public class PropertyBlockController {

    private final PropertyBlockService blockService;

    @PostMapping
    public ResponseEntity<PropertyBlockDTO> create(@PathVariable @Validated(PropertyIdValidator.class) Long propertyId, @RequestBody PropertyBlockRequestDTO createPropertyBlockRequest) {
        PropertyBlockDTO createdBlock = blockService.create(propertyId, createPropertyBlockRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBlock);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PropertyBlockDTO> update(@PathVariable @Validated(PropertyIdValidator.class) Long propertyId, @PathVariable Long id, @RequestBody PropertyBlockRequestDTO createPropertyBlockRequest) {
        PropertyBlockDTO updatedBlock = blockService.update(propertyId, id, createPropertyBlockRequest);
        return ResponseEntity.ok(updatedBlock);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable @Validated(PropertyIdValidator.class) Long propertyId, @PathVariable Long id) {
        blockService.delete(propertyId, id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<PropertyBlockDTO>> findAll(@PathVariable @Validated(PropertyIdValidator.class) Long propertyId) {
        List<PropertyBlockDTO> propertyBlocks = blockService.findByPropertyId(propertyId);
        return ResponseEntity.ok(propertyBlocks);
    }
}
