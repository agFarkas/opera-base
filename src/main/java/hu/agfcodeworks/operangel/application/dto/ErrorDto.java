package hu.agfcodeworks.operangel.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
public class ErrorDto {

    private final @NonNull String fieldName;

    private final @NonNull String errorDescription;
}
