package hu.agfcodeworks.operangel.application.exception;

import hu.agfcodeworks.operangel.application.validation.error.ErrorDto;
import hu.agfcodeworks.operangel.application.validation.error.FieldMarker;
import lombok.Getter;
import org.springframework.util.CollectionUtils;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static hu.agfcodeworks.operangel.application.ui.constants.UiConstants.RETURN_AND_LINE_BREAK;

public abstract class ValidationException extends RuntimeException {

    private static final String INDENTED_ERROR_DESCRIPTION_PATTERN = "    - %s";

    private static final String COMPONENTS_WITH_RETURN_AND_LINE_BREAK_PATTERN = "%s:\r\n%s";

    @Getter
    private final List<? extends ErrorDto<?>> errorDtos;

    public ValidationException(List<? extends ErrorDto<?>> errorDtos, Comparator<? super ErrorDto<?>> comparator) {
        super(convertToMessage(errorDtos, comparator));

        this.errorDtos = errorDtos;
    }

    private static String convertToMessage(List<? extends ErrorDto<?>> errorDtos, Comparator<? super ErrorDto<?>> comparator) {
        return collectErrorDtosByFieldMarkers(errorDtos, comparator)
                .values()
                .stream()
                .filter(l -> !CollectionUtils.isEmpty(l))
                .map(ValidationException::composeErrorMessage)
                .collect(Collectors.joining(RETURN_AND_LINE_BREAK));
    }

    private static Map<FieldMarker<?>, List<ErrorDto<?>>> collectErrorDtosByFieldMarkers(List<? extends ErrorDto<?>> errorDtos, Comparator<? super ErrorDto<?>> comparator) {
        var errorDtosByFieldMarkers = new HashMap<FieldMarker<?>, List<ErrorDto<?>>>();

        errorDtos.stream()
                .sorted(comparator)
                .forEach(errorDto -> {
                    var fieldMarker = errorDto.getFieldMarker();

                    errorDtosByFieldMarkers.computeIfAbsent(fieldMarker, v -> new LinkedList<>())
                            .add(errorDto);
                });

        return errorDtosByFieldMarkers;
    }

    private static String composeErrorMessage(List<ErrorDto<?>> errorDtos) {
        return COMPONENTS_WITH_RETURN_AND_LINE_BREAK_PATTERN.formatted(errorDtos.getFirst().getFieldMarkerText(), joinErrorDtos(errorDtos));
    }

    private static String joinErrorDtos(List<ErrorDto<?>> errorDtos) {
        return errorDtos.stream()
                .map(ErrorDto::getErrorDescription)
                .map(INDENTED_ERROR_DESCRIPTION_PATTERN::formatted)
                .collect(Collectors.joining(RETURN_AND_LINE_BREAK));
    }

}
