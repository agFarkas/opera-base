package hu.agfcodeworks.operangel.application.ui.components.custom;

import hu.agfcodeworks.operangel.application.ui.components.custom.uidto.ValidationStatus;

import java.util.Set;

public interface Validated {

    Set<ValidationStatus> getValidationStatus();
}
