package hu.agfcodeworks.operangel.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder(setterPrefix = "with")
@EqualsAndHashCode
@AllArgsConstructor
@Getter
public class RolePerformanceSimpleDto {

    private final PerformanceSimpleDto performanceSimpleDto;

    private final RoleSimpleDto roleSimpleDto;
}
