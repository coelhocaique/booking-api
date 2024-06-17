package com.coelhocaique.booking.repository;

import com.coelhocaique.booking.entity.PropertyBlock;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PropertyBlockRepository extends CrudRepository<PropertyBlock, Long> {

    List<PropertyBlock> findByPropertyId(Long propertyId);

    Optional<PropertyBlock> findByPropertyIdAndId(Long propertyId, Long Id);

    List<PropertyBlock> findByPropertyIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(Long propertyId, LocalDate endDate, LocalDate startDate);
}
