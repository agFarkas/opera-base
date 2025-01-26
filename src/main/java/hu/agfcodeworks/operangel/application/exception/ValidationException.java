package hu.agfcodeworks.operangel.application.exception;

import hu.agfcodeworks.operangel.application.dto.ErrorDto;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ValidationException extends RuntimeException {

    private static final String MESSAGE_PATTERN = "%s: %s";

    public ValidationException(List<ErrorDto> errorDtos) {
        super(convertToMessage(errorDtos));
    }

    private static String convertToMessage(List<ErrorDto> errorDtos) {
        return errorDtos.stream()
                .sorted(Comparator.comparing(err -> err.getFieldName().toLowerCase()))
                .map(err -> MESSAGE_PATTERN.formatted(err.getFieldName(), err.getErrorDescription()))
                .collect(Collectors.joining("\r\n"));
    }
}
