package hu.agfcodeworks.operangel.application.ui.components.custom;

import hu.agfcodeworks.operangel.application.ui.components.custom.uidto.ListItemWrapper;
import hu.agfcodeworks.operangel.application.ui.components.custom.uidto.ValidationStatus;
import hu.agfcodeworks.operangel.application.ui.renderer.CustomComboBoxRenderer;
import lombok.NonNull;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.ListCellRenderer;
import java.awt.event.ItemEvent;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JCustomComboBox<I> extends JComboBox<ListItemWrapper<I>> implements Validated {

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

    private Supplier<Optional<I>> itemSupplier = Optional::empty;

    private boolean providingNewAddition = false;

    private int itemIndex = -1;

    private int deselectedIndex = -1;

    private boolean mandatory = false;

    public JCustomComboBox() {
        super.addItemListener(event -> {
            switch (event.getStateChange()) {
                case ItemEvent.DESELECTED -> deselectedIndex = getSelectedIndex();
                case ItemEvent.SELECTED -> reactToSelectedWrapper((ListItemWrapper<I>) event.getItem());
            }
        });
    }

    private void reactToSelectedWrapper(ListItemWrapper<I> selectedWrapper) {
        if (selectedWrapper.isToAddNew()) {
            processNewItemOpt(itemSupplier.get());
        } else {
            itemIndex = super.getSelectedIndex();
        }
    }

    private void processNewItemOpt(Optional<I> newItemOpt) {
        if (newItemOpt.isPresent()) {
            var newWrapper = ListItemWrapper.of(newItemOpt.get());

            addWrapper(newWrapper);
            retrieveDefaultModel().setSelectedItem(newWrapper);
        } else {
            if (retrieveDefaultModel().getSize() == 1) {
                itemIndex = deselectedIndex;
            }

            setSelectedIndex(itemIndex);
        }
    }

    @Deprecated
    @Override
    public void setRenderer(ListCellRenderer<? super ListItemWrapper<I>> aRenderer) {
        super.setRenderer(aRenderer);
    }

    public void addListItem(@NonNull I item) {
        addWrapper(ListItemWrapper.of(item));
    }

    @Deprecated
    @Override
    public void addItem(ListItemWrapper<I> item) {
        super.addItem(item);
    }

    private void addWrapper(ListItemWrapper<I> listItemWrapper) {
        retrieveDefaultModel()
                .insertElementAt(listItemWrapper, calculatePosition(listItemWrapper));
    }

    private int calculatePosition(ListItemWrapper<I> wrapper) {
        var model = retrieveDefaultModel();
        var initialIndex = determineInitialIndex();
        var size = model.getSize();

        for (var i = initialIndex; i < size; i++) {
            var wrapperElement = model.getElementAt(i);

            if (itemWrapperComparator.compare(wrapper, wrapperElement) < 0) {
                return i;
            }
        }

        return size;
    }

    public void setItemComparator(@NonNull Comparator<I> itemComparator) {
        this.itemComparator = itemComparator;
        sortItems();
    }

    public boolean isProvidingNewAddition() {
        return providingNewAddition;
    }

    public void setProvidingNewAddition(boolean providingNewAddition) {
        var originalProvidingNewCreation = this.providingNewAddition;

        if (!originalProvidingNewCreation && providingNewAddition) {
            retrieveDefaultModel()
                    .insertElementAt(ListItemWrapper.ofEmpty(), 0);
        } else if (originalProvidingNewCreation && !providingNewAddition) {
            retrieveDefaultModel()
                    .removeElementAt(0);
        }

        this.providingNewAddition = providingNewAddition;
    }

    public void setTextProvider(@NonNull Function<I, String> textProvider) {
        super.setRenderer(new CustomComboBoxRenderer<>(textProvider));
    }

    public Supplier<Optional<I>> getItemSupplier() {
        return itemSupplier;
    }

    public void setItemSupplier(@NonNull Supplier<Optional<I>> itemSupplier) {
        this.itemSupplier = itemSupplier;
    }

    public I getSelectedListItem() {
        var wrapper = (ListItemWrapper<I>) super.getSelectedItem();
        if (Objects.isNull(wrapper)) {
            return null;
        }

        return wrapper.getDto();
    }

    public void setSelectedListItem(I item) {
        setSelectedItem(new ListItemWrapper<>(item));
    }

    public void addListItems(@NonNull Collection<@NonNull I> items) {
        var distinctItems = items.stream()
                .sorted(itemComparator)
                .map(ListItemWrapper::of)
                .collect(Collectors.toList());

        retrieveDefaultModel()
                .addAll(distinctItems);

        sortItems();
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

    private int determineInitialIndex() {
        return isProvidingNewAddition() ? 1 : 0;
    }

    private DefaultComboBoxModel<ListItemWrapper<I>> retrieveDefaultModel() {
        return (DefaultComboBoxModel<ListItemWrapper<I>>) super.getModel();
    }

    public List<I> getItems() {
        return streamOfItems()
                .filter(listItemWrapper -> !listItemWrapper.isToAddNew())
                .map(ListItemWrapper::getDto)
                .collect(Collectors.toList());
    }

    public void removeListItem(@NonNull I item) {
        var model = retrieveDefaultModel();
        var size = model.getSize();

        if (providingNewAddition && (Objects.equals(item, getSelectedListItem()) || size == 2)) {
            setSelectedIndex(-1);
        } else if (!providingNewAddition && size == 1) {
            setSelectedIndex(0);
        }

        var wrapper = ListItemWrapper.of(item);

        model.removeElement(wrapper);
    }

    @Deprecated
    @Override
    public void removeItem(Object anObject) {
        super.removeItem(anObject);
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    public Set<ValidationStatus> getValidationStatus() {
        if (mandatory && getSelectedIndex() < 0) {
            return Set.of(ValidationStatus.INVALID_FOR_MANDATORY);
        }

        return Collections.emptySet();
    }
}
