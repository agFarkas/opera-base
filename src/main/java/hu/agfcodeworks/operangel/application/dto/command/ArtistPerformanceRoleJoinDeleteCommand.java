package hu.agfcodeworks.operangel.application.dto.command;

import hu.agfcodeworks.operangel.application.dto.ArtistSimpleDto;
import hu.agfcodeworks.operangel.application.dto.PerformanceSimpleDto;
import hu.agfcodeworks.operangel.application.dto.RoleSimpleDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(setterPrefix = "with")
public class ArtistPerformanceRoleJoinDeleteCommand {

    private final ArtistSimpleDto originalArtist;

    private final PerformanceSimpleDto performance;

    private final RoleSimpleDto role;
}
