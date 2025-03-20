package hu.agfcodeworks.operangel.application.service.commmand.service;

import hu.agfcodeworks.operangel.application.dto.ComposerDto;
import hu.agfcodeworks.operangel.application.mapper.ComposerDtoMapper;
import hu.agfcodeworks.operangel.application.model.Composer;
import hu.agfcodeworks.operangel.application.repository.ComposerRepository;
import hu.agfcodeworks.operangel.application.service.cache.ComposerCache;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

@Transactional
@Service
@AllArgsConstructor
public class ComposerCommandService {

    private final ComposerRepository composerRepository;

    private final ComposerCache composerCache;

    private final ComposerDtoMapper composerDtoMapper;

    public ComposerDto save(@NonNull ComposerDto composerDto) {
        if (Objects.nonNull(composerDto.getNaturalId())) {
            return update(composerDto);
        }

        return create(composerDto);
    }

    private ComposerDto create(ComposerDto composerDto) {
        var newArtistDto = createNew(composerDto);
        composerCache.put(newArtistDto.getNaturalId(), newArtistDto);
        return newArtistDto;
    }

    private ComposerDto update(ComposerDto composerDto) {
        var newComposerDto = findAndUpdate(composerDto);
        composerCache.put(newComposerDto.getNaturalId(), newComposerDto);

        return composerDto;
    }

    private ComposerDto findAndUpdate(ComposerDto composerDto) {
        return composerRepository.findByNaturalId(composerDto.getNaturalId())
                .map(composer -> {
                    composer.setGivenName(composerDto.getGivenName());
                    composer.setFamilyName(composerDto.getFamilyName());

                    composerRepository.save(composer);

                    return composerDtoMapper.entityToDto(composer);
                }).orElse(composerDto);
    }

    private ComposerDto createNew(ComposerDto composerDto) {
        var entity = Composer.builder()
                .withNaturalId(UUID.randomUUID())
                .withGivenName(composerDto.getGivenName())
                .withFamilyName(composerDto.getFamilyName())
                .build();

        composerRepository.save(entity);

        return composerDtoMapper.entityToDto(entity);
    }
}
