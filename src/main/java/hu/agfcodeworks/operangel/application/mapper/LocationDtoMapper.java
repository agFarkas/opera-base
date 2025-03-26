package hu.agfcodeworks.operangel.application.mapper;

import hu.agfcodeworks.operangel.application.dto.LocationDto;
import hu.agfcodeworks.operangel.application.model.Location;
import hu.agfcodeworks.operangel.application.ui.util.TextUtil;
import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
public class LocationDtoMapper extends AbstractMapper<Location, LocationDto> {

    @Override
    public LocationDto entityToDto(@NonNull Location location) {
        return LocationDto.builder()
                .withNaturalId(location.getNaturalId())
                .withName(location.getName())
                .withNameUnified(TextUtil.unify(location.getName()))
                .build();
    }
}
