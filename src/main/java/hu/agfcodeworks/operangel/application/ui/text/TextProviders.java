package hu.agfcodeworks.operangel.application.ui.text;

import hu.agfcodeworks.operangel.application.dto.ArtistListDto;
import hu.agfcodeworks.operangel.application.dto.ComposerDto;
import hu.agfcodeworks.operangel.application.dto.LocationDto;
import hu.agfcodeworks.operangel.application.dto.PlayListDto;
import hu.agfcodeworks.operangel.application.dto.RoleDto;

import java.util.Objects;

public interface TextProviders {

    String NAME_PATTERN = "%s, %s";

    String PLAY_LIST_ITEM_TEXT_PATTERN = "%s: %s";

    TextProvider<LocationDto> locationTextProvider = LocationDto::getName;

    TextProvider<ComposerDto> composerTextProvider = c -> {
        if (Objects.nonNull(c.getGivenName())) {
            return NAME_PATTERN.formatted(c.getFamilyName(), c.getGivenName());
        }

        return c.getFamilyName();
    };

    TextProvider<ArtistListDto> artistTextProvider = a -> NAME_PATTERN.formatted(a.getFamilyName(), a.getGivenName());

    TextProvider<PlayListDto> composerPlayTextProvider = p -> PLAY_LIST_ITEM_TEXT_PATTERN.formatted(
            p.getComposer().getFamilyName(),
            p.getTitle()
    );

    TextProvider<RoleDto> roleTextProvider = RoleDto::getDescription;
}
