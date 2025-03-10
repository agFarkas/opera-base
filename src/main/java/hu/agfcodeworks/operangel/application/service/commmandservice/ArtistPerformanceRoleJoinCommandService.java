package hu.agfcodeworks.operangel.application.service.commmandservice;

import hu.agfcodeworks.operangel.application.dto.ArtistPerformanceSimpleDto;
import hu.agfcodeworks.operangel.application.dto.command.RoleChangeCommand;
import hu.agfcodeworks.operangel.application.model.Artist;
import hu.agfcodeworks.operangel.application.model.Performance;
import hu.agfcodeworks.operangel.application.repository.ArtistPerformanceRoleJoinRepository;
import hu.agfcodeworks.operangel.application.service.queryservice.ArtistQueryService;
import hu.agfcodeworks.operangel.application.service.queryservice.PerformanceQueryService;
import hu.agfcodeworks.operangel.application.service.queryservice.RoleQueryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
@AllArgsConstructor
public class ArtistPerformanceRoleJoinCommandService {

    private final ArtistPerformanceRoleJoinRepository artistPerformanceRoleJoinRepository;

    private final PerformanceQueryService performanceQueryService;

    private final ArtistQueryService artistQueryService;

    private final RoleQueryService roleQueryService;
    
    public void updateFromRoleToRole(RoleChangeCommand roleChangeCommand) {
        roleQueryService.findByNaturalId(roleChangeCommand.getOriginalRoleNaturalId())
                .ifPresent(originalRole -> {
                    var newRole = roleQueryService.findByNaturalId(roleChangeCommand.getNewRoleNaturalId())
                            .orElseThrow(() -> new RuntimeException("Role '%s' not found.".formatted(roleChangeCommand.getNewRoleNaturalId())));


                    artistPerformanceRoleJoinRepository.updateJoinsByRole(
                            originalRole, newRole,
                            collectPerformances(roleChangeCommand),
                            collectArtists(roleChangeCommand)
                    );
                });
    }

    private List<Artist> collectArtists(RoleChangeCommand roleChangeCommand) {
        var artistSimpleDtos = roleChangeCommand.getArtistPerformanceSimpleDtos().stream()
                .map(ArtistPerformanceSimpleDto::getArtistSimpleDto)
                .toList();

        return artistQueryService.findBySimpleDtos(artistSimpleDtos);
    }

    private List<Performance> collectPerformances(RoleChangeCommand roleChangeCommand) {
        var performanceSimpleDtos = roleChangeCommand.getArtistPerformanceSimpleDtos()
                .stream()
                .map(ArtistPerformanceSimpleDto::getPerformanceSimpleDto)
                .toList();

        return performanceQueryService.findBySimpleDtos(performanceSimpleDtos);
    }
}
