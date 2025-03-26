package hu.agfcodeworks.operangel.application.service.commmand.service;

import hu.agfcodeworks.operangel.application.dto.ArtistPerformanceSimpleDto;
import hu.agfcodeworks.operangel.application.dto.ArtistSimpleDto;
import hu.agfcodeworks.operangel.application.dto.PerformanceSimpleDto;
import hu.agfcodeworks.operangel.application.dto.RoleSimpleDto;
import hu.agfcodeworks.operangel.application.dto.command.ArtistPerformanceRoleJoinDeleteCommand;
import hu.agfcodeworks.operangel.application.dto.command.ArtistRoleChangeCommand;
import hu.agfcodeworks.operangel.application.dto.command.RoleChangeCommand;
import hu.agfcodeworks.operangel.application.model.Artist;
import hu.agfcodeworks.operangel.application.model.ArtistPerformanceRoleJoin;
import hu.agfcodeworks.operangel.application.model.Performance;
import hu.agfcodeworks.operangel.application.model.Role;
import hu.agfcodeworks.operangel.application.model.embeddable.ArtistPerformanceRoleId;
import hu.agfcodeworks.operangel.application.repository.ArtistPerformanceRoleJoinRepository;
import hu.agfcodeworks.operangel.application.service.query.service.ArtistQueryService;
import hu.agfcodeworks.operangel.application.service.query.service.PerformanceQueryService;
import hu.agfcodeworks.operangel.application.service.query.service.RoleQueryService;
import hu.agfcodeworks.operangel.application.util.ThreadCacheUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@Transactional
@Service
@AllArgsConstructor
public class ArtistPerformanceRoleJoinCommandService {

    private static final String ROLE_NOT_FOUND_ERROR_MESSAGE_PATTERN = "Role '%s' not found.";

    private static final String ARTIST_NOT_FOUND_ERROR_MESSAGE_PATTERN = "Artist '%s' not found.";

    private static final String PERFORMANCE_NOT_FOUND_ERROR_MESSAGE_PATTERN = "Performance '%s' not found.";

    private final ArtistPerformanceRoleJoinRepository artistPerformanceRoleJoinRepository;

    private final PerformanceQueryService performanceQueryService;

    private final ArtistQueryService artistQueryService;

    private final RoleQueryService roleQueryService;

    public void updateFromRoleToRole(RoleChangeCommand roleChangeCommand) {
        var entityPrecollection = precollectEntities(roleChangeCommand);

        roleChangeCommand.getArtistPerformanceSimpleDtos()
                .forEach(apSimpleDto -> updateByRole(apSimpleDto, entityPrecollection));
    }

    public void saveArtistRoleJoin(ArtistRoleChangeCommand artistRoleChangeCommand) {
        var rolePerformanceSimpleDto = artistRoleChangeCommand.getRolePerformanceSimpleDto();

        var originalArtistSimpleDto = artistRoleChangeCommand.getOriginalArtist();
        var newArtistSimpleDto = artistRoleChangeCommand.getNewArtist();

        var roleSimpleDto = rolePerformanceSimpleDto.getRoleSimpleDto();
        var performanceSimpleDto = rolePerformanceSimpleDto.getPerformanceSimpleDto();

        var newArtist = findArtist(newArtistSimpleDto);
        var role = findRole(roleSimpleDto);
        var performance = findPerformance(performanceSimpleDto);

        if (Objects.nonNull(originalArtistSimpleDto)) {
            var originalArtist = findArtist(originalArtistSimpleDto);

            artistPerformanceRoleJoinRepository.updateJoinsByArtist(
                    originalArtist, newArtist,
                    performance,
                    role
            );
        } else {
            var artistPerformanceRoleJoin = makeArtistPerformanceRoleJoin(newArtist, performance, role);
            artistPerformanceRoleJoinRepository.save(artistPerformanceRoleJoin);
        }
    }

    public void delete(ArtistPerformanceRoleJoinDeleteCommand artistPerformanceRoleJoinDeleteCommand) {
        var joinId = makeJoinId(artistPerformanceRoleJoinDeleteCommand);
        artistPerformanceRoleJoinRepository.deleteById(joinId);
    }

    private EntityPrecollection precollectEntities(RoleChangeCommand roleChangeCommand) {
        return EntityPrecollection.builder()
                .withOriginalRole(findRole(roleChangeCommand.getOriginalRole()))
                .withNewRole(findRole(roleChangeCommand.getNewRole()))
                .withPerformances(findPerformances(collectPerformanceSimpleDtos(roleChangeCommand)))
                .withArtists(findArtists(collectArtistSimpleDtos(roleChangeCommand)))
                .build();
    }

    private ArtistPerformanceRoleId makeJoinId(ArtistPerformanceRoleJoinDeleteCommand artistPerformanceRoleJoinDeleteCommand) {
        return ArtistPerformanceRoleId.builder()
                .withArtist(findArtist(artistPerformanceRoleJoinDeleteCommand.getOriginalArtist()))
                .withPerformance(findPerformance(artistPerformanceRoleJoinDeleteCommand.getPerformance()))
                .withRole(findRole(artistPerformanceRoleJoinDeleteCommand.getRole()))
                .build();
    }

    private ArtistPerformanceRoleJoin makeArtistPerformanceRoleJoin(Artist newArtist, Performance performance, Role role) {
        return ArtistPerformanceRoleJoin.builder()
                .withId(makeArtistPerfromanceRoleId(newArtist, performance, role))
                .build();
    }

    private ArtistPerformanceRoleId makeArtistPerfromanceRoleId(Artist newArtist, Performance performance, Role role) {
        return ArtistPerformanceRoleId.builder()
                .withArtist(newArtist)
                .withPerformance(performance)
                .withRole(role)
                .build();
    }

    private List<Performance> findPerformances(List<PerformanceSimpleDto> performanceSimpleDtos) {
        return performanceQueryService.findBySimpleDtos(performanceSimpleDtos);
    }

    private List<Artist> findArtists(List<ArtistSimpleDto> artistSimpleDtos) {
        return artistSimpleDtos.stream()
                .map(ArtistSimpleDto::getNaturalId)
                .map(ThreadCacheUtil::getArtistBy)
                .toList();
    }

    private Role findRole(RoleSimpleDto roleSimpleDto) {
        return roleQueryService.findByNaturalId(roleSimpleDto.getNaturalId())
                .orElseThrow(() -> new RuntimeException(composeRoleNotFoundErrorMessage(roleSimpleDto)));
    }

    private Artist findArtist(ArtistSimpleDto artistSimpleDto) {
        return artistQueryService.findByNaturalId(artistSimpleDto.getNaturalId())
                .orElseThrow(() -> new RuntimeException(composeArtistNotFoundErrorMessage(artistSimpleDto)));
    }

    private Performance findPerformance(PerformanceSimpleDto performanceSimpleDto) {
        return performanceQueryService.findByNaturalId(performanceSimpleDto.getNaturalId())
                .orElseThrow(() -> new RuntimeException(composePerformanceNotFoundErrorMessage(performanceSimpleDto)));
    }

    private List<PerformanceSimpleDto> collectPerformanceSimpleDtos(RoleChangeCommand roleChangeCommand) {
        return collectDtos(roleChangeCommand, ArtistPerformanceSimpleDto::getPerformanceSimpleDto);
    }

    private List<ArtistSimpleDto> collectArtistSimpleDtos(RoleChangeCommand roleChangeCommand) {
        return collectDtos(roleChangeCommand, ArtistPerformanceSimpleDto::getArtistSimpleDto);
    }

    private void updateByRole(ArtistPerformanceSimpleDto artistPerformanceSimpleDto, EntityPrecollection entityPrecollection) {
        var originalRole = entityPrecollection.getOriginalRole();
        var newRole = entityPrecollection.getNewRole();

        var performance = getPerformance(entityPrecollection, artistPerformanceSimpleDto.getPerformanceSimpleDto());
        var artist = getArtist(entityPrecollection, artistPerformanceSimpleDto.getArtistSimpleDto());

        artistPerformanceRoleJoinRepository.updateJoinsByRole(
                originalRole, newRole,
                performance,
                artist
        );
    }

    private Artist getArtist(EntityPrecollection entityPrecollection, ArtistSimpleDto artistSimpleDto) {
        return entityPrecollection.getArtists()
                .stream()
                .filter(a -> Objects.equals(a.getNaturalId(), artistSimpleDto.getNaturalId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(composeArtistNotFoundErrorMessage(artistSimpleDto)));
    }

    private Performance getPerformance(EntityPrecollection entityPrecollection, PerformanceSimpleDto performanceSimpleDto) {
        return entityPrecollection.getPerformances()
                .stream()
                .filter(p -> Objects.equals(p.getNaturalId(), performanceSimpleDto.getNaturalId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(composePerformanceNotFoundErrorMessage(performanceSimpleDto)));
    }

    private <DTO> List<DTO> collectDtos(RoleChangeCommand roleChangeCommand, Function<ArtistPerformanceSimpleDto, DTO> dtoReader) {
        return roleChangeCommand.getArtistPerformanceSimpleDtos()
                .stream()
                .map(dtoReader)
                .toList();
    }

    private static String composeRoleNotFoundErrorMessage(RoleSimpleDto roleSimpleDto) {
        return ROLE_NOT_FOUND_ERROR_MESSAGE_PATTERN.formatted(roleSimpleDto.getNaturalId());
    }

    private static String composeArtistNotFoundErrorMessage(ArtistSimpleDto artistSimpleDto) {
        return ARTIST_NOT_FOUND_ERROR_MESSAGE_PATTERN.formatted(artistSimpleDto.getNaturalId());
    }

    private static String composePerformanceNotFoundErrorMessage(PerformanceSimpleDto performanceSimpleDto) {
        return PERFORMANCE_NOT_FOUND_ERROR_MESSAGE_PATTERN.formatted(performanceSimpleDto.getNaturalId());
    }

    @Getter
    @Builder(setterPrefix = "with")
    @AllArgsConstructor
    private static class EntityPrecollection {

        private final Role originalRole;

        private final Role newRole;

        private final List<Performance> performances;

        private final List<Artist> artists;
    }
}
