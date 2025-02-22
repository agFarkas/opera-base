package hu.agfcodeworks.operangel.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Builder(setterPrefix = "with")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@Getter
public class PlayDetailedDto {

    @EqualsAndHashCode.Include
    private final UUID naturalId;

    private final ComposerDto composer;

    private final String title;

    private final String titleUnified;

    private final List<RoleDto> roles;

    private final List<PerformanceDto> performances;
}
