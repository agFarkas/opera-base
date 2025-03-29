package hu.agfcodeworks.operangel.application.exception;

import hu.agfcodeworks.operangel.application.validation.error.DialogValidationErrorDto;

import java.util.Comparator;
import java.util.List;

public class DialogValidationException extends ValidationException {

    public DialogValidationException(List<DialogValidationErrorDto> errorDtos) {
        super(errorDtos, Comparator.comparing(errorDto -> errorDto.getFieldMarkerText().toLowerCase()));
    }
}
