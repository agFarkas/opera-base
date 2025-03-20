package hu.agfcodeworks.operangel.application.dto.command;

import hu.agfcodeworks.operangel.application.dto.state.PlayStateDto;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder(setterPrefix = "with")
public class PlayPerformanceChangeCommand {

    private final UUID playNaturalId;

    private PlayStateDto originalPlayState;

    private PlayStateDto newPlayState;
}
