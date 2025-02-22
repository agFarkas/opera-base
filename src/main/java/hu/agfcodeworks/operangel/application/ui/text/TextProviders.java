package hu.agfcodeworks.operangel.application.ui.text;

import hu.agfcodeworks.operangel.application.dto.ArtistListDto;
import hu.agfcodeworks.operangel.application.dto.ComposerDto;
import hu.agfcodeworks.operangel.application.dto.LocationDto;
import hu.agfcodeworks.operangel.application.dto.RoleDto;

import java.util.Objects;
import java.util.function.Function;

public interface TextProviders {

    String NAME_PATTERN = "%s, %s";

    Function<LocationDto, String> locationTextProvider = LocationDto::getName;

    Function<ComposerDto, String> composerTextProvider = c -> {
        if (Objects.nonNull(c.getGivenName())) {
            return NAME_PATTERN.formatted(c.getFamilyName(), c.getGivenName());
        }

        return c.getFamilyName();
    };

    Function<ArtistListDto, String> artistTextProvider = a -> NAME_PATTERN.formatted(a.getFamilyName(), a.getGivenName());

    Function<RoleDto, String> roleTextProvider = RoleDto::getDescription;
}
