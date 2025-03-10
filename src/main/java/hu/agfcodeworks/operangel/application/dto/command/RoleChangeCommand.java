package hu.agfcodeworks.operangel.application.dto.command;

import hu.agfcodeworks.operangel.application.dto.ArtistPerformanceSimpleDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@Builder(setterPrefix = "with")
public class RoleChangeCommand {

    private final UUID originalRoleNaturalId;

    private final UUID newRoleNaturalId;

    private final List<ArtistPerformanceSimpleDto> artistPerformanceSimpleDtos;
}
