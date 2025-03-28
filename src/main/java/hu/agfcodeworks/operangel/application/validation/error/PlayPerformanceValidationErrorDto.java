package hu.agfcodeworks.operangel.application.validation.error;

import hu.agfcodeworks.operangel.application.ui.text.TextProvider;
import hu.agfcodeworks.operangel.application.validation.PerformanceValidationMarker;
import lombok.Getter;
import lombok.NonNull;

import java.util.Objects;

import static hu.agfcodeworks.operangel.application.ui.constants.UiConstants.dateFormatter;
import static hu.agfcodeworks.operangel.application.ui.text.TextProviders.locationTextProvider;

@Getter
public class PlayPerformanceValidationErrorDto extends ErrorDto<PerformanceValidationMarker> {

    private static final String NO_DATE_TEXT = "Nincs dátum";

    private static final String NO_LOCATION_TEXT = "Nincs helyszín";

    private static final String PERFORMANCE_MARKER_TEXT_PATTERN = "%s (%s)";

    private static final TextProvider<PerformanceValidationMarker> fieldMarkerTextProvider = p -> PERFORMANCE_MARKER_TEXT_PATTERN.formatted(
            obtainLocationText(p), obtainDateText(p)
    );

    private static String obtainLocationText(PerformanceValidationMarker performanceValidationMarker) {
        return Objects.nonNull(performanceValidationMarker.getLocation()) ?
                locationTextProvider.provide(performanceValidationMarker.getLocation()) : NO_LOCATION_TEXT;
    }

    private static String obtainDateText(PerformanceValidationMarker performanceValidationMarker) {
        return Objects.nonNull(performanceValidationMarker.getDate()) ?
                dateFormatter.format(performanceValidationMarker.getDate()) : NO_DATE_TEXT;
    }

    public PlayPerformanceValidationErrorDto(@NonNull PerformanceValidationMarker fieldMarker, @NonNull String errorDescription) {
        super(fieldMarker, errorDescription, fieldMarkerTextProvider);
    }
}
