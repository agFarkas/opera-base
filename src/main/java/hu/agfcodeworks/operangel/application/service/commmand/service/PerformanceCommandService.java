package hu.agfcodeworks.operangel.application.service.commmand.service;

import hu.agfcodeworks.operangel.application.dto.ArtistRoleSimpleDto;
import hu.agfcodeworks.operangel.application.dto.ArtistSimpleDto;
import hu.agfcodeworks.operangel.application.dto.PerformanceSimpleDto;
import hu.agfcodeworks.operangel.application.dto.RoleSimpleDto;
import hu.agfcodeworks.operangel.application.dto.state.PerformanceStateDto;
import hu.agfcodeworks.operangel.application.model.Artist;
import hu.agfcodeworks.operangel.application.model.ArtistPerformanceRoleJoin;
import hu.agfcodeworks.operangel.application.model.Location;
import hu.agfcodeworks.operangel.application.model.Performance;
import hu.agfcodeworks.operangel.application.model.PerformanceConductorJoin;
import hu.agfcodeworks.operangel.application.model.Role;
import hu.agfcodeworks.operangel.application.model.embeddable.ArtistPerformanceRoleId;
import hu.agfcodeworks.operangel.application.model.embeddable.PerformanceConductorId;
import hu.agfcodeworks.operangel.application.repository.PerformanceRepository;
import hu.agfcodeworks.operangel.application.service.query.service.PerformanceQueryService;
import hu.agfcodeworks.operangel.application.util.ThreadCacheUtil;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional
@Service
@AllArgsConstructor
public class PerformanceCommandService {

    private final PerformanceQueryService performanceQueryService;

    private final PerformanceRepository performanceRepository;

    public void save(@NonNull PerformanceStateDto performanceStateDto) {
        var performanceOpt = performanceQueryService.findByNaturalId(performanceStateDto.getNaturalId());

        if (performanceOpt.isPresent()) {
            update(performanceOpt.get(), performanceStateDto);
        } else {
            create(performanceStateDto);
        }
    }

    private void create(PerformanceStateDto performanceStateDto) {
        var performance = Performance.builder()
                .withPlay(ThreadCacheUtil.getPlay())
                .withNaturalId(performanceStateDto.getNaturalId())
                .build();
        fill(performance, performanceStateDto);
        performanceRepository.save(performance);
    }

    private void update(Performance performance, PerformanceStateDto performanceStateDto) {
        fill(performance, performanceStateDto);
        performanceRepository.save(performance);
    }

    private void fill(Performance performance, PerformanceStateDto performanceStateDto) {
        performance.setDate(convertDate(performanceStateDto));
        performance.setLocation(obtainLocation(performanceStateDto));
        performance.setPerformanceConductorJoins(makePerformanceConductorJoins(performance, performanceStateDto));
        performance.setArtistPerformanceRoleJoins(makeArtistPerformanceRoleJoins(performance, performanceStateDto));
    }

    private Timestamp convertDate(PerformanceStateDto performanceStateDto) {
        return Timestamp.valueOf(performanceStateDto.getDate().atStartOfDay());
    }

    private Set<ArtistPerformanceRoleJoin> makeArtistPerformanceRoleJoins(Performance performance, PerformanceStateDto performanceStateDto) {
        return performanceStateDto.getArtistRoleSimpleDtos()
                .stream()
                .map(artistRoleSimpleDto -> makeArtistPerformanceRoleJoin(performance, artistRoleSimpleDto))
                .collect(Collectors.toSet());
    }

    private ArtistPerformanceRoleJoin makeArtistPerformanceRoleJoin(Performance performance, ArtistRoleSimpleDto artistRoleSimpleDto) {
        return ArtistPerformanceRoleJoin.builder()
                .withId(makeArtistPerformanceRoleId(performance, artistRoleSimpleDto))
                .build();
    }

    private ArtistPerformanceRoleId makeArtistPerformanceRoleId(Performance performance, ArtistRoleSimpleDto artistRoleSimpleDto) {
        return ArtistPerformanceRoleId.builder()
                .withPerformance(performance)
                .withArtist(obtainArtist(artistRoleSimpleDto.getArtistSimpleDto()))
                .withRole(obtainRole(artistRoleSimpleDto.getRoleSimpleDto()))
                .build();
    }

    private Location obtainLocation(PerformanceStateDto performanceStateDto) {
        return ThreadCacheUtil.getLocationBy(performanceStateDto.getLocation().getNaturalId());
    }

    private List<PerformanceConductorJoin> makePerformanceConductorJoins(Performance performance, PerformanceStateDto performanceStateDto) {
        return new ArrayList<>(
                performanceStateDto.getConductors()
                        .stream()
                        .map(c -> makePerformanceConductorJoin(performance, c))
                        .toList()
        );
    }

    private PerformanceConductorJoin makePerformanceConductorJoin(Performance performance, ArtistSimpleDto artistSimpleDto) {
        return PerformanceConductorJoin.builder()
                .withId(makePerformanceConductorId(performance, artistSimpleDto))
                .build();
    }

    private PerformanceConductorId makePerformanceConductorId(Performance performance, ArtistSimpleDto artistSimpleDto) {
        return PerformanceConductorId.builder()
                .withPerformance(performance)
                .withConductor(obtainArtist(artistSimpleDto))
                .build();
    }

    private Artist obtainArtist(ArtistSimpleDto artistSimpleDto) {
        return ThreadCacheUtil.getArtistBy(artistSimpleDto.getNaturalId());
    }

    private Role obtainRole(RoleSimpleDto roleSimpleDto) {
        return ThreadCacheUtil.getRoleBy(roleSimpleDto.getNaturalId());
    }

    public void delete(PerformanceSimpleDto performanceSimpleDto) {
        performanceRepository.deleteByNaturalId(performanceSimpleDto.getNaturalId());
    }
}
