package hu.agfcodeworks.operangel.application.validation;

import hu.agfcodeworks.operangel.application.dto.LocationDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Builder(setterPrefix = "with")
public class PerformanceValidationMarker {

    @NonNull
    private final UUID naturalId;

    private final LocalDate date;

    private final LocationDto location;
}
