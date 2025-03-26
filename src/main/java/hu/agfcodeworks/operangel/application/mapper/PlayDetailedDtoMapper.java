package hu.agfcodeworks.operangel.application.mapper;

import hu.agfcodeworks.operangel.application.dto.PlayDetailedDto;
import hu.agfcodeworks.operangel.application.model.Play;
import hu.agfcodeworks.operangel.application.ui.util.TextUtil;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class PlayDetailedDtoMapper extends AbstractMapper<Play, PlayDetailedDto> {

    private final ComposerDtoMapper composerDtoMapper;

    private final RoleDtoMapper roleDtoMapper;

    @Override
    public PlayDetailedDto entityToDto(@NonNull Play play) {
        return PlayDetailedDto.builder()
                .withNaturalId(play.getNaturalId())
                .withComposer(composerDtoMapper.entityToDto(play.getComposer()))
                .withTitle(play.getTitle())
                .withTitleUnified(TextUtil.unify(play.getTitle()))
                .withRoles(
                        play.getRoles()
                                .stream()
                                .map(roleDtoMapper::entityToDto)
                                .collect(Collectors.toList())
                ).build();
    }
}
