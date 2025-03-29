package hu.agfcodeworks.operangel.application.validation.error;

import hu.agfcodeworks.operangel.application.ui.text.TextProvider;
import hu.agfcodeworks.operangel.application.validation.dto.PerformanceValidationMarkerContent;
import lombok.Getter;
import lombok.NonNull;

import java.util.Objects;

import static hu.agfcodeworks.operangel.application.ui.constants.UiConstants.dateFormatter;
import static hu.agfcodeworks.operangel.application.ui.text.TextProviders.locationTextProvider;

@Getter
public class PlayPerformanceValidationErrorDto extends ErrorDto<PerformanceValidationMarkerContent> {

    private static final String NO_DATE_TEXT = "[Nincs dátum]";

    private static final String NO_LOCATION_TEXT = "[Nincs helyszín]";

    private static final String PERFORMANCE_MARKER_TEXT_PATTERN = "%s (%s)";

    private static final TextProvider<PerformanceValidationMarkerContent> fieldMarkerTextProvider =
            p -> PERFORMANCE_MARKER_TEXT_PATTERN.formatted(obtainLocationText(p), obtainDateText(p));

    public PlayPerformanceValidationErrorDto(@NonNull PerformanceValidationMarkerContent fieldMarkerContent, @NonNull String errorDescription) {
        super(new FieldMarker<>(fieldMarkerContent), errorDescription, fieldMarkerTextProvider);
    }

    private static String obtainLocationText(PerformanceValidationMarkerContent performanceValidationMarkerContent) {
        return Objects.nonNull(performanceValidationMarkerContent.getLocation()) ?
                locationTextProvider.provide(performanceValidationMarkerContent.getLocation()) : NO_LOCATION_TEXT;
    }

    private static String obtainDateText(PerformanceValidationMarkerContent performanceValidationMarkerContent) {
        return Objects.nonNull(performanceValidationMarkerContent.getDate()) ?
                dateFormatter.format(performanceValidationMarkerContent.getDate()) : NO_DATE_TEXT;
    }
}
