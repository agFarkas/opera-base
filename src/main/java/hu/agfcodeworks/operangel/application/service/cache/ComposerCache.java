package hu.agfcodeworks.operangel.application.service.cache;

import hu.agfcodeworks.operangel.application.dto.ComposerDto;
import hu.agfcodeworks.operangel.application.service.query.service.ComposerQueryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ComposerCache extends AbstractCache<ComposerDto> {

    private final ComposerQueryService composerQueryService;

    @Override
    protected void fillCache() {
        composerQueryService.getAllComposers()
                .forEach(composerDto -> put(composerDto.getNaturalId(), composerDto));
    }
}
