package hu.agfcodeworks.operangel.application.validation.error;

import hu.agfcodeworks.operangel.application.ui.text.TextProvider;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@AllArgsConstructor
public abstract class ErrorDto<M> {

    @Getter
    protected final @NonNull FieldMarker<M> fieldMarker;

    @Getter
    private final @NonNull String errorDescription;

    private final @NonNull TextProvider<M> fieldMarkerTextProvider;

    public String getFieldMarkerText() {
        return fieldMarkerTextProvider.provide(fieldMarker.getContent());
    }
}
