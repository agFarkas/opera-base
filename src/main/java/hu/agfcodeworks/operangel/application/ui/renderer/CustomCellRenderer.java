package hu.agfcodeworks.operangel.application.ui.renderer;

import hu.agfcodeworks.operangel.application.ui.uidto.ListItemWrapper;
import lombok.NonNull;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import java.awt.Component;
import java.util.Objects;
import java.util.function.Function;

import static hu.agfcodeworks.operangel.application.constants.StringConstants.EMPTY_TEXT;

public class CustomCellRenderer<V> extends DefaultListCellRenderer {

    private final Function<V, String> textProvider;

    public CustomCellRenderer(@NonNull Function<V, String> textProvider) {
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

        return textProvider.apply(wrapper.getDto());
    }
}
