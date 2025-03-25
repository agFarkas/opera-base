package hu.agfcodeworks.operangel.application.service.query.service;

import hu.agfcodeworks.operangel.application.dto.LocationDto;
import hu.agfcodeworks.operangel.application.dto.LocationSimpleDto;
import hu.agfcodeworks.operangel.application.mapper.LocationDtoMapper;
import hu.agfcodeworks.operangel.application.model.Location;
import hu.agfcodeworks.operangel.application.repository.LocationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class LocationQueryService {

    private final LocationRepository locationRepository;

    private final LocationDtoMapper locationDtoMapper;

    public List<LocationDto> getAllLocations() {
        return locationRepository.findAllLocations()
                .stream()
                .map(locationDtoMapper::entityToDto)
                .collect(Collectors.toList());
    }


    public Optional<Location> findByNaturalId(LocationSimpleDto location) {
        return locationRepository.findByNaturalId(location.getNaturalId());
    }
}
