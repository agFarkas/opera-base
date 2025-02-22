package hu.agfcodeworks.operangel.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Builder(setterPrefix = "with")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@Getter
@Setter
public class PerformanceDto {

    @EqualsAndHashCode.Include
    private final UUID naturalId;

    private final LocalDate date;

    private final LocationDto location;

    private List<ArtistListDto> conductors;

    private final Set<RoleArtistDto> roleArtists;


}
