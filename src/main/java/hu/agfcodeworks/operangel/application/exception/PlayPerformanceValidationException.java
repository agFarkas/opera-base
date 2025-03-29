package hu.agfcodeworks.operangel.application.exception;

import hu.agfcodeworks.operangel.application.validation.dto.PerformanceValidationMarkerContent;
import hu.agfcodeworks.operangel.application.validation.error.ErrorDto;
import hu.agfcodeworks.operangel.application.validation.error.PlayPerformanceValidationErrorDto;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class PlayPerformanceValidationException extends ValidationException {

    public static final Comparator<ErrorDto<?>> DATE_COMPARATOR = ((p1, p2) -> {
        var date1 = getSpecificFieldMarker(p1).getDate();
        var date2 = getSpecificFieldMarker(p2).getDate();

        if(Objects.isNull(date1) || Objects.isNull(date2)) {
            return 0;
        }

        return date1.compareTo(date2);
    });

    public PlayPerformanceValidationException(List<PlayPerformanceValidationErrorDto> errorDtos) {
        super(errorDtos, DATE_COMPARATOR);
    }

    private static PerformanceValidationMarkerContent getSpecificFieldMarker(ErrorDto<?> p) {
        return (PerformanceValidationMarkerContent) p.getFieldMarker().getContent();
    }
}
