package hu.agfcodeworks.operangel.application.event;

import hu.agfcodeworks.operangel.application.event.values.ContextStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder(setterPrefix = "with")
public class ContextEvent {

    private final ContextStatus status;
}
