package hu.agfcodeworks.operangel.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder(setterPrefix = "with")
@EqualsAndHashCode
@AllArgsConstructor
@Getter
public class ArtistRoleSimpleDto {

    private final RoleSimpleDto roleSimpleDto;

    private final ArtistSimpleDto artistSimpleDto;
}
