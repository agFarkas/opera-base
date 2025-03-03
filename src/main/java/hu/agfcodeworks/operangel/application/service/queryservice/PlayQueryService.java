package hu.agfcodeworks.operangel.application.service.queryservice;

import hu.agfcodeworks.operangel.application.dto.PlayDetailedDto;
import hu.agfcodeworks.operangel.application.dto.PlayListDto;
import hu.agfcodeworks.operangel.application.mapper.PlayDetailedDtoMapper;
import hu.agfcodeworks.operangel.application.mapper.PlayListDtoMapper;
import hu.agfcodeworks.operangel.application.model.Play;
import hu.agfcodeworks.operangel.application.model.enums.PlayType;
import hu.agfcodeworks.operangel.application.repository.PlayRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PlayQueryService {

    private final PlayRepository playRepository;

    private final PlayListDtoMapper playListDtoMapper;

    private final PlayDetailedDtoMapper playDetailedDtoMapper;

    public List<PlayListDto> getAllOperas() {
        return playRepository.findOperaHeadsByType(PlayType.OPERA)
                .stream()
                .map(playListDtoMapper::entityToDto)
                .collect(Collectors.toList());
    }

    public Optional<PlayDetailedDto> getPlay(UUID naturalId) {
        return playRepository.findByNaturalId(naturalId)
                .map(playDetailedDtoMapper::entityToDto);
    }

    public Optional<Play> getPlayByNaturalId(UUID naturalId) {
        return playRepository.findByNaturalId(naturalId);
    }
}
