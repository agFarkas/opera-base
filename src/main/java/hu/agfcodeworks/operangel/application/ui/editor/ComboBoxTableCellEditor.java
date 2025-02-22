package hu.agfcodeworks.operangel.application.ui.editor;

import hu.agfcodeworks.operangel.application.ui.components.custom.JCustomComboBox;
import lombok.NonNull;

import javax.swing.DefaultCellEditor;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public class ComboBoxTableCellEditor<V> extends DefaultCellEditor {
    public ComboBoxTableCellEditor(
            @NonNull Function<V, String> textProvider,
            @NonNull Comparator<V> comparator,
            @NonNull Supplier<Optional<V>> itemSupplier
    ) {
        super(new JCustomComboBox<>());

        var comboBox = getEditorComponent();

        comboBox.setTextProvider(textProvider);
        comboBox.setItemComparator(comparator);
        comboBox.setItemSupplier(itemSupplier);
        comboBox.setProvidingNewAddition(true);

        this.delegate = new EditorDelegate() {

            @Override
            public Object getCellEditorValue() {
                var comboBox = getEditorComponent();
                var value = comboBox.getSelectedListItem();

                return value;
            }


            @Override
            public void setValue(Object value) {
                super.setValue(value);
                var comboBox = getEditorComponent();

                if (Objects.nonNull(value)) {
                    comboBox.setSelectedListItem((V) value);
                }
            }
        };
    }

    protected JCustomComboBox<V> getEditorComponent() {
        return (JCustomComboBox<V>) editorComponent;
    }
}
