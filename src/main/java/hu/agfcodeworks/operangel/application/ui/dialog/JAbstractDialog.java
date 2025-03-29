package hu.agfcodeworks.operangel.application.ui.dialog;

import hu.agfcodeworks.operangel.application.exception.DialogValidationException;
import hu.agfcodeworks.operangel.application.exception.ValidationException;
import hu.agfcodeworks.operangel.application.ui.components.custom.labeled.JAbstractedLabeledTextField;
import hu.agfcodeworks.operangel.application.ui.components.custom.labeled.JLabeledComboBox;
import hu.agfcodeworks.operangel.application.ui.components.custom.labeled.JLabeledComponent;
import hu.agfcodeworks.operangel.application.ui.dto.DialogStatus;
import hu.agfcodeworks.operangel.application.ui.text.TextProvider;
import hu.agfcodeworks.operangel.application.ui.util.DialogUtil;
import hu.agfcodeworks.operangel.application.validation.error.DialogValidationErrorDto;
import lombok.NonNull;
import org.springframework.util.CollectionUtils;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static hu.agfcodeworks.operangel.application.ui.dto.DialogStatus.CANCEL;
import static hu.agfcodeworks.operangel.application.ui.dto.DialogStatus.OK;
import static hu.agfcodeworks.operangel.application.validation.ValidationStatus.INVALID_FOR_CONTENT_RULE;
import static hu.agfcodeworks.operangel.application.validation.ValidationStatus.INVALID_FOR_MANDATORY;

public abstract class JAbstractDialog<V> extends JDialog {

    protected static final String NO_WHITESPACE_REGEX = "[^-\\s]{1,}";

    private static final String TITLE_MODIFICATION_PATTERN = "%s módosítása";

    protected static final String VALIDATION_MESSAGE_NO_WHITESPACE = "Nem tartalmazhat szóközt.";

    protected static final String VALIDATION_MESSAGE_NUMBERS_ONLY = "Csak számot tartalmazhat.";

    protected static final String VALIDATION_MESSAGE_MANDATORY = "Kötelező";

    private static final int HORIZONTAL_DISTANCE_FROM_OWNER = 30;

    private static final int VERTICAL_DISTANCE_FROM_OWNER = 15;

    private Container contentPane;

    private JButton buttonOK = new JButton("OK");

    private JButton buttonCancel = new JButton("Mégsem");

    private JPanel formPane = new JPanel();

    private JPanel buttonPane = new JPanel(new FlowLayout(FlowLayout.RIGHT));

    protected V value;

    private DialogStatus dialogStatus = CANCEL;

    public JAbstractDialog(Frame owner) {
        super(owner);
        initiateDialog(owner, obtainTitle());
    }

    public JAbstractDialog(Frame owner, TextProvider<V> textProvider, V initialValue) {
        super(owner);
        this.value = initialValue;
        initiateDialog(owner, composeModificationTitle(value, textProvider));
    }

    private void initiateDialog(Frame owner, String title) {
        this.contentPane = getContentPane();

        setLocation(new Point(owner.getX() + HORIZONTAL_DISTANCE_FROM_OWNER, owner.getY() + VERTICAL_DISTANCE_FROM_OWNER));

        setTitle(title);


        formPane.setLayout(new BoxLayout(formPane, BoxLayout.PAGE_AXIS));

        buttonPane.add(buttonOK);
        buttonPane.add(buttonCancel);

        contentPane.setLayout(new BorderLayout());
        contentPane.add(formPane);
        contentPane.add(buttonPane, BorderLayout.PAGE_END);

        buildFormPane(formPane);
        initiateValue();

        setModal(true);
        getRootPane()
                .setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> onOK());
        buttonCancel.addActionListener(e -> onCancel());

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        setResizable(false);
        pack();
    }

    private <T> String composeModificationTitle(T dto, @NonNull TextProvider<T> textProvider) {
        return TITLE_MODIFICATION_PATTERN.formatted(
                textProvider.provide(dto)
        );
    }

    protected List<DialogValidationErrorDto> getErrorDtos(JLabeledComboBox<?> comboBox) {
        var validationStatuses = comboBox.getValidationStatus();

        if (!CollectionUtils.isEmpty(validationStatuses)) {
            return List.of(makeErrorDtoOfMandatoryMessage(comboBox));
        }

        return Collections.emptyList();
    }

    protected List<DialogValidationErrorDto> getErrorDtos(JAbstractedLabeledTextField<?> textField) {
        var validationStatuses = textField.getValidationStatus();
        var errorDtos = new LinkedList<DialogValidationErrorDto>();

        if (validationStatuses.contains(INVALID_FOR_MANDATORY)) {
            errorDtos.add(makeErrorDtoOfMandatoryMessage(textField));
        }

        if (validationStatuses.contains(INVALID_FOR_CONTENT_RULE)) {
            errorDtos.add(makeErrorMessageOfTextField(textField));
        }

        return errorDtos;
    }

    private DialogValidationErrorDto makeErrorDtoOfMandatoryMessage(JLabeledComponent<?> labeledComponent) {
        return new DialogValidationErrorDto(
                labeledComponent.getLabelText(),
                VALIDATION_MESSAGE_MANDATORY
        );
    }

    private DialogValidationErrorDto makeErrorMessageOfTextField(JAbstractedLabeledTextField<?> textField) {
        return new DialogValidationErrorDto(
                textField.getLabelText(),
                textField.getValidationMessage()
        );
    }

    private void onOK() {
        try {
            validateContent();

            this.value = composeValue();
            this.dialogStatus = OK;
            dispose();
        } catch (ValidationException ex) {
            DialogUtil.showWarningMessageByValidation(getOwner(), ex);
        }
    }

    protected void validateContent() {
        var errorDtos = validateCustomFields();

        if (!CollectionUtils.isEmpty(errorDtos)) {
            throw new DialogValidationException(errorDtos);
        }
    }

    protected abstract List<DialogValidationErrorDto> validateCustomFields();

    private void onCancel() {
        dispose();
    }

    protected abstract void buildFormPane(JPanel formPane);

    protected abstract void initiateValue();

    protected abstract V composeValue();

    public V getValue() {
        return value;
    }

    public DialogStatus getDialogStatus() {
        return dialogStatus;
    }

    protected abstract String obtainTitle();
}
