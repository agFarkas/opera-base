package hu.agfcodeworks.operangel.application.service.cache;

import hu.agfcodeworks.operangel.application.dto.LocationDto;
import hu.agfcodeworks.operangel.application.service.query.service.LocationQueryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class LocationCache extends AbstractCache<LocationDto> {

    private final LocationQueryService locationQueryService;

    @Override
    protected void fillCache() {
        locationQueryService.getAllLocations()
                .forEach(l -> put(l.getNaturalId(), l));
    }
}
