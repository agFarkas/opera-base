package hu.agfcodeworks.operangel.application.ui.renderer;

import hu.agfcodeworks.operangel.application.ui.uidto.ListItemWrapper;
import lombok.NonNull;
import sun.swing.DefaultLookup;

import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JList;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.synth.SynthListUI;
import java.awt.Color;
import java.awt.Component;
import java.util.Objects;
import java.util.function.Function;

import static hu.agfcodeworks.operangel.application.constants.StringConstants.EMPTY_TEXT;

public class CustomCellRenderer<V> extends DefaultListCellRenderer {

    private static final Border SAFE_NO_FOCUS_BORDER = new EmptyBorder(1, 1, 1, 1);
    private static final Border DEFAULT_NO_FOCUS_BORDER = new EmptyBorder(1, 1, 1, 1);
    private Function<V, String> textProvider = Object::toString;

    public CustomCellRenderer(@NonNull Function<V, String> textProvider) {

        this.textProvider = textProvider;
    }

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        setComponentOrientation(list.getComponentOrientation());

        Color bg = null;
        Color fg = null;

        JList.DropLocation dropLocation = list.getDropLocation();
        if (dropLocation != null
                && !dropLocation.isInsert()
                && dropLocation.getIndex() == index) {

            bg = DefaultLookup.getColor(this, ui, "List.dropCellBackground");
            fg = DefaultLookup.getColor(this, ui, "List.dropCellForeground");

            isSelected = true;
        }

        if (isSelected) {
            setBackground(bg == null ? list.getSelectionBackground() : bg);
            setForeground(fg == null ? list.getSelectionForeground() : fg);
        }
        else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }

        if (value instanceof Icon) {
            setIcon((Icon)value);
            setText("");
        }
        else {
            setIcon(null);
            setText((value == null) ? "" : value.toString());
        }

        if (list.getName() == null || !list.getName().equals("ComboBox.list")
                || !(list.getUI() instanceof SynthListUI)) {
            setEnabled(list.isEnabled());
        }

        setFont(list.getFont());

        Border border = null;
        if (cellHasFocus) {
            if (isSelected) {
                border = DefaultLookup.getBorder(this, ui, "List.focusSelectedCellHighlightBorder");
            }
            if (border == null) {
                border = DefaultLookup.getBorder(this, ui, "List.focusCellHighlightBorder");
            }
        } else {
            border = getNoFocusBorder();
        }
        setBorder(border);

        return this;
    }

    private String obtainText(ListItemWrapper<V> wrapper) {
        if (Objects.isNull(wrapper)) {
            return EMPTY_TEXT;
        }

        return textProvider.apply(wrapper.getDto());
    }
}
