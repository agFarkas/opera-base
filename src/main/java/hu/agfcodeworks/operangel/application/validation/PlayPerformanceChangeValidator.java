package hu.agfcodeworks.operangel.application.validation;

import hu.agfcodeworks.operangel.application.dto.ArtistRoleSimpleDto;
import hu.agfcodeworks.operangel.application.dto.ArtistSimpleDto;
import hu.agfcodeworks.operangel.application.dto.LocationDto;
import hu.agfcodeworks.operangel.application.dto.LocationSimpleDto;
import hu.agfcodeworks.operangel.application.dto.RoleSimpleDto;
import hu.agfcodeworks.operangel.application.dto.command.PlayPerformanceChangeCommand;
import hu.agfcodeworks.operangel.application.dto.state.PerformanceStateDto;
import hu.agfcodeworks.operangel.application.dto.state.PlayStateDto;
import hu.agfcodeworks.operangel.application.exception.PlayPerformanceValidationException;
import hu.agfcodeworks.operangel.application.service.cache.global.LocationCache;
import hu.agfcodeworks.operangel.application.validation.dto.PerformanceValidationMarkerContent;
import hu.agfcodeworks.operangel.application.validation.error.PlayPerformanceValidationErrorDto;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Component
@AllArgsConstructor
public class PlayPerformanceChangeValidator {

    private static final String MANDATORY_DATAS_MISSING_ERROR_MESSAGE = "Kötelező adatok hiányoznak.";

    private static final String SAME_CONDUCTOR_MULTIPLE_TIMES_ERROR_MESSAGE = "Ugyanaz a karmester többször.";

    private static final String SAME_SINGER_ROLE_ASSOCIATION_MULTIPLE_TIMES_ERROR_MESSAGE = "Ugyanaz az énekes ugyanabban a szerepben többször.";

    private final LocationCache locationCache;

    public void validate(PlayPerformanceChangeCommand playPerformanceChangeCommand) {
        validatePlayState(playPerformanceChangeCommand.getNewPlayState());
    }

    private void validatePlayState(PlayStateDto playState) {
        var errorDtos = new LinkedList<PlayPerformanceValidationErrorDto>();

        errorDtos.addAll(validatePerformanceStateDtos(playState.getPerformanceStateDtos()));

        if (!CollectionUtils.isEmpty(errorDtos)) {
            throw new PlayPerformanceValidationException(errorDtos);
        }
    }

    private LinkedList<PlayPerformanceValidationErrorDto> validatePerformanceStateDtos(List<PerformanceStateDto> performanceStateDtos) {
        var errorDtos = new LinkedList<PlayPerformanceValidationErrorDto>();

        performanceStateDtos.stream()
                .map(this::validate)
                .forEach(errorDtos::addAll);

        return errorDtos;
    }

    private List<PlayPerformanceValidationErrorDto> validate(PerformanceStateDto performanceStateDto) {
        var errorDtos = new LinkedList<PlayPerformanceValidationErrorDto>();

        validateMandatory(performanceStateDto)
                .ifPresent(errorDtos::add);
        validateUniqueConductors(performanceStateDto)
                .ifPresent(errorDtos::add);
        validateUniqueSingerRoleAssociations(performanceStateDto)
                .ifPresent(errorDtos::add);

        return errorDtos;
    }

    private Optional<PlayPerformanceValidationErrorDto> validateMandatory(PerformanceStateDto performanceStateDto) {
        if (Objects.isNull(performanceStateDto.getDate()) || Objects.isNull(performanceStateDto.getLocation())) {
            return Optional.of(makeErrorDto(performanceStateDto, MANDATORY_DATAS_MISSING_ERROR_MESSAGE));
        }

        return Optional.empty();
    }

    private Optional<PlayPerformanceValidationErrorDto> validateUniqueConductors(PerformanceStateDto performanceStateDto) {
        var seenArtists = new HashSet<ArtistSimpleDto>();

        for (var artistSimpleDto : performanceStateDto.getConductors()) {
            if (seenArtists.contains(artistSimpleDto)) {
                return Optional.of(makeErrorDto(performanceStateDto, SAME_CONDUCTOR_MULTIPLE_TIMES_ERROR_MESSAGE));
            }

            seenArtists.add(artistSimpleDto);
        }

        return Optional.empty();
    }

    private Optional<PlayPerformanceValidationErrorDto> validateUniqueSingerRoleAssociations(PerformanceStateDto performanceStateDto) {
        var seenArtistRoles = new HashMap<RoleSimpleDto, Set<ArtistSimpleDto>>();

        for (var artistRoleSimpleDto : performanceStateDto.getArtistRoleSimpleDtos()) {
            var seenArtists = getSeenArtistsBy(artistRoleSimpleDto, seenArtistRoles);
            var artistSimpleDto = artistRoleSimpleDto.getArtistSimpleDto();

            if (seenArtists.contains(artistSimpleDto)) {
                return Optional.of(makeErrorDto(performanceStateDto, SAME_SINGER_ROLE_ASSOCIATION_MULTIPLE_TIMES_ERROR_MESSAGE));
            }

            seenArtists.add(artistSimpleDto);
        }

        return Optional.empty();
    }

    private static Set<ArtistSimpleDto> getSeenArtistsBy(ArtistRoleSimpleDto artistRoleSimpleDto, HashMap<RoleSimpleDto, Set<ArtistSimpleDto>> seenArtistRoles) {
        var roleSimpleDto = artistRoleSimpleDto.getRoleSimpleDto();
        var artistSimpleDtos = seenArtistRoles.get(roleSimpleDto);

        if (Objects.isNull(artistSimpleDtos)) {
            var newSet = new HashSet<ArtistSimpleDto>();
            seenArtistRoles.put(roleSimpleDto, newSet);

            return newSet;
        }

        return artistSimpleDtos;
    }


    private PlayPerformanceValidationErrorDto makeErrorDto(PerformanceStateDto performanceStateDto, String message) {
        return new PlayPerformanceValidationErrorDto(makeFieldMarker(performanceStateDto), message);
    }

    private PerformanceValidationMarkerContent makeFieldMarker(PerformanceStateDto performanceStateDto) {
        return PerformanceValidationMarkerContent.builder()
                .withNaturalId(performanceStateDto.getNaturalId())
                .withDate(performanceStateDto.getDate())
                .withLocation(obtainLocationDto(performanceStateDto.getLocation()))
                .build();
    }

    private LocationDto obtainLocationDto(LocationSimpleDto locationSimpleDto) {
        if(Objects.isNull(locationSimpleDto)) {
            return null;
        }

        return locationCache.get(locationSimpleDto.getNaturalId());
    }

}
