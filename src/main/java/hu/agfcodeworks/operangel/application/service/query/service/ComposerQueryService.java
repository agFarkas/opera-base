package hu.agfcodeworks.operangel.application.service.query.service;

import hu.agfcodeworks.operangel.application.dto.ComposerDto;
import hu.agfcodeworks.operangel.application.mapper.ComposerDtoMapper;
import hu.agfcodeworks.operangel.application.model.Composer;
import hu.agfcodeworks.operangel.application.repository.ComposerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ComposerQueryService {

    private final ComposerRepository composerRepository;

    private final ComposerDtoMapper composerDtoMapper;

    public List<ComposerDto> getAllComposers() {
        return composerRepository.findAllComposers()
                .stream()
                .map(composerDtoMapper::entityToDto)
                .collect(Collectors.toList());
    }

    public Optional<Composer> getComposerEntityByNaturalId(UUID naturalId) {
        return composerRepository.findByNaturalId(naturalId);
    }
}
