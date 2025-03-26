package hu.agfcodeworks.operangel.application.service.cache;

import hu.agfcodeworks.operangel.application.dto.ArtistRoleSimpleDto;
import hu.agfcodeworks.operangel.application.dto.ArtistSimpleDto;
import hu.agfcodeworks.operangel.application.dto.LocationSimpleDto;
import hu.agfcodeworks.operangel.application.dto.RoleDto;
import hu.agfcodeworks.operangel.application.dto.command.PlayPerformanceChangeCommand;
import hu.agfcodeworks.operangel.application.dto.state.PerformanceStateDto;
import hu.agfcodeworks.operangel.application.dto.state.PlayStateDto;
import hu.agfcodeworks.operangel.application.service.query.service.ArtistQueryService;
import hu.agfcodeworks.operangel.application.service.query.service.LocationQueryService;
import hu.agfcodeworks.operangel.application.service.query.service.PlayQueryService;
import hu.agfcodeworks.operangel.application.service.query.service.RoleQueryService;
import hu.agfcodeworks.operangel.application.util.ThreadCacheUtil;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
@Component
public class ThreadCachePreparer {

    private final PlayQueryService playQueryService;

    private final LocationQueryService locationQueryService;

    private final ArtistQueryService artistQueryService;

    private final RoleQueryService roleQueryService;

    public void prepare(@NonNull PlayPerformanceChangeCommand playPerformanceChangeCommand) {
        preparePlay(playPerformanceChangeCommand);
        prepareLocations(playPerformanceChangeCommand);
        prepareArtists(playPerformanceChangeCommand);
        prepareRoles(playPerformanceChangeCommand);
    }

    private void preparePlay(@NonNull PlayPerformanceChangeCommand playPerformanceChangeCommand) {
        playQueryService.getByNaturalId(
                playPerformanceChangeCommand.getPlayNaturalId()
        ).ifPresent(ThreadCacheUtil::storePlay);
    }

    private void prepareLocations(PlayPerformanceChangeCommand playPerformanceChangeCommand) {
        ThreadCacheUtil.storeLocations(locationQueryService.findByNaturalIds(
                collectPresentLocationNaturalIds(playPerformanceChangeCommand)
        ));
    }

    private void prepareArtists(PlayPerformanceChangeCommand playPerformanceChangeCommand) {
        ThreadCacheUtil.storeArtists(artistQueryService.findByNaturalIds(
                collectPresentArtistNaturalIds(playPerformanceChangeCommand)
        ));
    }

    private void prepareRoles(PlayPerformanceChangeCommand playPerformanceChangeCommand) {
        ThreadCacheUtil.storeRoles(roleQueryService.findBySimpleDtos(
                collectPresentRoleNaturalIds(playPerformanceChangeCommand)
        ));
    }

    private Set<UUID> collectPresentLocationNaturalIds(@NonNull PlayPerformanceChangeCommand playPerformanceChangeCommand) {
        var naturalIds = new HashSet<UUID>();

        naturalIds.addAll(collectLocationNaturalIds(playPerformanceChangeCommand.getOriginalPlayState()));
        naturalIds.addAll(collectLocationNaturalIds(playPerformanceChangeCommand.getNewPlayState()));

        return naturalIds;
    }

    private Set<UUID> collectPresentArtistNaturalIds(@NonNull PlayPerformanceChangeCommand playPerformanceChangeCommand) {
        var naturalIds = new HashSet<UUID>();

        naturalIds.addAll(collectArtistNaturalIds(playPerformanceChangeCommand.getOriginalPlayState(), this::makeStreamOfConductors));
        naturalIds.addAll(collectArtistNaturalIds(playPerformanceChangeCommand.getOriginalPlayState(), this::makeStreamOfSingers));

        naturalIds.addAll(collectArtistNaturalIds(playPerformanceChangeCommand.getNewPlayState(), this::makeStreamOfConductors));
        naturalIds.addAll(collectArtistNaturalIds(playPerformanceChangeCommand.getNewPlayState(), this::makeStreamOfSingers));

        return naturalIds;
    }

    private Set<UUID> collectPresentRoleNaturalIds(@NonNull PlayPerformanceChangeCommand playPerformanceChangeCommand) {
        var naturalIds = new HashSet<UUID>();

        naturalIds.addAll(collectRoleNaturalIds(playPerformanceChangeCommand.getOriginalPlayState()));
        naturalIds.addAll(collectRoleNaturalIds(playPerformanceChangeCommand.getNewPlayState()));

        return naturalIds;
    }

    private Set<UUID> collectLocationNaturalIds(PlayStateDto playState) {
        return playState.getPerformanceStateDtos()
                .stream()
                .map(PerformanceStateDto::getLocation)
                .map(LocationSimpleDto::getNaturalId)
                .collect(Collectors.toSet());
    }

    private Set<UUID> collectRoleNaturalIds(PlayStateDto playState) {
        return playState.getRoles()
                .stream()
                .map(RoleDto::getNaturalId)
                .collect(Collectors.toSet());
    }

    private Set<UUID> collectArtistNaturalIds(PlayStateDto playState, Function<PerformanceStateDto, Stream<ArtistSimpleDto>> makeStreamOfConductors) {
        return playState.getPerformanceStateDtos()
                .stream()
                .flatMap(makeStreamOfConductors)
                .map(ArtistSimpleDto::getNaturalId)
                .collect(Collectors.toSet());
    }

    private Stream<ArtistSimpleDto> makeStreamOfConductors(PerformanceStateDto performanceStateDto) {
        return performanceStateDto.getConductors()
                .stream();
    }

    private Stream<ArtistSimpleDto> makeStreamOfSingers(PerformanceStateDto performanceStateDto) {
        return performanceStateDto.getArtistRoleSimpleDtos()
                .stream()
                .map(ArtistRoleSimpleDto::getArtistSimpleDto);
    }

}
