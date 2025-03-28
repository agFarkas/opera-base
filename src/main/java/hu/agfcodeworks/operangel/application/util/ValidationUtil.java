package hu.agfcodeworks.operangel.application.util;

import hu.agfcodeworks.operangel.application.exception.ValidationException;
import hu.agfcodeworks.operangel.application.validation.error.ErrorDto;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class ValidationUtil {

    public <SPECIFIC_ERROR_DTO extends ErrorDto<?>> List<SPECIFIC_ERROR_DTO> getGenericErrorDto(ValidationException ex) {
        return (List<SPECIFIC_ERROR_DTO>) ex.getErrorDtos();
    }
}
