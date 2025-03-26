package hu.agfcodeworks.operangel.application.service.commmand.service;

import hu.agfcodeworks.operangel.application.dto.ArtistPerformanceSimpleDto;
import hu.agfcodeworks.operangel.application.dto.ArtistSimpleDto;
import hu.agfcodeworks.operangel.application.dto.PerformanceSimpleDto;
import hu.agfcodeworks.operangel.application.dto.RoleSimpleDto;
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


}
