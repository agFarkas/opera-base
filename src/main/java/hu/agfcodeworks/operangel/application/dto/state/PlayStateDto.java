package hu.agfcodeworks.operangel.application.dto.state;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Builder(setterPrefix = "with")
@Getter
@AllArgsConstructor
public class PlayStateDto {

    private final List<PerformanceStateDto> performanceStateDtos;
}
