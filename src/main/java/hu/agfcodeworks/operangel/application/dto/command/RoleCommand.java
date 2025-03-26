package hu.agfcodeworks.operangel.application.dto.command;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder(setterPrefix = "with")
public class RoleCommand {

    private final UUID playNaturalId;

    private final UUID naturalId;

    private final String description;
}
