package hu.agfcodeworks.operangel.application.ui.components.custom.labeled;

import hu.agfcodeworks.operangel.application.ui.renderer.CustomCellRenderer;
import hu.agfcodeworks.operangel.application.ui.uidto.ListItemWrapper;
import lombok.NonNull;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.event.ListSelectionListener;
import java.awt.BorderLayout;
import java.util.Collection;
import java.util.Comparator;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JLabeledList<I> extends JLabeledComponent<JList<ListItemWrapper<I>>> {

    private Comparator<I> itemComparator = (i1, i2) -> 0;

    private final Comparator<ListItemWrapper<I>> itemWrapperComparator = (l1, l2) -> {
        if (l1.isToAddNew()) {
            return -1;
        }

        if (l2.isToAddNew()) {
            return 1;
        }

        return itemComparator.compare(l1.getDto(), l2.getDto());
    };

    public JLabeledList(@NonNull String labelText) {
        super(labelText, new JList<>());

        removeAll();
        setLayout(new BorderLayout());
        add(label, BorderLayout.PAGE_START);
        add(component, BorderLayout.CENTER);

        component.setModel(new DefaultListModel<>());
    }

    public int getSelectedIndex() {
        return component.getSelectedIndex();
    }

    public void setSelectedIndex(int index) {
        component.setSelectedIndex(index);
    }

    public void addItems(Collection<I> items) {
        var distinctItems = items.stream()
                .sorted(itemComparator)
                .map(ListItemWrapper::of)
                .collect(Collectors.toList());

        retrieveDefaultModel()
                .addAll(distinctItems);

        sortItems();
    }

    public I getSelectedItem() {
        var index = component.getSelectedIndex();

        return retrieveDefaultModel()
                .getElementAt(index)
                .getDto();
    }

    public void setSelectedItem(I item) {
        var index = retrieveDefaultModel().indexOf(item);
        setSelectedIndex(index);
    };

    public void setTextProvider(@NonNull Function<I, String> textProvider) {
        component.setCellRenderer(new CustomCellRenderer<>(textProvider));
    }

    public void setItemComparator(@NonNull Comparator<I> itemComparator) {
        this.itemComparator = itemComparator;
    }

    private DefaultListModel<ListItemWrapper<I>> retrieveDefaultModel() {
        return (DefaultListModel<ListItemWrapper<I>>) component.getModel();
    }

    private void sortItems() {
        var itemsInCollection = streamOfItems()
                .sorted(itemWrapperComparator)
                .collect(Collectors.toList());

        var model = retrieveDefaultModel();

        model.removeAllElements();
        model.addAll(itemsInCollection);
    }

    private Stream<ListItemWrapper<I>> streamOfItems() {
        var model = retrieveDefaultModel();
        var size = model.getSize();

        var wrapperArray = new ListItemWrapper[size];

        for (var i = 0; i < size; i++) {
            wrapperArray[i] = (model.getElementAt(i));
        }

        return Stream.of(wrapperArray);
    }

    public void setFixedCellWidth(int width) {
        component.setFixedCellWidth(width);
    }

    public void addListSelectionListener(ListSelectionListener listener) {
        component.addListSelectionListener(listener);
    }

    public void removeAllItems() {
        retrieveDefaultModel()
                .removeAllElements();
    }
}
