package hu.agfcodeworks.operangel.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Builder(setterPrefix = "with")
@EqualsAndHashCode
@AllArgsConstructor
@Getter
@Setter
public class PerformanceSummaryDto {

    private final Integer maxConductorCount;

    private final Map<UUID, Integer> maxRoleCounts;

    private final List<PerformanceDto> performances;

}
