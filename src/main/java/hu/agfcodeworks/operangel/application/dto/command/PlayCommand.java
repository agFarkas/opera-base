package hu.agfcodeworks.operangel.application.dto.command;

import hu.agfcodeworks.operangel.application.dto.ComposerDto;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder(setterPrefix = "with")
public class PlayCommand {

    private final UUID naturalId;

    private final ComposerDto composerDto;

    private final String title;
}
