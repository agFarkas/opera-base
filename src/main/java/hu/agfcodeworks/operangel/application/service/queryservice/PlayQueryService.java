package hu.agfcodeworks.operangel.application.service.queryservice;

import hu.agfcodeworks.operangel.application.dto.PlayDetailedDto;
import hu.agfcodeworks.operangel.application.dto.PlayDto;
import hu.agfcodeworks.operangel.application.mapper.PlayDetailedDtoMapper;
import hu.agfcodeworks.operangel.application.mapper.PlayDtoMapper;
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

    private final PlayDtoMapper playDtoMapper;

    private final PlayDetailedDtoMapper playDetailedDtoMapper;

    public List<PlayDto> getAllOperas() {
        return playRepository.findOperaHeadsByType(PlayType.OPERA)
                .stream()
                .map(playDtoMapper::entityToDto)
                .collect(Collectors.toList());
    }

    public Optional<PlayDetailedDto> getPlay(UUID naturalId) {
        return playRepository.findByNaturalId(naturalId)
                .map(playDetailedDtoMapper::entityToDto);
    }
}
