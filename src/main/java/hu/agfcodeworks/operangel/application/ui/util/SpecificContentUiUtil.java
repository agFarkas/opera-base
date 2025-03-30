package hu.agfcodeworks.operangel.application.ui.util;

import hu.agfcodeworks.operangel.application.dto.ArtistListDto;
import hu.agfcodeworks.operangel.application.dto.LocationDto;
import lombok.experimental.UtilityClass;

import javax.swing.JMenuItem;

import java.util.function.Consumer;

import static hu.agfcodeworks.operangel.application.ui.text.TextProviders.artistTextProvider;

@UtilityClass
public class SpecificContentUiUtil {

    public static final String CAPTION_MODIFY_LOCATION = "Helyszín módosítása";

    public static final String CAPTION_DELETE_LOCATION = "Helyszín törlése";

    public static final String CAPTION_DELETE_ARTIST_PATTERN = "%s törlése";

    public static final String CAPTION_MODIFY_ARTIST_PATTERN = "%s módosítása";
    
    public JMenuItem makeMenuItemModifyLocation(LocationDto locationDto, Consumer<LocationDto> executor) {
        return UiUtil.makeMenuItemToOpenDialog(CAPTION_MODIFY_LOCATION, e -> executor.accept(locationDto));
    }

    public JMenuItem makeMenuItemDeleteLocation(LocationDto locationDto, Consumer<LocationDto> executor) {
        return UiUtil.makeMenuItem(CAPTION_DELETE_LOCATION, e -> executor.accept(locationDto));
    }
    
    public JMenuItem makeMenuItemModifyArtist(ArtistListDto artistDto, Consumer<ArtistListDto> executor) {
        return UiUtil.makeMenuItemToOpenDialog(CAPTION_MODIFY_ARTIST_PATTERN.formatted(artistTextProvider.provide(artistDto)), e -> executor.accept(artistDto));
    }
    
    public JMenuItem makeMenuItemDeleteArtist(ArtistListDto artistDto, Consumer<ArtistListDto> executor) {
        return UiUtil.makeMenuItem(CAPTION_DELETE_ARTIST_PATTERN.formatted(artistTextProvider.provide(artistDto)), e -> executor.accept(artistDto));
    }
}
