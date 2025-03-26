package hu.agfcodeworks.operangel.application.service.commmand.service;

import hu.agfcodeworks.operangel.application.dto.PerformanceSimpleDto;
import hu.agfcodeworks.operangel.application.dto.RoleDto;
import hu.agfcodeworks.operangel.application.dto.command.PlayPerformanceChangeCommand;
import hu.agfcodeworks.operangel.application.dto.command.RoleCommand;
import hu.agfcodeworks.operangel.application.dto.command.RoleDeleteCommand;
import hu.agfcodeworks.operangel.application.dto.state.PerformanceStateDto;
import hu.agfcodeworks.operangel.application.service.cache.ThreadCachePreparer;
import hu.agfcodeworks.operangel.application.service.query.service.PlayQueryService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Transactional
@Service
@AllArgsConstructor
public class PlayPerformanceCommandService {

    private static final String ROLE_NOT_FOUND_ERROR_MESSAGE_PATTERN = "Role '%s' not found.";

    private final ThreadCachePreparer threadCachePreparer;

    private final RoleCommandService roleCommandService;

    private final PerformanceCommandService performanceCommandService;

    public void save(@NonNull PlayPerformanceChangeCommand playPerformanceChangeCommand) {
        prepareThreadCaches(playPerformanceChangeCommand);

        manageRoles(playPerformanceChangeCommand);
        managePerformances(playPerformanceChangeCommand);
    }

    private void prepareThreadCaches(PlayPerformanceChangeCommand playPerformanceChangeCommand) {
        threadCachePreparer.prepare(playPerformanceChangeCommand);
    }

    private void manageRoles(PlayPerformanceChangeCommand playPerformanceChangeCommand) {
        deleteRoles(playPerformanceChangeCommand);
        insertRoles(playPerformanceChangeCommand);
        updateRoles(playPerformanceChangeCommand);
    }

    private void managePerformances(PlayPerformanceChangeCommand playPerformanceChangeCommand) {
        deletePerformances(playPerformanceChangeCommand);
        insertOrUpdatePerformances(playPerformanceChangeCommand);
    }

    private void deletePerformances(PlayPerformanceChangeCommand playPerformanceChangeCommand) {
        makeStreamOfPerformancesToDelete(playPerformanceChangeCommand)
                .map(this::makePerformanceSimpleDto)
                .forEach(performanceCommandService::delete);
    }

    private void insertOrUpdatePerformances(PlayPerformanceChangeCommand playPerformanceChangeCommand) {
        var playNaturalId = playPerformanceChangeCommand.getPlayNaturalId();
        playPerformanceChangeCommand.getNewPlayState()
                .getPerformanceStateDtos()
                .forEach(performanceCommandService::save);

    }

    private PerformanceSimpleDto makePerformanceSimpleDto(PerformanceStateDto performanceStateDto) {
        return PerformanceSimpleDto.builder()
                .withNaturalId(performanceStateDto.getNaturalId())
                .build();
    }

    private Stream<PerformanceStateDto> makeStreamOfPerformancesToDelete(PlayPerformanceChangeCommand playPerformanceChangeCommand) {
        var newPerformanceDtos = playPerformanceChangeCommand.getNewPlayState()
                .getPerformanceStateDtos();

        return playPerformanceChangeCommand.getOriginalPlayState()
                .getPerformanceStateDtos()
                .stream()
                .filter(performanceStateDto -> !newPerformanceDtos.contains(performanceStateDto));
    }

    private void deleteRoles(PlayPerformanceChangeCommand playPerformanceChangeCommand) {
        makeStreamOfRolesToDelete(playPerformanceChangeCommand)
                .map(this::makeRoleDeleteCommand)
                .forEach(roleCommandService::delete);
    }

    private void insertRoles(PlayPerformanceChangeCommand playPerformanceChangeCommand) {
        makeStreamOfRolesToInsert(playPerformanceChangeCommand)
                .map(this::makeRoleCommand)
                .forEach(roleCommandService::save);
    }

    private void updateRoles(PlayPerformanceChangeCommand playPerformanceChangeCommand) {
        makeStreamOfRolesToUpdate(playPerformanceChangeCommand)
                .map(this::makeRoleCommand)
                .forEach(roleCommandService::save);
    }

    private Stream<RoleDto> makeStreamOfRolesToDelete(PlayPerformanceChangeCommand playPerformanceChangeCommand) {
        var roles = playPerformanceChangeCommand.getNewPlayState()
                .getRoles();

        return playPerformanceChangeCommand.getOriginalPlayState()
                .getRoles()
                .stream()
                .filter(role -> !roles.contains(role));
    }

    private Stream<RoleDto> makeStreamOfRolesToInsert(PlayPerformanceChangeCommand playPerformanceChangeCommand) {
        return makeStreamOfRolesInNewPlayState(playPerformanceChangeCommand)
                .filter(role -> !playPerformanceChangeCommand.getOriginalPlayState().getRoles().contains(role));
    }

    private Stream<RoleDto> makeStreamOfRolesToUpdate(PlayPerformanceChangeCommand playPerformanceChangeCommand) {
        return makeStreamOfRolesInNewPlayState(playPerformanceChangeCommand)
                .filter(role -> playPerformanceChangeCommand.getOriginalPlayState().getRoles().contains(role) && !descriptionEquals(playPerformanceChangeCommand, role));
    }

    private boolean descriptionEquals(PlayPerformanceChangeCommand playPerformanceChangeCommand, RoleDto role) {
        return Objects.equals(role.getDescription(), findInOriginalPlayState(playPerformanceChangeCommand, role).getDescription());
    }

    private RoleDto findInOriginalPlayState(PlayPerformanceChangeCommand playPerformanceChangeCommand, RoleDto roleDto) {
        return playPerformanceChangeCommand.getOriginalPlayState().getRoles()
                .stream()
                .filter(originalRole -> Objects.equals(originalRole.getNaturalId(), roleDto.getNaturalId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(ROLE_NOT_FOUND_ERROR_MESSAGE_PATTERN.formatted(roleDto.getNaturalId())));
    }

    private static Stream<RoleDto> makeStreamOfRolesInNewPlayState(PlayPerformanceChangeCommand playPerformanceChangeCommand) {
        return playPerformanceChangeCommand.getNewPlayState()
                .getRoles()
                .stream();
    }

    private RoleDeleteCommand makeRoleDeleteCommand(RoleDto roleDto) {
        return RoleDeleteCommand.builder()
                .withNaturalId(roleDto.getNaturalId())
                .build();
    }

    private RoleCommand makeRoleCommand(RoleDto roleDto) {
        return RoleCommand.builder()
                .withPlayNaturalId(roleDto.getPlayNaturalId())
                .withNaturalId(roleDto.getNaturalId())
                .withDescription(roleDto.getDescription())
                .build();
    }
}
