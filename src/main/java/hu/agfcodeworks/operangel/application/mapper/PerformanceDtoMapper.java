package hu.agfcodeworks.operangel.application.mapper;

import hu.agfcodeworks.operangel.application.dto.ArtistListDto;
import hu.agfcodeworks.operangel.application.dto.PerformanceDto;
import hu.agfcodeworks.operangel.application.dto.ArtistPerformanceDto;
import hu.agfcodeworks.operangel.application.model.ArtistPerformanceRoleJoin;
import hu.agfcodeworks.operangel.application.model.Performance;
import hu.agfcodeworks.operangel.application.model.PerformanceConductorJoin;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class PerformanceDtoMapper extends AbstractDtoMapper<Performance, PerformanceDto> {

    private final LocationDtoMapper locationDtoMapper;

    private final ArtistListDtoMapper artistListDtoMapper;

    private final RoleArtistDtoMapper roleArtistDtoMapper;

    @Override
    public PerformanceDto entityToDto(@NonNull Performance performance) {
        return PerformanceDto.builder()
                .withNaturalId(performance.getNaturalId())
                .withDate(performance.getDate().toLocalDateTime().toLocalDate())
                .withLocation(locationDtoMapper.entityToDto(performance.getLocation()))
                .withConductors(mapConductors(performance.getPerformanceConductorJoins()))
                .withRoleArtists(mapRoleArtists(performance.getArtistPerformanceRoleJoins()))
                .build();
    }

    private List<ArtistListDto> mapConductors(List<PerformanceConductorJoin> conductors) {
        return conductors.stream()
                .distinct()
                .map(prfCnd -> prfCnd.getId().getConductor())
                .map(artistListDtoMapper::entityToDto)
                .collect(Collectors.toList());
    }

    private Set<ArtistPerformanceDto> mapRoleArtists(Set<ArtistPerformanceRoleJoin> artistPerformanceRoleJoins) {
        return artistPerformanceRoleJoins.stream()
                .map(roleArtistDtoMapper::entityToDto)
                .collect(Collectors.toSet());
    }
}
