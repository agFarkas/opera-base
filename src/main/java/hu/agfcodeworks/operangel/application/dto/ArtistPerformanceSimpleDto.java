package hu.agfcodeworks.operangel.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder(setterPrefix = "with")
@EqualsAndHashCode
@AllArgsConstructor
@Getter
public class ArtistPerformanceSimpleDto {

    private final PerformanceSimpleDto performanceSimpleDto;

    private final ArtistSimpleDto artistSimpleDto;
}
