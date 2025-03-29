package hu.agfcodeworks.operangel.application.ui.renderer;

import hu.agfcodeworks.operangel.application.ui.dto.ListItemWrapper;
import hu.agfcodeworks.operangel.application.ui.text.TextProvider;
import lombok.NonNull;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import java.awt.Component;
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
