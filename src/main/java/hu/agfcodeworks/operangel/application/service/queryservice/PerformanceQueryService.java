package hu.agfcodeworks.operangel.application.service.queryservice;

import hu.agfcodeworks.operangel.application.dto.PerformanceSimpleDto;
import hu.agfcodeworks.operangel.application.dto.PerformanceSummaryDto;
import hu.agfcodeworks.operangel.application.dto.PlayListDto;
import hu.agfcodeworks.operangel.application.mapper.PerformanceSummaryDtoMapper;
import hu.agfcodeworks.operangel.application.model.Performance;
import hu.agfcodeworks.operangel.application.repository.PerformanceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    public List<Performance> findBySimpleDtos(List<PerformanceSimpleDto> performanceSimpleDtos) {
        var naturalIds = performanceSimpleDtos.stream()
                .map(PerformanceSimpleDto::getNaturalId)
                .toList();

        return performanceRepository.findByNaturalIds(naturalIds);
    }

    public Optional<Performance> findByNaturalId(UUID naturalId) {
        return performanceRepository.findByNaturalId(naturalId);
    }
}
