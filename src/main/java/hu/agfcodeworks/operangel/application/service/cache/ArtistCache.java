package hu.agfcodeworks.operangel.application.service.cache;

import hu.agfcodeworks.operangel.application.dto.ArtistListDto;
import hu.agfcodeworks.operangel.application.service.query.service.ArtistQueryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ArtistCache extends AbstractCache<ArtistListDto> {

    private final ArtistQueryService artistQueryService;

    @Override
    protected void fillCache() {
        artistQueryService.getAllArtists()
                .forEach(a -> put(a.getNaturalId(), a));
    }
}
