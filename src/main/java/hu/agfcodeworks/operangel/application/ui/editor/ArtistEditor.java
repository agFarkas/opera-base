package hu.agfcodeworks.operangel.application.ui.editor;

import hu.agfcodeworks.operangel.application.dto.ArtistListDto;
import hu.agfcodeworks.operangel.application.service.cache.ArtistCache;
import hu.agfcodeworks.operangel.application.ui.text.Comparators;
import hu.agfcodeworks.operangel.application.ui.text.TextProviders;
import hu.agfcodeworks.operangel.application.util.ContextUtil;
import lombok.NonNull;

import java.util.Optional;
import java.util.function.Supplier;

public class ArtistEditor extends ComboBoxTableCellEditor<ArtistListDto> {

    public ArtistEditor(
            @NonNull Supplier<Optional<ArtistListDto>> itemSupplier, int fontStyle
    ) {
        super(TextProviders.artistTextProvider, Comparators.artistComparator, itemSupplier);
        var comboBox = getEditorComponent();

        comboBox.addListItems(ContextUtil.getBean(ArtistCache.class)
                .getAll());

        comboBox.setFont(comboBox.getFont().deriveFont(fontStyle));
    }
}
