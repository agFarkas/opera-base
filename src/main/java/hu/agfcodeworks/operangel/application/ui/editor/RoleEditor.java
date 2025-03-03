package hu.agfcodeworks.operangel.application.ui.editor;

import hu.agfcodeworks.operangel.application.dto.RoleDto;
import hu.agfcodeworks.operangel.application.ui.text.AutoCompleterDocument;
import lombok.NonNull;
import org.springframework.util.StringUtils;

import javax.swing.DefaultCellEditor;
import javax.swing.JTextField;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import static hu.agfcodeworks.operangel.application.ui.constants.UiConstants.EMPTY_STRING;
import static hu.agfcodeworks.operangel.application.ui.editor.OperationOnChangingValue.UPDATE_EXISTING;
import static hu.agfcodeworks.operangel.application.ui.text.Comparators.roleComparator;

public class RoleEditor extends DefaultCellEditor {

    private final int rowIndex;

    private final Function<RoleDto, RoleDto> creator;

    private final Function<RoleDto, RoleDto> updater;

    private final Consumer<Integer> mandatoryProcedure;

    BiFunction<RoleDto, String, Optional<OperationOnChangingValue>> operationChooser;

    private final List<RoleDto> roleDtos;

    public RoleEditor(
            int rowIndex,
            @NonNull Function<RoleDto, RoleDto> creator,
            @NonNull Function<RoleDto, RoleDto> updater,
            @NonNull Consumer<Integer> mandatoryProcedure,
            @NonNull BiFunction<RoleDto, String, Optional<OperationOnChangingValue>> operationChooser,
            @NonNull List<RoleDto> roleDtos
    ) {
        super(new JTextField());
        this.rowIndex = rowIndex;

        this.creator = creator;
        this.updater = updater;

        this.mandatoryProcedure = mandatoryProcedure;
        this.operationChooser = operationChooser;

        this.roleDtos = roleDtos.stream()
                .sorted(roleComparator)
                .toList();


        prepareEditorComponent();

        this.delegate = makeDelegate();
    }

    private EditorDelegate makeDelegate() {
        return new EditorDelegate() {

            @Override
            public Object getCellEditorValue() {
                var textField = (JTextField) editorComponent;
                var text = textField.getText();

                if (!StringUtils.hasText(text)) {
                    if (hasOriginalValue()) {
                        return value;
                    }

                    return null;
                }

                var trimmedText = text.trim();
                var roleDto = obtainRoleDto(trimmedText);

                mandatoryProcedure.accept(rowIndex);

                return roleDto;
            }

            private boolean hasOriginalValue() {
                return Objects.nonNull(value);
            }

            private RoleDto obtainRoleDto(String text) {
                var roleOpt = roleDtos.stream()
                        .filter(r -> Objects.equals(r.getDescription(), text))
                        .findFirst();

                if (hasOriginalValue()) {
                    return roleOpt.orElseGet(() -> updateOrCreateValueByChoseOperation(text));
                }

                return roleOpt.orElseGet(() -> createValue(text));
            }

            private RoleDto updateOrCreateValueByChoseOperation(String text) {
                var operationOpt = chooseOperation(text);
                if (operationOpt.isEmpty()) {
                    return ((RoleDto) value);
                }

                var operation = operationOpt.get();
                if (operation == UPDATE_EXISTING) {
                    return updateValue(((RoleDto) value), text);
                }

                return createValue(text);
            }

            private Optional<OperationOnChangingValue> chooseOperation(String text) {
                return operationChooser.apply(((RoleDto) value), text);
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

    private RoleDto updateValue(RoleDto originalRoleDto, String text) {
        var roleDto = RoleDto.builder()
                .withNaturalId(originalRoleDto.getNaturalId())
                .withDescription(text)
                .build();

        return updater.apply(roleDto);
    }
}
