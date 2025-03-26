package hu.agfcodeworks.operangel.application.mapper;

import hu.agfcodeworks.operangel.application.dto.PlayListDto;
import hu.agfcodeworks.operangel.application.model.Play;
import hu.agfcodeworks.operangel.application.ui.util.TextUtil;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PlayListDtoMapper extends AbstractMapper<Play, PlayListDto> {

    private final ComposerDtoMapper composerDtoMapper;

    @Override
    public PlayListDto entityToDto(@NonNull Play play) {
        return PlayListDto.builder()
                .withNaturalId(play.getNaturalId())
                .withComposer(composerDtoMapper.entityToDto(play.getComposer()))
                .withTitle(play.getTitle())
                .withTitleUnified(TextUtil.unify(play.getTitle()))
                .build();
    }
}
