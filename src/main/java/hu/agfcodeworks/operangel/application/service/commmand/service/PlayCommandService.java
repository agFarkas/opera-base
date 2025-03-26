package hu.agfcodeworks.operangel.application.service.commmand.service;

import hu.agfcodeworks.operangel.application.dto.PlayListDto;
import hu.agfcodeworks.operangel.application.dto.command.PlayCommand;
import hu.agfcodeworks.operangel.application.mapper.PlayListDtoMapper;
import hu.agfcodeworks.operangel.application.model.Composer;
import hu.agfcodeworks.operangel.application.model.Play;
import hu.agfcodeworks.operangel.application.model.enums.PlayType;
import hu.agfcodeworks.operangel.application.repository.PlayRepository;
import hu.agfcodeworks.operangel.application.service.query.service.ComposerQueryService;
import hu.agfcodeworks.operangel.application.ui.util.TextUtil;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

@Transactional
@Service
@AllArgsConstructor
public class PlayCommandService {

    private final PlayRepository playRepository;

    private final PlayListDtoMapper playListDtoMapper;

    private final ComposerQueryService composerQueryService;

    public PlayListDto saveOpera(@NonNull PlayCommand playCommand) {
        if (Objects.nonNull(playCommand.getNaturalId())) {
            return update(playCommand);
        }

        return create(playCommand);
    }

    private PlayListDto create(PlayCommand playCommand) {
        var playListDto = createNew(playCommand);

        return playListDto;
    }

    private PlayListDto update(PlayCommand playCommand) {
        var newPlayDto = findAndUpdate(playCommand);

        return PlayListDto.builder()
                .withNaturalId(newPlayDto.getNaturalId())
                .withComposer(newPlayDto.getComposer())
                .withTitle(newPlayDto.getTitle())
                .withTitleUnified(TextUtil.unify(newPlayDto.getTitle()))
                .build();
    }

    private PlayListDto findAndUpdate(PlayCommand playCommand) {
        return playRepository.findByNaturalId(playCommand.getNaturalId())
                .map(play -> {
                    play.setComposer(obtainComposer(playCommand));
                    play.setTitle(playCommand.getTitle());

                    playRepository.save(play);

                    return playListDtoMapper.entityToDto(play);
                }).orElseGet((() -> makePlayListDtoByCommand(playCommand)));
    }

    private PlayListDto makePlayListDtoByCommand(PlayCommand playCommand) {
        return PlayListDto.builder()
                .withNaturalId(playCommand.getNaturalId())
                .withComposer(playCommand.getComposerDto())
                .withTitle(playCommand.getTitle())
                .withTitleUnified(TextUtil.unify(playCommand.getTitle()))
                .build();
    }

    private PlayListDto createNew(PlayCommand playCommand) {
        var entity = Play.builder()
                .withType(PlayType.OPERA)
                .withNaturalId(UUID.randomUUID())
                .withComposer(obtainComposer(playCommand))
                .withTitle(playCommand.getTitle())
                .build();

        playRepository.save(entity);

        return playListDtoMapper.entityToDto(entity);
    }

    private Composer obtainComposer(PlayCommand playCommand) {
        return composerQueryService.getComposerEntityByNaturalId(playCommand.getComposerDto().getNaturalId())
                .orElseThrow(() -> new RuntimeException("Composer Not Found"));
    }

    public void deletePlay(UUID naturalId) {
        playRepository.deleteByNaturalId(naturalId);
    }
}
