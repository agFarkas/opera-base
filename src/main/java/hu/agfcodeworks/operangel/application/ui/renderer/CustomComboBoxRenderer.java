package hu.agfcodeworks.operangel.application.ui.renderer;

import hu.agfcodeworks.operangel.application.ui.text.TextProvider;
import hu.agfcodeworks.operangel.application.ui.dto.ListItemWrapper;
import lombok.AllArgsConstructor;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import java.awt.Component;
import java.util.Objects;

import static hu.agfcodeworks.operangel.application.constants.StringConstants.EMPTY_TEXT;
import static hu.agfcodeworks.operangel.application.ui.constants.UiConstants.LIST_ITEM_CREATE_NEW;

@AllArgsConstructor
public class CustomComboBoxRenderer<V> extends BasicComboBoxRenderer {

    private static final Icon DEFAULT_ICON = new ImageIcon();

    private static final String CREATE_NEW_PATTERN = "[[%s]]";

    private final TextProvider<V> textProvider;

    private final boolean showIcon;

    public CustomComboBoxRenderer() {
        this.textProvider = Object::toString;
        this.showIcon = false;
    }

    public CustomComboBoxRenderer(TextProvider<V> textProvider) {
        this.textProvider = textProvider;
        this.showIcon = false;
    }

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        setColors(list, isSelected);
        setFont(list.getFont());

        var wrapper = (ListItemWrapper<V>) value;

        setText(obtainText(wrapper));

        if (showIcon) {
            setIcon(obtainIcon(wrapper));
        }

        return this;
    }

    private void setColors(JList<?> list, boolean isSelected) {
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
    }

    private Icon obtainIcon(ListItemWrapper<V> wrapper) {
        if (Objects.isNull(wrapper) || Objects.isNull(wrapper.getIcon())) {
            return DEFAULT_ICON;
        }

        return wrapper.getIcon();
    }

    private String obtainText(ListItemWrapper<V> wrapper) {
        if (Objects.isNull(wrapper)) {
            return EMPTY_TEXT;
        }

        if (wrapper.isToAddNew()) {
            return CREATE_NEW_PATTERN.formatted(LIST_ITEM_CREATE_NEW);
        }

        if (wrapper.isEmpty()) {
            return EMPTY_TEXT;
        }

        return textProvider.provide(wrapper.getDto());
    }
}
