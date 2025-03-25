package hu.agfcodeworks.operangel.application.mapper;

import hu.agfcodeworks.operangel.application.dto.PerformanceDto;
import hu.agfcodeworks.operangel.application.dto.PerformanceSummaryDto;
import hu.agfcodeworks.operangel.application.dto.ArtistPerformanceDto;
import hu.agfcodeworks.operangel.application.dto.RoleSimpleDto;
import hu.agfcodeworks.operangel.application.model.Performance;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class PerformanceSummaryDtoMapper extends AbstractMapper<List<Performance>, PerformanceSummaryDto> {

    private final PerformanceDtoMapper performanceDtoMapper;

    @Override
    public PerformanceSummaryDto entityToDto(@NonNull List<Performance> performances) {
        var performanceDtos = performances.stream()
                .map(performanceDtoMapper::entityToDto)
                .toList();

        return PerformanceSummaryDto.builder()
                .withPerformances(performanceDtos)
                .withMaxConductorCount(countMaxConductors(performanceDtos))
                .withMaxRoleCounts(countMaxRolesNumber(performanceDtos))
                .build();
    }

    private Integer countMaxConductors(List<PerformanceDto> performanceDtos) {
        return performanceDtos.stream()
                .mapToInt(prf -> prf.getConductors().size())
                .max()
                .orElse(0);
    }

    private Map<UUID, Integer> countMaxRolesNumber(List<PerformanceDto> performances) {
        var roles = performances.stream()
                .flatMap(performance ->
                        performance.getRoleArtists().stream()
                                .map(ArtistPerformanceDto::getRoleSimpleDto)
                                .distinct()
                ).collect(Collectors.toSet());

        var maxCountMap = new HashMap<UUID, Integer>();
        roles.forEach(r -> maxCountMap.put(r.getNaturalId(), getMaxCountInPerformances(performances, r)));

        return maxCountMap;
    }

    private Integer getMaxCountInPerformances(List<PerformanceDto> performances, RoleSimpleDto roleSimpleDto) {
        return performances.stream()
                .map(prf -> prf.getRoleArtists().stream().filter(ra -> Objects.equals(ra.getRoleSimpleDto(), roleSimpleDto)).count())
                .max(Long::compareTo)
                .map(Long::intValue)
                .orElse(0);
    }
}
