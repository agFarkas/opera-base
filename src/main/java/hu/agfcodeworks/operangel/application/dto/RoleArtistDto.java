package hu.agfcodeworks.operangel.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

@Builder(setterPrefix = "with")
@EqualsAndHashCode
@AllArgsConstructor
@Getter
public class RoleArtistDto {

    private final UUID performanceId;

    private final RoleJoinDto roleJoinDto;

    private final ArtistListDto artistListDto;
}
