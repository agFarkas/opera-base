package hu.agfcodeworks.operangel.application.ui.text;

import hu.agfcodeworks.operangel.application.dto.ArtistListDto;
import hu.agfcodeworks.operangel.application.dto.ComposerDto;
import hu.agfcodeworks.operangel.application.dto.LocationDto;
import hu.agfcodeworks.operangel.application.dto.PlayListDto;
import hu.agfcodeworks.operangel.application.dto.RoleDto;

import java.util.Comparator;

public interface Comparators {

    Comparator<PlayListDto> playDtoByTitleComparator = Comparator.comparing(PlayListDto::getTitleUnified);

    Comparator<LocationDto> locationComparator = Comparator.comparing(LocationDto::getNameUnified);

    Comparator<ComposerDto> composerComparator = Comparator.comparing(ComposerDto::getFamilyNameUnified)
            .thenComparing(ComposerDto::getGivenNameUnified);

    Comparator<ArtistListDto> artistComparator = Comparator.comparing(ArtistListDto::getFamilyNameUnified)
            .thenComparing(ArtistListDto::getGivenNameUnified);

    Comparator<RoleDto> roleComparator = Comparator.comparing(RoleDto::getDescriptionUnified);
}
