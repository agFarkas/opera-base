package hu.agfcodeworks.operangel.application.service.queryservice;

import hu.agfcodeworks.operangel.application.dto.PerformanceSummaryDto;
import hu.agfcodeworks.operangel.application.dto.PlayListDto;
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

    public Optional<PerformanceSummaryDto> getPerformancesForPlay(PlayListDto playListDto) {
        var performances = performanceRepository.findByPlayNaturalId(playListDto.getNaturalId());

        if (CollectionUtils.isEmpty(performances)) {
            return Optional.empty();
        }

        return Optional.of(performanceSummaryDtoMapper.entityToDto(performances));
    }
}
