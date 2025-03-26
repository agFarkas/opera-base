package hu.agfcodeworks.operangel.application.util;

import hu.agfcodeworks.operangel.application.model.Artist;
import hu.agfcodeworks.operangel.application.model.Location;
import hu.agfcodeworks.operangel.application.model.Play;
import hu.agfcodeworks.operangel.application.model.Role;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@UtilityClass
public class ThreadCacheUtil {

    private static final String LOCATION_NOT_FOUND_ERROR_MESSAGE_PATTERN = "Location '%s' not found.";
    private static final String ARTIST_NOT_FOUND_ERROR_MESSAGE_PATTERN = "Artist '%s' not found.";
    private static final String ROLE_NOT_FOUND_ERROR_MESSAGE_PATTERN = "Role '%s' not found.";

    private final ThreadLocal<Play> playThreadLocal = new ThreadLocal<>();
    private final ThreadLocal<Set<Location>> locationThreadLocal = new ThreadLocal<>();
    private final ThreadLocal<Set<Artist>> artistThreadLocal = new ThreadLocal<>();
    private final ThreadLocal<Set<Role>> roleThreadLocal = new ThreadLocal<>();

    public void storePlay(Play play) {
        playThreadLocal.set(play);
    }

    public void storeLocations(@NonNull Set<Location> locations) {
        locationThreadLocal.set(locations);
    }

    public void storeArtists(@NonNull Set<Artist> artists) {
        artistThreadLocal.set(artists);
    }

    public void storeRoles(@NonNull Set<Role> roles) {
        roleThreadLocal.set(roles);
    }

    public void clearAll() {
        clearLocations();
        clearArtists();
        clearRoles();
    }

    private void clearPlay() {
        playThreadLocal.remove();
    }

    private void clearLocations() {
        locationThreadLocal.remove();
    }

    public void clearArtists() {
        artistThreadLocal.remove();
    }

    public void clearRoles() {
        roleThreadLocal.remove();
    }

    public boolean isPlayEmpty() {
        return Objects.isNull(playThreadLocal.get());
    }

    public boolean isLocationssEmpty() {
        return Objects.isNull(locationThreadLocal.get());
    }

    public boolean isArtistsEmpty() {
        return Objects.isNull(artistThreadLocal.get());
    }

    public boolean isRolesEmpty() {
        return Objects.isNull(roleThreadLocal.get());
    }

    public boolean areAllEmpty() {
        return isPlayEmpty()
                && isLocationssEmpty()
                && isArtistsEmpty()
                && isRolesEmpty();
    }

    public Play getPlay() {
        return playThreadLocal.get();
    }

    public Location getLocationBy(@NonNull UUID naturalId) {
        if (!isLocationssEmpty()) {
            return locationThreadLocal.get()
                    .stream()
                    .filter(location -> Objects.equals(location.getNaturalId(), naturalId))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException(LOCATION_NOT_FOUND_ERROR_MESSAGE_PATTERN.formatted(naturalId)));
        }

        throw new RuntimeException("Set of cached artists is empty.");
    }

    public Artist getArtistBy(@NonNull UUID naturalId) {
        if (!isArtistsEmpty()) {
            return artistThreadLocal.get()
                    .stream()
                    .filter(artist -> Objects.equals(artist.getNaturalId(), naturalId))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException(ARTIST_NOT_FOUND_ERROR_MESSAGE_PATTERN.formatted(naturalId)));
        }

        throw new RuntimeException("Set of cached artists is empty.");
    }

    public Role getRoleBy(@NonNull UUID naturalId) {
        if (!isRolesEmpty()) {
            return roleThreadLocal.get()
                    .stream()
                    .filter(role -> Objects.equals(role.getNaturalId(), naturalId))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException(ROLE_NOT_FOUND_ERROR_MESSAGE_PATTERN.formatted(naturalId)));
        }

        throw new RuntimeException("Set of cached roles is empty.");
    }
}
