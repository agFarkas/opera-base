package hu.agfcodeworks.operangel.application.ui.editor;

import hu.agfcodeworks.operangel.application.ui.components.custom.JCustomComboBox;
import lombok.NonNull;

import javax.swing.DefaultCellEditor;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ComboBoxTableCellEditor<V> extends DefaultCellEditor {

    private final BiConsumer<V, V> itemChangeHandler;

    public ComboBoxTableCellEditor(
            @NonNull Function<V, String> textProvider,
            @NonNull Comparator<V> comparator,
            @NonNull Supplier<Optional<V>> itemSupplier,
            @NonNull BiConsumer<V, V> itemChangeHandler
    ) {
        super(new JCustomComboBox<>());

        this.itemChangeHandler = itemChangeHandler;
        this.delegate = new EditorDelegate() {
            private V originalValue;

            @Override
            public Object getCellEditorValue() {
                var comboBox = getEditorComponent();
                var newValue = comboBox.getSelectedListItem();

                if (!Objects.equals(originalValue, newValue)) {
                    itemChangeHandler.accept(originalValue, newValue);
                }

                return newValue;
            }


            @Override
            public void setValue(Object value) {
                this.originalValue = (V) value;

                super.setValue(this.originalValue);
                var comboBox = getEditorComponent();

                if (Objects.nonNull(value)) {
                    comboBox.setSelectedListItem((V) value);
                }
            }
        };

        prepareComboBox(textProvider, comparator, itemSupplier);
    }

    private void prepareComboBox(Function<V, String> textProvider, Comparator<V> comparator, Supplier<Optional<V>> itemSupplier) {
        var comboBox = getEditorComponent();

        comboBox.setTextProvider(textProvider);
        comboBox.setItemComparator(comparator);
        comboBox.setItemSupplier(itemSupplier);
        comboBox.setProvidingNewAddition(true);
    }

    protected JCustomComboBox<V> getEditorComponent() {
        return (JCustomComboBox<V>) editorComponent;
    }
}
