package hu.agfcodeworks.operangel.application.mapper;

import hu.agfcodeworks.operangel.application.dto.ComposerDto;
import hu.agfcodeworks.operangel.application.model.Composer;
import hu.agfcodeworks.operangel.application.util.TextUtil;
import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
public class ComposerDtoMapper extends AbstractDtoMapper<Composer, ComposerDto> {

    @Override
    public ComposerDto entityToDto(@NonNull Composer composer) {
        return ComposerDto.builder()
                .withGivenName(composer.getGivenName())
                .withGivenNameUnified(TextUtil.unify(composer.getGivenName()))
                .withFamilyName(composer.getFamilyName())
                .withFamilyNameUnified(TextUtil.unify(composer.getFamilyName()))
                .build();
    }
}
