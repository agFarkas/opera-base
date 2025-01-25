package hu.agfcodeworks.operangel.application.ui.custom.components;

import hu.agfcodeworks.operangel.application.ui.components.custom.JCustomComboBox;
import hu.agfcodeworks.operangel.application.ui.custom.components.itemdto.TestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class JCustomComboBoxTest {

    private JCustomComboBox<TestDto> comboBox;

    @BeforeEach
    void init() {
        this.comboBox = new JCustomComboBox<>();
    }

    @Test
    void changeItemComparatorTest() {
        fillComboBox(20, 4, 5, 2, 16, 12, 13);

        comboBox.setItemComparator(Comparator.comparing(TestDto::getNumber));
        assertThat(comboBox.getItems()).isEqualTo(List.of(
                new TestDto(2),
                new TestDto(4),
                new TestDto(5),
                new TestDto(12),
                new TestDto(13),
                new TestDto(16),
                new TestDto(20)
        ));
    }

    @Test
    void addItemsOneByOneTest() {
        fillComboBox(6, 2, 62);

        comboBox.setItemComparator(Comparator.comparing(TestDto::getNumber));
        comboBox.addListItems(List.of(
                new TestDto(1),
                new TestDto(9),
                new TestDto(7),
                new TestDto(22),
                new TestDto(19)
        ));

        assertThat(comboBox.getItems()).isEqualTo(List.of(
                new TestDto(1),
                new TestDto(2),
                new TestDto(6),
                new TestDto(7),
                new TestDto(9),
                new TestDto(19),
                new TestDto(22),
                new TestDto(62)
        ));
    }

    @Test
    void removeAllItemsOneByOneWhenNotProvidingNewAdditionTest() {
        fillComboBox(4, 2, 5);

        comboBox.setProvidingNewAddition(false);
        comboBox.setSelectedIndex(2);

        comboBox.removeListItem(new TestDto(4));
        comboBox.removeListItem(new TestDto(2));
        comboBox.removeListItem(new TestDto(5));

        assertThat(comboBox.getSelectedIndex()).isEqualTo(-1);
    }

    @Test
    void removeAllItemsOneByOneWhenProvidingNewAdditionTest() {
        fillComboBox(4, 2, 5, 1);

        comboBox.setProvidingNewAddition(true);
        comboBox.setSelectedIndex(2);

        comboBox.removeListItem(new TestDto(2));
        comboBox.removeListItem(new TestDto(1));
        comboBox.removeListItem(new TestDto(4));
        comboBox.removeListItem(new TestDto(5));

        assertThat(comboBox.getSelectedIndex()).isEqualTo(-1);
        comboBox.setSelectedIndex(0);
        assertThat(comboBox.getSelectedIndex()).isEqualTo(-1);
    }

    @Test
    void removeSelectedItemWhenProvidingNewAdditionTest() {
        fillComboBox(2, 4, 5, 1);

        comboBox.setProvidingNewAddition(true);
        comboBox.setSelectedIndex(2);

        comboBox.removeListItem(new TestDto(4));

        assertThat(comboBox.getSelectedIndex()).isEqualTo(-1);
    }

    @Test
    void removeSelectedItemWhenNotProvidingNewAdditionTest() {
        fillComboBox(2, 4, 5, 1);

        comboBox.setProvidingNewAddition(false);
        comboBox.setSelectedIndex(2);

        comboBox.removeListItem(new TestDto(5));

        assertThat(comboBox.getSelectedIndex()).isEqualTo(1);
    }

    @Test
    void removeSelectedLastItemWhenNotProvidingNewAdditionTest() {
        fillComboBox(2, 4, 5, 1);

        comboBox.setProvidingNewAddition(false);
        comboBox.setSelectedIndex(3);

        comboBox.removeListItem(new TestDto(1));

        assertThat(comboBox.getSelectedIndex()).isEqualTo(2);
    }

    @Test
    void removeOneBeforeSelectedItemWhenProvidingNewAdditionTest() {
        fillComboBox(2, 4, 5, 1);

        comboBox.setProvidingNewAddition(true);
        comboBox.setSelectedIndex(2);

        comboBox.removeListItem(new TestDto(2));

        assertThat(comboBox.getSelectedIndex()).isEqualTo(1);
    }

    @Test
    void removeOneBeforeSelectedItemWhenNotProvidingNewAdditionTest() {
        fillComboBox(2, 4, 5, 1);

        comboBox.setProvidingNewAddition(false);
        comboBox.setSelectedIndex(2);

        comboBox.removeListItem(new TestDto(2));

        assertThat(comboBox.getSelectedIndex()).isEqualTo(1);
    }

    @Test
    void removeOneAfterSelectedItemWhenProvidingNewAdditionTest() {
        fillComboBox(2, 4, 5, 1, 3);

        comboBox.setProvidingNewAddition(true);
        comboBox.setSelectedIndex(2);

        comboBox.removeListItem(new TestDto(1));

        assertThat(comboBox.getSelectedIndex()).isEqualTo(2);
    }

    @Test
    void removeOneAfterSelectedItemWhenNotProvidingNewAdditionTest() {
        fillComboBox(2, 4, 5, 1, 3);

        comboBox.setProvidingNewAddition(false);
        comboBox.setSelectedIndex(2);

        comboBox.removeListItem(new TestDto(1));

        assertThat(comboBox.getSelectedIndex()).isEqualTo(2);
    }

    @Test
    void selectFirstItemWhenProvidingNewAdditionAddingNewElementTest() {
        comboBox.setProvidingNewAddition(true);
        comboBox.setItemSupplier(() -> Optional.of(new TestDto(-1)));

        fillComboBox(6, 2);
        comboBox.setSelectedIndex(0);

        assertThat(comboBox.getSelectedIndex()).isEqualTo(3);
        assertThat(comboBox.getSelectedListItem()).isEqualTo(new TestDto(-1));
        assertThat(comboBox.getItems()).isEqualTo(List.of(
                new TestDto(6),
                new TestDto(2),
                new TestDto(-1)
        ));
    }

    @Test
    void selectFirstItemWhenProvidingNewAdditionAddingNoElementTest() {
        comboBox.setProvidingNewAddition(true);
        comboBox.setItemSupplier(Optional::empty);

        fillComboBox(6, 2, 3, 4);
        comboBox.setSelectedIndex(2);
        comboBox.setSelectedIndex(0);

        assertThat(comboBox.getSelectedIndex()).isEqualTo(2);
        assertThat(comboBox.getItems()).isEqualTo(List.of(
                new TestDto(6),
                new TestDto(2),
                new TestDto(3),
                new TestDto(4)
        ));
    }

    private void fillComboBox(Integer... numbers) {
        var dtos = Stream.of(numbers)
                .map(TestDto::new)
                .collect(Collectors.toList());

        comboBox.addListItems(dtos);
    }
}
