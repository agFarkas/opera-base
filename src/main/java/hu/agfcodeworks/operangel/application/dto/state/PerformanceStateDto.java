package hu.agfcodeworks.operangel.application.dto.state;


import hu.agfcodeworks.operangel.application.dto.ArtistRoleSimpleDto;
import hu.agfcodeworks.operangel.application.dto.ArtistSimpleDto;
import hu.agfcodeworks.operangel.application.dto.LocationSimpleDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Builder(setterPrefix = "with")
@Getter
@AllArgsConstructor
public class PerformanceStateDto {

    private final UUID naturalId;

    private final LocalDate date;

    private final LocationSimpleDto location;

    private final List<ArtistSimpleDto> conductors;

    @Setter
    private List<ArtistRoleSimpleDto> artistRoleSimpleDtos;
}
