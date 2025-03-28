package hu.agfcodeworks.operangel.application.validation.error;

import hu.agfcodeworks.operangel.application.ui.text.TextProvider;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class DialogValidationErrorDto extends ErrorDto<String> {

    private static final TextProvider<String> fieldMarkerTextProvider = t -> t;

    public DialogValidationErrorDto(@NonNull String fieldMarker, @NonNull String errorDescription) {
        super(fieldMarker, errorDescription, fieldMarkerTextProvider);
    }
}
