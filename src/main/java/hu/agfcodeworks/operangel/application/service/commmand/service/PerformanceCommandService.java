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
import hu.agfcodeworks.operangel.application.service.query.service.ArtistQueryService;
import hu.agfcodeworks.operangel.application.service.query.service.LocationQueryService;
import hu.agfcodeworks.operangel.application.service.query.service.RoleQueryService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional
@Service
@AllArgsConstructor
public class PerformanceCommandService {

    private static final String ARTIST_NOT_FOUND_ERROR_MESSAGE_PATTERN = "Artist '%s' not found";
    public static final String LOCATION_S_NOT_FOUND_ERROR_MESSAGE_PATTERN = "Location '%s' not found";

    private final LocationQueryService locationQueryService;

    private final ArtistQueryService artistQueryService;

    private final RoleQueryService roleQueryService;

    private final PerformanceRepository performanceRepository;

    public void save(@NonNull PerformanceStateDto performanceStateDto) {
        var performanceOpt = performanceRepository.findByNaturalId(performanceStateDto.getNaturalId());

        if (performanceOpt.isPresent()) {
            update(performanceOpt.get(), performanceStateDto);
        } else {
            create(performanceStateDto);
        }
    }

    private void create(PerformanceStateDto performanceStateDto) {
        //TODO fill play
    }

    private void update(Performance performance, PerformanceStateDto performanceStateDto) {
        var artists = artistQueryService.findBySimpleDtos(
                new ArrayList<>(collectArtistDtos(performanceStateDto))
        );

        var roles = roleQueryService.findBySimpleDtos(
                new ArrayList<>(collectRoleDtos(performanceStateDto))
        );

        performance.setLocation(findLocation(performanceStateDto));
        performance.setPerformanceConductorJoins(makePerformanceConductorJoins(performance, artists, performanceStateDto));
        performance.setArtistPerformanceRoleJoins(makeArtistPerformanceRoleJoins(performance, artists, roles, performanceStateDto));
    }

    //TODO try to restructure with ThreadLocale
    private Set<ArtistPerformanceRoleJoin> makeArtistPerformanceRoleJoins(Performance performance, List<Artist> artists, List<Role> roles, PerformanceStateDto performanceStateDto) {
        return performanceStateDto.getArtistRoleSimpleDtos()
                .stream()
                .map(s -> makeArtistPerformanceRoleJoin(performance, artists, roles, s))
                .collect(Collectors.toSet());
    }

    private ArtistPerformanceRoleJoin makeArtistPerformanceRoleJoin(Performance performance, List<Artist> artists, List<Role> roles, ArtistRoleSimpleDto s) {
        return ArtistPerformanceRoleJoin.builder()
                .withId(makeArtistPerformanceRoleId(performance, artists, roles, s))
                .build();
    }

    private ArtistPerformanceRoleId makeArtistPerformanceRoleId(Performance performance, List<Artist> artists, List<Role> roles, ArtistRoleSimpleDto s) {
        return ArtistPerformanceRoleId.builder()
                .withPerformance(performance)
                .withArtist(obtainArtist(artists, s.getArtistSimpleDto()))
                .withRole(obtainRole(roles, s.getRoleSimpleDto()))
                .build();
    }

    private Location findLocation(PerformanceStateDto performanceStateDto) {
        var location = performanceStateDto.getLocation();

        return locationQueryService.findByNaturalId(location)
                .orElseThrow(() -> new RuntimeException(LOCATION_S_NOT_FOUND_ERROR_MESSAGE_PATTERN.formatted(location.getNaturalId())));
    }

    private List<PerformanceConductorJoin> makePerformanceConductorJoins(Performance performance, List<Artist> artists, PerformanceStateDto performanceStateDto) {
        return performanceStateDto.getConductors()
                .stream()
                .map(c -> makePerformanceConductorJoin(performance, artists, c))
                .toList();
    }

    private PerformanceConductorJoin makePerformanceConductorJoin(Performance performance, List<Artist> artists, ArtistSimpleDto c) {
        return PerformanceConductorJoin.builder()
                .withId(makePerformanceConductorId(performance, artists, c))
                .build();
    }

    private PerformanceConductorId makePerformanceConductorId(Performance performance, List<Artist> artists, ArtistSimpleDto c) {
        return PerformanceConductorId.builder()
                .withPerformance(performance)
                .withConductor(obtainArtist(artists, c))
                .build();
    }

    private Artist obtainArtist(List<Artist> artists, ArtistSimpleDto artistSimpleDto) {
        return artists.stream()
                .filter(a -> Objects.equals(artistSimpleDto.getNaturalId(), a.getNaturalId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(ARTIST_NOT_FOUND_ERROR_MESSAGE_PATTERN.formatted(artistSimpleDto.getNaturalId())));
    }

    private Role obtainRole(List<Role> roles, RoleSimpleDto roleSimpleDto) {
        return roles.stream()
                .filter(r -> Objects.equals(roleSimpleDto.getNaturalId(), r.getNaturalId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(ARTIST_NOT_FOUND_ERROR_MESSAGE_PATTERN.formatted(roleSimpleDto.getNaturalId())));
    }

    private Set<RoleSimpleDto> collectRoleDtos(PerformanceStateDto performanceStateDto) {
        return performanceStateDto.getArtistRoleSimpleDtos()
                .stream()
                .map(ArtistRoleSimpleDto::getRoleSimpleDto)
                .collect(Collectors.toSet());
    }


    private Set<ArtistSimpleDto> collectArtistDtos(PerformanceStateDto performanceStateDto) {
        var artistNaturalIds = new HashSet<ArtistSimpleDto>();

        artistNaturalIds.addAll(
                collectConductorDtos(performanceStateDto)
        );
        artistNaturalIds.addAll(
                collectSingerDtos(performanceStateDto)
        );

        return artistNaturalIds;
    }

    private Set<ArtistSimpleDto> collectConductorDtos(PerformanceStateDto performanceStateDto) {
        return new HashSet<>(performanceStateDto.getConductors());
    }

    private Set<ArtistSimpleDto> collectSingerDtos(PerformanceStateDto performanceStateDto) {
        return performanceStateDto.getArtistRoleSimpleDtos()
                .stream()
                .map(ArtistRoleSimpleDto::getArtistSimpleDto)
                .collect(Collectors.toSet());
    }

    public void delete(PerformanceSimpleDto performanceSimpleDto) {
        performanceRepository.deleteByNaturalId(performanceSimpleDto.getNaturalId());
    }
}
