package hu.agfcodeworks.operangel.application.service.queryservice;

import hu.agfcodeworks.operangel.application.dto.ArtistListDto;
import hu.agfcodeworks.operangel.application.dto.ArtistSimpleDto;
import hu.agfcodeworks.operangel.application.mapper.ArtistListDtoMapper;
import hu.agfcodeworks.operangel.application.model.Artist;
import hu.agfcodeworks.operangel.application.repository.ArtistRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ArtistQueryService {

    private final ArtistRepository artistRepository;

    private final ArtistListDtoMapper artistListDtoMapper;

    public List<ArtistListDto> getAllArtists() {
        return artistRepository.findAllArtists()
                .stream()
                .map(artistListDtoMapper::entityToDto)
                .collect(Collectors.toList());
    }

    public List<Artist> findBySimpleDtos(List<ArtistSimpleDto> artistSimpleDtos) {
        var naturalIds = artistSimpleDtos.stream()
                .map(ArtistSimpleDto::getNaturalId)
                .toList();

        return artistRepository.findByNaturalIds(naturalIds);
    }

    public Optional<Artist> findByNaturalId(UUID naturalId) {
        return artistRepository.findByNaturalId(naturalId);
    }
}
