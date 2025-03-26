package hu.agfcodeworks.operangel.application.ui.renderer;

import hu.agfcodeworks.operangel.application.ui.custom.components.itemdto.TestDto;
import hu.agfcodeworks.operangel.application.ui.uidto.ListItemWrapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import javax.swing.JList;
import java.util.stream.Stream;

import static hu.agfcodeworks.operangel.application.constants.StringConstants.EMPTY_TEXT;
import static hu.agfcodeworks.operangel.application.ui.constants.UiConstants.LIST_ITEM_CREATE_NEW;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CustomComboBoxRendererTest {

    private CustomComboBoxRenderer<TestDto> comboBoxRenderer;

    private JList<?> jListMock = mock(JList.class);

    @BeforeAll
    void init() {
        this.comboBoxRenderer = new CustomComboBoxRenderer<>(t -> Integer.toString(t.getNumber()));

        when(jListMock.getFont()).thenCallRealMethod();
        when(jListMock.getForeground()).thenCallRealMethod();
        when(jListMock.getBackground()).thenCallRealMethod();
    }

    @ParameterizedTest
    @ArgumentsSource(CustomComboBoxRendererArgumentProvider.class)
    void getListCellRendererComponentTest(ListItemWrapper<TestDto> value, String expectedText) {
        comboBoxRenderer.getListCellRendererComponent(jListMock, value, 0, true, true);

        assertThat(comboBoxRenderer.getText()).isEqualTo(expectedText);
    }

    static class CustomComboBoxRendererArgumentProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                    Arguments.of(ListItemWrapper.of(new TestDto(2)), "2"),
                    Arguments.of(ListItemWrapper.ofToAddNew(), "[[%s]]".formatted(LIST_ITEM_CREATE_NEW)),
                    Arguments.of(null, EMPTY_TEXT)
            );
        }
    }
}
