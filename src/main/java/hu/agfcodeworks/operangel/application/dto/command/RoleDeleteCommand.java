package hu.agfcodeworks.operangel.application.dto.command;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder(setterPrefix = "with")
public class RoleDeleteCommand {

    private final UUID naturalId;

}
