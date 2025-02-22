package hu.agfcodeworks.operangel.application.service.queryservice;

import hu.agfcodeworks.operangel.application.dto.PerformanceSummaryDto;
import hu.agfcodeworks.operangel.application.dto.PlayDto;
import hu.agfcodeworks.operangel.application.mapper.PerformanceSummaryDtoMapper;
import hu.agfcodeworks.operangel.application.repository.PerformanceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Optional;

@Service
@AllArgsConstructor
public class PerformanceQueryService {

    private final PerformanceRepository performanceRepository;

    private final PerformanceSummaryDtoMapper performanceSummaryDtoMapper;

    public Optional<PerformanceSummaryDto> getPerformancesForPlay(PlayDto playDto) {
        var performances = performanceRepository.findByPlayNaturalId(playDto.getNaturalId());

        if (CollectionUtils.isEmpty(performances)) {
            return Optional.empty();
        }

        return Optional.of(performanceSummaryDtoMapper.entityToDto(performances));
    }
}
