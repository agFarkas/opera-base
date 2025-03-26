package hu.agfcodeworks.operangel.application.ui.editor;

import hu.agfcodeworks.operangel.application.dto.RoleDto;
import hu.agfcodeworks.operangel.application.ui.text.AutoCompleterDocument;
import hu.agfcodeworks.operangel.application.ui.util.TextUtil;
import lombok.NonNull;
import org.springframework.util.StringUtils;

import javax.swing.DefaultCellEditor;
import javax.swing.JTextField;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import static hu.agfcodeworks.operangel.application.ui.constants.UiConstants.EMPTY_STRING;
import static hu.agfcodeworks.operangel.application.ui.text.Comparators.roleComparator;

public class RoleEditor extends DefaultCellEditor {

    private final int rowIndex;

    private final Function<RoleDto, RoleDto> creator;
    private final Function<RoleDto, RoleDto> updater;
    private final BiConsumer<RoleDto, RoleDto> singleExchanger;

    private final Consumer<Integer> mandatoryProcedure;

    private final BiFunction<RoleDto, String, Optional<OperationOnChangingValue>> operationChooserForNewValue;

    private final List<RoleDto> roleDtos;

    public RoleEditor(
            int rowIndex,
            @NonNull Function<RoleDto, RoleDto> creator,
            @NonNull Function<RoleDto, RoleDto> updater,
            @NonNull BiConsumer<RoleDto, RoleDto> singleExchanger,
            @NonNull Consumer<Integer> mandatoryProcedure,
            @NonNull BiFunction<RoleDto, String, Optional<OperationOnChangingValue>> operationChooserForNewValue,
            @NonNull List<RoleDto> roleDtos
    ) {
        super(new JTextField());
        this.rowIndex = rowIndex;

        this.creator = creator;
        this.updater = updater;
        this.singleExchanger = singleExchanger;

        this.mandatoryProcedure = mandatoryProcedure;
        this.operationChooserForNewValue = operationChooserForNewValue;

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
                return processText(
                        textField.getText()
                );

            }

            private Object processText(String text) {
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
                var newRoleDtoOpt = selectRoleDtoBy(text);

                if (hasOriginalValue() && value instanceof RoleDto originalRoleDto) {
                    newRoleDtoOpt.ifPresent(roleDto -> exchangeExistingRoles(originalRoleDto, roleDto));

                    return newRoleDtoOpt.orElseGet(() -> updateOrCreateValueByChoseOperation(originalRoleDto, text));
                }

                return newRoleDtoOpt.orElseGet(() -> createRoleDto(text));
            }

            private Optional<RoleDto> selectRoleDtoBy(String text) {
                return roleDtos.stream()
                        .filter(r -> Objects.equals(r.getDescription(), text))
                        .findFirst();
            }

            private void exchangeExistingRoles(RoleDto originalRoleDto, RoleDto roleDto) {
                RoleEditor.this.exchangeExistingRoles(originalRoleDto, roleDto);
            }

            private RoleDto updateOrCreateValueByChoseOperation(RoleDto originalRoleDto, String text) {
                var operationOpt = chooseOperation(text);

                if (operationOpt.isPresent()) {
                    return switch (operationOpt.get()) {
                        case UPDATE_ALL_OCCURENCES -> updateValue(((RoleDto) value), text);
                        case CREATE_NEW -> {
                            var roleDto = createRoleDto(text);
                            singleExchanger.accept(originalRoleDto, roleDto);

                            yield roleDto;
                        }
                    };
                }

                return ((RoleDto) value);
            }

            private Optional<OperationOnChangingValue> chooseOperation(String text) {
                return operationChooserForNewValue.apply(((RoleDto) value), text);
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

    private void exchangeExistingRoles(RoleDto originalRoleDto, RoleDto roleDto) {
        singleExchanger.accept(originalRoleDto, roleDto);
    }

    private void prepareEditorComponent() {
        var textField = (JTextField) editorComponent;

        textField.setDocument(
                makeAutoCompleterDocument(textField)
        );
    }

    private AutoCompleterDocument<RoleDto> makeAutoCompleterDocument(JTextField textField) {
        return new AutoCompleterDocument<>(textField, roleDtos, RoleDto::getDescription);
    }

    private RoleDto createRoleDto(String text) {
        return creator.apply(
                makeRoleDto(text)
        );
    }

    private RoleDto updateValue(RoleDto originalRoleDto, String text) {
        return updater.apply(
                makeRoleDto(originalRoleDto, text)
        );
    }

    private RoleDto makeRoleDto(String text) {
        var trimmedText = text.trim();

        return RoleDto.builder()
                .withDescription(trimmedText)
                .withDescriptionUnified(TextUtil.unify(trimmedText))
                .build();
    }

    private RoleDto makeRoleDto(RoleDto originalRoleDto, String text) {
        return RoleDto.builder()
                .withNaturalId(originalRoleDto.getNaturalId())
                .withDescription(text)
                .build();
    }
}
