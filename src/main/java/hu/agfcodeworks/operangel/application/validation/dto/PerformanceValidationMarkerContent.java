package hu.agfcodeworks.operangel.application.validation.dto;

import hu.agfcodeworks.operangel.application.dto.LocationDto;
import hu.agfcodeworks.operangel.application.ui.text.TextProvider;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Builder(setterPrefix = "with")
@EqualsAndHashCode
public class PerformanceValidationMarkerContent {

    @NonNull
    private final UUID naturalId;

    private final LocalDate date;

    private final LocationDto location;

    @Override
    public String toString() {
        return super.toString();
    }
}
