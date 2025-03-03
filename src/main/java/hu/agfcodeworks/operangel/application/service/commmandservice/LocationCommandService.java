package hu.agfcodeworks.operangel.application.service.commmandservice;

import hu.agfcodeworks.operangel.application.dto.ArtistListDto;
import hu.agfcodeworks.operangel.application.dto.LocationDto;
import hu.agfcodeworks.operangel.application.mapper.LocationDtoMapper;
import hu.agfcodeworks.operangel.application.model.Location;
import hu.agfcodeworks.operangel.application.repository.ArtistRepository;
import hu.agfcodeworks.operangel.application.repository.LocationRepository;
import hu.agfcodeworks.operangel.application.service.cache.LocationCache;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

@Transactional
@Service
@AllArgsConstructor
public class LocationCommandService {

    private final LocationRepository locationRepository;

    private final LocationCache locationCache;

    private final LocationDtoMapper locationDtoMapper;

    public LocationDto save(@NonNull LocationDto locationDto) {
        if (Objects.nonNull(locationDto.getNaturalId())) {
            return update(locationDto);
        }

        return create(locationDto);
    }

    private LocationDto create(LocationDto locationDto) {
        var newLocationDto = createNew(locationDto);
        locationCache.put(newLocationDto.getNaturalId(), newLocationDto);

        return newLocationDto;
    }

    private LocationDto update(LocationDto locationDto) {
        var newLocationDto = findAndUpdate(locationDto);
        locationCache.put(newLocationDto.getNaturalId(), newLocationDto);

        return locationDto;
    }

    private LocationDto findAndUpdate(LocationDto locationDto) {
        return locationRepository.findByNaturalId(locationDto.getNaturalId())
                .map(location -> {
                    location.setName(locationDto.getName());

                    locationRepository.save(location);

                    return locationDtoMapper.entityToDto(location);
                }).orElse(locationDto);
    }

    private LocationDto createNew(@NonNull LocationDto locationDto) {
        var entity = Location.builder()
                .withNaturalId(UUID.randomUUID())
                .withName(locationDto.getName())
                .build();

        locationRepository.save(entity);

        return locationDtoMapper.entityToDto(entity);
    }
}
