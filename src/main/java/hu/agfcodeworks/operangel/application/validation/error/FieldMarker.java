package hu.agfcodeworks.operangel.application.validation.error;


import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class FieldMarker<M> {

    private final @NonNull M content;
}
