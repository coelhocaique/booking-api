package com.coelhocaique.booking.controller;

import com.coelhocaique.booking.dto.PropertyDTO;
import com.coelhocaique.booking.service.PropertyService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/properties", produces = MediaType.APPLICATION_JSON_VALUE)
public class PropertyController {

    private final PropertyService propertyService;

    @GetMapping
    public ResponseEntity<List<PropertyDTO>> findAll() {
        List<PropertyDTO> propertyBlocks = propertyService.findAll();
        return ResponseEntity.ok(propertyBlocks);
    }
}
