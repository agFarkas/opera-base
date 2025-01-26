package hu.agfcodeworks.operangel.application.ui.components.custom.labeled;

import hu.agfcodeworks.operangel.application.ui.components.custom.JCustomComboBox;
import hu.agfcodeworks.operangel.application.ui.components.custom.uidto.ValidationStatus;
import lombok.NonNull;

import javax.swing.ComboBoxEditor;
import java.util.Collection;
import java.util.Set;
import java.util.function.Function;

public class JLabeledComboBox<I> extends JLabeledComponent<JCustomComboBox<I>> {

    public JLabeledComboBox(@NonNull String labelText) {
        super(labelText, new JCustomComboBox<>());
    }

    public void setEditable(boolean editable) {
        component.setEditable(editable);
    }

    public void setEditor(@NonNull ComboBoxEditor comboBoxEditor) {
        component.setEditor(comboBoxEditor);
    }

    public void setMaximumRowCount(int rowCount) {
        component.setMaximumRowCount(rowCount);
    }

    public int getSelectedIndex() {
        return component.getSelectedIndex();
    }

    public void setSelectedIndex(int index) {
        component.setSelectedIndex(index);
    }

    public void addItems(Collection<I> items) {
        component.addListItems(items);
    }

    public I getSelectedItem() {
        return component.getSelectedListItem();
    }

    public void setSelectedItem(I item) {
        component.setSelectedListItem(item);
    }

    public void setTextProvider(@NonNull Function<I, String> textProvider) {
        component.setTextProvider(textProvider);
    }

    public Set<ValidationStatus> getValidationStatus() {
        return component.getValidationStatus();
    }
}
