package hu.agfcodeworks.operangel.application.service.commmand.service;

import hu.agfcodeworks.operangel.application.dto.ArtistListDto;
import hu.agfcodeworks.operangel.application.mapper.ArtistListDtoMapper;
import hu.agfcodeworks.operangel.application.model.Artist;
import hu.agfcodeworks.operangel.application.repository.ArtistRepository;
import hu.agfcodeworks.operangel.application.service.cache.ArtistCache;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

@Transactional
@Service
@AllArgsConstructor
public class ArtistCommandService {

    private final ArtistRepository artistRepository;

    private final ArtistCache artistCache;

    private final ArtistListDtoMapper artistListDtoMapper;

    public ArtistListDto save(@NonNull ArtistListDto artistDto) {
        if (Objects.nonNull(artistDto.getNaturalId())) {
            return update(artistDto);
        }

        return create(artistDto);
    }

    private ArtistListDto create(ArtistListDto artistDto) {
        var newArtistDto = createNew(artistDto);
        artistCache.put(newArtistDto.getNaturalId(), newArtistDto);

        return newArtistDto;
    }

    private ArtistListDto update(ArtistListDto artistDto) {
        var newArtistDto = findAndUpdate(artistDto);
        artistCache.put(newArtistDto.getNaturalId(), newArtistDto);

        return artistDto;
    }

    private ArtistListDto findAndUpdate(ArtistListDto artistDto) {
        return artistRepository.findByNaturalId(artistDto.getNaturalId())
                .map(artist -> {
                    artist.setGivenName(artistDto.getGivenName());
                    artist.setFamilyName(artistDto.getFamilyName());

                    artistRepository.save(artist);

                    return artistListDtoMapper.entityToDto(artist);
                }).orElse(artistDto);
    }

    private ArtistListDto createNew(ArtistListDto artistDto) {
        var entity = Artist.builder()
                .withNaturalId(UUID.randomUUID())
                .withGivenName(artistDto.getGivenName())
                .withFamilyName(artistDto.getFamilyName())
                .build();

        artistRepository.save(entity);

        return artistListDtoMapper.entityToDto(entity);
    }
}
