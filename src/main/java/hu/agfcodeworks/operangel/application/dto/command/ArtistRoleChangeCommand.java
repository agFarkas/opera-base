package hu.agfcodeworks.operangel.application.dto.command;

import hu.agfcodeworks.operangel.application.dto.ArtistSimpleDto;
import hu.agfcodeworks.operangel.application.dto.RolePerformanceSimpleDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(setterPrefix = "with")
public class ArtistRoleChangeCommand {

    private final ArtistSimpleDto originalArtist;

    private final ArtistSimpleDto newArtist;

    private final RolePerformanceSimpleDto rolePerformanceSimpleDto;
}
