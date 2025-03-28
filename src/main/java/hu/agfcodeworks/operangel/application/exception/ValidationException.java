package hu.agfcodeworks.operangel.application.exception;

import hu.agfcodeworks.operangel.application.validation.error.ErrorDto;
import lombok.Getter;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static hu.agfcodeworks.operangel.application.ui.constants.UiConstants.RETURN_AND_LINE_BREAK;

public abstract class ValidationException extends RuntimeException {

    private static final String MESSAGE_PATTERN = "%s: %s";

    @Getter
    private final List<? extends ErrorDto<?>> errorDtos;

    public ValidationException(List<? extends ErrorDto<?>> errorDtos) {
        super(convertToMessage(errorDtos));

        this.errorDtos = errorDtos;
    }

    private static String convertToMessage(List<? extends ErrorDto<?>> dialogValidationErrorDtos) {
        return dialogValidationErrorDtos.stream()
                .sorted(Comparator.comparing(err -> err.getFieldMarkerText().toLowerCase()))
                .map(err -> MESSAGE_PATTERN.formatted(err.getFieldMarkerText(), err.getErrorDescription()))
                .collect(Collectors.joining(RETURN_AND_LINE_BREAK));
    }
}
