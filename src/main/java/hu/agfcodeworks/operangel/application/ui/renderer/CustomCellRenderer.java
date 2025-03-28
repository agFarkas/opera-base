package hu.agfcodeworks.operangel.application.ui.renderer;

import hu.agfcodeworks.operangel.application.ui.text.TextProvider;
import hu.agfcodeworks.operangel.application.ui.dto.ListItemWrapper;
import lombok.NonNull;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

import static hu.agfcodeworks.operangel.application.constants.StringConstants.EMPTY_TEXT;

public class CustomCellRenderer<V> extends DefaultListCellRenderer {

    private final @NonNull TextProvider<V> textProvider;

    public CustomCellRenderer(@NonNull TextProvider<V> textProvider) {
        this.textProvider = textProvider;
    }

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        if (value instanceof ListItemWrapper wrapper) {
            setText(obtainText(wrapper));
        }

        return this;
    }

    private String obtainText(ListItemWrapper<V> wrapper) {
        if (Objects.isNull(wrapper)) {
            return EMPTY_TEXT;
        }

        return textProvider.provide(wrapper.getDto());
    }
}
