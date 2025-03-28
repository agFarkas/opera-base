package hu.agfcodeworks.operangel.application.exception;

import hu.agfcodeworks.operangel.application.validation.error.PlayPerformanceValidationErrorDto;

import java.util.List;

public class PlayPerformanceValidationException extends ValidationException {

    public PlayPerformanceValidationException(List<PlayPerformanceValidationErrorDto> errorDtos) {
        super(errorDtos);
    }
}
