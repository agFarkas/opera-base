package hu.agfcodeworks.operangel.application.mapper;

import hu.agfcodeworks.operangel.application.dto.ArtistListDto;
import hu.agfcodeworks.operangel.application.dto.ComposerDto;
import hu.agfcodeworks.operangel.application.model.Artist;
import hu.agfcodeworks.operangel.application.util.TextUtil;
import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
public class ArtistListDtoMapper extends AbstractDtoMapper<Artist, ArtistListDto> {

    @Override
    public ArtistListDto entityToDto(@NonNull Artist artist) {
        return ArtistListDto.builder()
                .withNaturalId(artist.getNaturalId())
                .withGivenName(artist.getGivenName())
                .withGivenNameUnified(TextUtil.unify(artist.getGivenName()))
                .withFamilyName(artist.getFamilyName())
                .withFamilyNameUnified(TextUtil.unify(artist.getFamilyName()))
                .build();
    }
}
