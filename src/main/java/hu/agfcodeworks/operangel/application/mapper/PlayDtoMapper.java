package hu.agfcodeworks.operangel.application.mapper;

import hu.agfcodeworks.operangel.application.dto.PlayDto;
import hu.agfcodeworks.operangel.application.model.Play;
import hu.agfcodeworks.operangel.application.util.TextUtil;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PlayDtoMapper extends AbstractDtoMapper<Play, PlayDto> {

    private final ComposerDtoMapper composerDtoMapper;

    @Override
    public PlayDto entityToDto(@NonNull Play play) {
        return PlayDto.builder()
                .withNaturalId(play.getNaturalId())
                .withComposer(composerDtoMapper.entityToDto(play.getComposer()))
                .withTitle(play.getTitle())
                .withTitleUnified(TextUtil.unify(play.getTitle()))
                .build();
    }
}
