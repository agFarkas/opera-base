package hu.agfcodeworks.operangel.application.service.cache;

import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;

@NoArgsConstructor
public abstract class AbstractCache<DTO> {

    private final Map<UUID, DTO> cache = new HashMap<>();

    private boolean cacheIsFilled = false;

    public DTO get(@NonNull UUID naturalId) {
        if (!cacheIsFilled) {
            fillCache();
            cacheIsFilled = true;
        }

        var dto = cache.get(naturalId);

        if(Objects.isNull(dto)) {
            return getDefault();
        }

        return dto;
    }

    public DTO getDefault() {
        return null;
    }

    public List<DTO> getAll() {
        if (!cacheIsFilled) {
            fillCache();
            cacheIsFilled = true;
        }

        return new ArrayList<>(cache.values());
    }

    public DTO getOPutDefault(@NonNull UUID naturalId, @NonNull Supplier<DTO> defaultDtoSupplier) {
        if (!cache.containsKey(naturalId)) {
            var defaultDto = defaultDtoSupplier.get();

            cache.put(naturalId, defaultDto);
            return defaultDto;
        }

        return cache.get(naturalId);
    }

    public void put(@NonNull UUID naturalId, @NonNull DTO dto) {
        cache.put(naturalId, dto);
    }

    protected abstract void fillCache();


    public void remove(UUID naturalId) {
        cache.remove(naturalId);
    }
}
