package hu.agfcodeworks.operangel.application.ui.editor;

import hu.agfcodeworks.operangel.application.dto.RoleDto;
import hu.agfcodeworks.operangel.application.ui.text.AutoCompleterDocument;
import lombok.NonNull;
import org.springframework.util.StringUtils;

import javax.swing.DefaultCellEditor;
import javax.swing.JTextField;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import static hu.agfcodeworks.operangel.application.ui.constants.UiConstants.EMPTY_STRING;
import static hu.agfcodeworks.operangel.application.ui.text.Comparators.roleComparator;

public class RoleEditor extends DefaultCellEditor {

    private final Function<RoleDto, RoleDto> creator;

    private final Function<RoleDto, RoleDto> updater;

    private final List<RoleDto> roleDtos;

    public RoleEditor(
            @NonNull Function<RoleDto, RoleDto> creator,
            @NonNull Function<RoleDto, RoleDto> updater,
            @NonNull List<RoleDto> roleDtos
    ) {
        super(new JTextField());

        this.roleDtos = roleDtos.stream()
                .sorted(roleComparator)
                .toList();

        this.creator = creator;
        this.updater = updater;

        prepareEditorComponent();

        this.delegate = new EditorDelegate() {

            @Override
            public Object getCellEditorValue() {
                var textField = (JTextField) editorComponent;
                var text = textField.getText();

                if (!StringUtils.hasText(text)) {
                    if (Objects.nonNull(value)) {
                        return value;
                    }

                    return null;
                }

                var trimmedText = text.trim();
                var roleOpt = roleDtos.stream()
                        .filter(r -> Objects.equals(r.getDescription(), trimmedText))
                        .findFirst();

                return roleOpt.orElseGet(() -> createValue(text));
            }

            @Override
            public void setValue(Object value) {
                super.setValue(value);
                var textField = (JTextField) editorComponent;

                if (Objects.isNull(value)) {
                    textField.setText(EMPTY_STRING);
                } else if (value instanceof RoleDto role) {
                    textField.setText(role.getDescription());
                }
            }
        };
    }

    private void prepareEditorComponent() {
        var textField = (JTextField) editorComponent;

        textField.setDocument(new AutoCompleterDocument<>(textField, roleDtos, RoleDto::getDescription));
    }

    private RoleDto createValue(String text) {
        return creator.apply(RoleDto.builder()
                .withDescription(text.trim())
                .build());
    }

    private RoleDto updateValue(RoleDto roleDto, String trimmedText) {
        return updater.apply(RoleDto.builder()
                .withNaturalId(roleDto.getNaturalId())
                .withDescription(trimmedText)
                .build());
    }
}
