package hu.agfcodeworks.operangel.application.ui.editor;

import hu.agfcodeworks.operangel.application.dto.LocationDto;
import hu.agfcodeworks.operangel.application.service.cache.global.LocationCache;
import hu.agfcodeworks.operangel.application.ui.text.Comparators;
import hu.agfcodeworks.operangel.application.ui.text.TextProviders;
import hu.agfcodeworks.operangel.application.util.ContextUtil;
import lombok.NonNull;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import static hu.agfcodeworks.operangel.application.ui.constants.OperaTableConstants.FONT_STYLE_LOCATION;

public class LocationEditor extends ComboBoxTableCellEditor<LocationDto> {

    public LocationEditor(
            @NonNull Supplier<Optional<LocationDto>> itemSupplier,
            @NonNull BiConsumer<LocationDto, LocationDto> itemChangeHandler
    ) {
        super(
                TextProviders.locationTextProvider,
                Comparators.locationComparator,
                itemSupplier,
                itemChangeHandler
        );

        var comboBox = getEditorComponent();

        comboBox.addListItems(ContextUtil.getBean(LocationCache.class)
                .getAll());

        comboBox.setFont(comboBox.getFont().deriveFont(FONT_STYLE_LOCATION));
    }
}
