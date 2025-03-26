package hu.agfcodeworks.operangel.application.dto.command;

import hu.agfcodeworks.operangel.application.dto.ArtistPerformanceSimpleDto;
import hu.agfcodeworks.operangel.application.dto.RoleSimpleDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder(setterPrefix = "with")
public class RoleChangeCommand {

    private final RoleSimpleDto originalRole;

    private final RoleSimpleDto newRole;

    private final List<ArtistPerformanceSimpleDto> artistPerformanceSimpleDtos;
}
